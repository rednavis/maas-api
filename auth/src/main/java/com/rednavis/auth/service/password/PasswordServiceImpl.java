package com.rednavis.auth.service.password;

import com.rednavis.core.exception.MaasApiException;
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
  public String generatePassword(String password) {
    try {
      return PasswordUtils.createHash(password);
    } catch (CannotPerformOperationException e) {
      log.error("Can't generate password [password: {}]", password, e);
      throw new MaasApiException("Can't validate [password: " + password + "]");
    }
  }

  @Override
  public boolean validatePassword(String passwordDb, String password) {
    try {
      return PasswordUtils.verifyPassword(password, passwordDb);
    } catch (CannotPerformOperationException | InvalidHashException e) {
      log.error("Can't validate password [password: {}]", password, e);
      throw new MaasApiException("Can't validate [password: " + password + "]");
    }
  }
}
