package com.rednavis.auth.service.password;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Service for work with passwords. Checking the password for validity. Generate a token for the password. Validation of the token for a given
 * password
 */
public interface PasswordService extends PasswordEncoder {

  List<String> checkPasswordComplexity(String password);

  String generatePassword(String password) throws CannotPerformOperationException;

  boolean validatePassword(String passwordDb, String password) throws CannotPerformOperationException, InvalidHashException;
}
