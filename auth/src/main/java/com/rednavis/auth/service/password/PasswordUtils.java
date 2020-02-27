package com.rednavis.auth.service.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import lombok.experimental.UtilityClass;

@UtilityClass
class PasswordUtils {

  private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

  // These constants may be changed without breaking existing hashes.
  private static final int SALT_BYTE_SIZE = 24;
  private static final int HASH_BYTE_SIZE = 18;
  private static final int PBKDF2_ITERATIONS = 64000;

  // These constants define the encoding and may not be changed.
  private static final int HASH_SECTIONS = 5;
  private static final int HASH_ALGORITHM_INDEX = 0;
  private static final int ITERATION_INDEX = 1;
  private static final int HASH_SIZE_INDEX = 2;
  private static final int SALT_INDEX = 3;
  private static final int PBKDF2_INDEX = 4;

  public static String createHash(String password) throws CannotPerformOperationException {
    return createHash(password.toCharArray());
  }

  public static String createHash(char[] password) throws CannotPerformOperationException {
    // Generate a random salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_BYTE_SIZE];
    random.nextBytes(salt);

    // Hash the password
    byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
    int hashSize = hash.length;

    // format: algorithm:iterations:hashSize:salt:hash
    return "sha1:" + PBKDF2_ITERATIONS + ":" + hashSize + ":" + toBase64(salt) + ":" + toBase64(hash);
  }

  public static boolean verifyPassword(String password, String correctHash) throws CannotPerformOperationException, InvalidHashException {
    return verifyPassword(password.toCharArray(), correctHash);
  }

  public static boolean verifyPassword(char[] password, String correctHash) throws CannotPerformOperationException, InvalidHashException {
    // Decode the hash into its parameters
    String[] params = correctHash.split(":");
    checkCommon(params);
    int iterations = checkIterations(params);
    byte[] salt = checkSalt(params);
    byte[] hash = checkHash(params);
    checkStoredHashSize(params, hash);

    // Compute the hash of the provided password, using the same salt,
    // iteration count, and hash length
    byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

    // Compare the hashes in constant time. The password is correct if
    // both hashes match.
    return slowEquals(hash, testHash);
  }

  private static void checkStoredHashSize(String[] params, byte[] hash) throws InvalidHashException {
    int storedHashSize;
    try {
      storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
    } catch (NumberFormatException e) {
      throw new InvalidHashException("Could not parse the hash size as an integer.", e);
    }

    if (storedHashSize != hash.length) {
      throw new InvalidHashException("Hash length doesn't match stored hash length.");
    }
  }

  private static byte[] checkHash(String[] params) throws InvalidHashException {
    byte[] hash;
    try {
      hash = fromBase64(params[PBKDF2_INDEX]);
    } catch (IllegalArgumentException e) {
      throw new InvalidHashException("Base64 decoding of pbkdf2 output failed.", e);
    }
    return hash;
  }

  private static byte[] checkSalt(String[] params) throws InvalidHashException {
    byte[] salt;
    try {
      salt = fromBase64(params[SALT_INDEX]);
    } catch (IllegalArgumentException e) {
      throw new InvalidHashException("Base64 decoding of salt failed.", e);
    }
    return salt;
  }

  private static int checkIterations(String[] params) throws InvalidHashException {
    int iterations;
    try {
      iterations = Integer.parseInt(params[ITERATION_INDEX]);
    } catch (NumberFormatException e) {
      throw new InvalidHashException("Could not parse the iteration count as an integer.", e);
    }

    if (iterations < 1) {
      throw new InvalidHashException("Invalid number of iterations. Must be >= 1.");
    }
    return iterations;
  }

  private static void checkCommon(String[] params) throws InvalidHashException, CannotPerformOperationException {
    if (params.length != HASH_SECTIONS) {
      throw new InvalidHashException("Fields are missing from the password hash.");
    }

    // Currently, Java only supports SHA1.
    if (!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
      throw new CannotPerformOperationException("Unsupported hash type.");
    }
  }

  private static boolean slowEquals(byte[] a, byte[] b) {
    int diff = a.length ^ b.length;
    for (int i = 0; i < a.length && i < b.length; i++) {
      diff |= a[i] ^ b[i];
    }
    return diff == 0;
  }

  private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws CannotPerformOperationException {
    try {
      PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
      return skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException e) {
      throw new CannotPerformOperationException("Hash algorithm not supported.", e);
    } catch (InvalidKeySpecException e) {
      throw new CannotPerformOperationException("Invalid key spec.", e);
    }
  }

  private static byte[] fromBase64(String hex) {
    return Base64.getDecoder().decode(hex);
  }

  private static String toBase64(byte[] array) {
    return Base64.getEncoder().encodeToString(array);
  }
}
