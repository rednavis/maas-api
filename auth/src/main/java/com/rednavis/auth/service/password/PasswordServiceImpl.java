package com.rednavis.auth.service.password;

import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.RuleResult;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired
  private PasswordValidator passwordValidator;

  @Override
  public List<String> checkPasswordComplexity(String password) {
    PasswordData passwordData = new PasswordData(new Password(password));
    RuleResult result = passwordValidator.validate(passwordData);
    if (!result.isValid()) {
      return passwordValidator.getMessages(result);
    }
    return Collections.emptyList();
  }

  @Override
  public String generatePassword(String password) throws CannotPerformOperationException {
    return PasswordUtils.createHash(password);
  }

  @Override
  public boolean validatePassword(String passwordDb, String password) throws CannotPerformOperationException, InvalidHashException {
    return PasswordUtils.verifyPassword(password, passwordDb);
  }

  @Override
  public String encode(CharSequence rawPassword) {
    try {
      return generatePassword(rawPassword.toString());
    } catch (CannotPerformOperationException ex) {
      log.error("Error generate password", ex);
      throw new RuntimeException("Error generate password", ex);
    }
  }

  @Override
  public boolean matches(CharSequence cs, String string) {
    return encode(cs).equals(string);
  }
}
