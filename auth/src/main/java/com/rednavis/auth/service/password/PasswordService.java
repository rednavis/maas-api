package com.rednavis.auth.service.password;

import java.util.List;

/**
 * Service for work with passwords. Checking the password for validity. Generate a token for the password.
 *
 * <p>Validation of the token for a given password
 */
public interface PasswordService {

  List<String> checkPasswordComplexity(String password);

  String generatePassword(String password);

  boolean validatePassword(String passwordDb, String password);
}
