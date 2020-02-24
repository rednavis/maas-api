package com.rednavis.auth.config;

import edu.vt.middleware.password.AlphabeticalCharacterRule;
import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

  @Value("${password.validation.minLength}")
  private int minLength;
  @Value("${password.validation.maxLength}")
  private int maxLength;
  @Value("${password.validation.whitespaceAllowed}")
  private boolean whitespaceAllowed;
  @Value("${password.validation.requireLeastNumberOfDigits}")
  private int requireLeastNumberOfDigits;
  @Value("${password.validation.requireLeastNumberOfAlphabeticals}")
  private int requireLeastNumberOfAlphabeticals;
  @Value("${password.validation.requireLeastNumberOfNotAlphanumerics}")
  private int requireLeastNumberOfNotAlphanumerics;
  @Value("${password.validation.requireLeastNumberOfLowercases}")
  private int requireLeastNumberOfLowercases;
  @Value("${password.validation.requireLeastNumberOfUppercases}")
  private int requireLeastNumberOfUppercases;
  @Value("${password.validation.allowAlphabeticalSequence}")
  private boolean allowAlphabeticalSequence;
  @Value("${password.validation.allowNumericalSequence}")
  private boolean allowNumericalSequence;
  @Value("${password.validation.allowQwertySequence}")
  private boolean allowQwertySequence;
  @Value("${password.validation.allowRepeatCharactersMax}")
  private int allowRepeatCharactersMax;

  /**
   * passwordValidator.
   *
   * @return
   */
  @Bean
  public PasswordValidator passwordValidator() {
    List<Rule> ruleList = new ArrayList<>();
    checkPasswordLength(ruleList);
    checkRequireLeastNumberOfDigits(ruleList);
    checkRequireLeastNumberOfNotAlphanumerics(ruleList);
    checkRequireLeastNumberOfLowercases(ruleList);
    checkRequireLeastNumberOfUppercases(ruleList);
    checkAllowRepeatCharactersMax(ruleList);
    checkWhitespaceAllowed(ruleList);
    checkAllowAlphabeticalSequence(ruleList);
    checkAllowNumericalSequence(ruleList);
    checkAllowQwertySequence(ruleList);
    checkRequireLeastNumberOfAlphabeticals(ruleList);
    return new PasswordValidator(ruleList);
  }

  private void checkRequireLeastNumberOfAlphabeticals(List<Rule> ruleList) {
    if (requireLeastNumberOfAlphabeticals > 0) {
      ruleList.add(new AlphabeticalCharacterRule(requireLeastNumberOfAlphabeticals));
    }
  }

  private void checkAllowQwertySequence(List<Rule> ruleList) {
    if (allowQwertySequence) {
      ruleList.add(new QwertySequenceRule());
    }
  }

  private void checkAllowNumericalSequence(List<Rule> ruleList) {
    if (allowNumericalSequence) {
      ruleList.add(new NumericalSequenceRule());
    }
  }

  private void checkAllowAlphabeticalSequence(List<Rule> ruleList) {
    if (allowAlphabeticalSequence) {
      ruleList.add(new AlphabeticalSequenceRule());
    }
  }

  private void checkWhitespaceAllowed(List<Rule> ruleList) {
    if (whitespaceAllowed) {
      ruleList.add(new WhitespaceRule());
    }
  }

  private void checkAllowRepeatCharactersMax(List<Rule> ruleList) {
    if (allowRepeatCharactersMax > 0) {
      ruleList.add(new RepeatCharacterRegexRule(allowRepeatCharactersMax));
    }
  }

  private void checkRequireLeastNumberOfUppercases(List<Rule> ruleList) {
    if (requireLeastNumberOfUppercases > 0) {
      ruleList.add(new UppercaseCharacterRule(requireLeastNumberOfUppercases));
    }
  }

  private void checkRequireLeastNumberOfLowercases(List<Rule> ruleList) {
    if (requireLeastNumberOfLowercases > 0) {
      ruleList.add(new LowercaseCharacterRule(requireLeastNumberOfLowercases));
    }
  }

  private void checkRequireLeastNumberOfNotAlphanumerics(List<Rule> ruleList) {
    if (requireLeastNumberOfNotAlphanumerics > 0) {
      ruleList.add(new NonAlphanumericCharacterRule(requireLeastNumberOfNotAlphanumerics));
    }
  }

  private void checkRequireLeastNumberOfDigits(List<Rule> ruleList) {
    if (requireLeastNumberOfDigits > 0) {
      ruleList.add(new DigitCharacterRule(requireLeastNumberOfDigits));
    }
  }

  private void checkPasswordLength(List<Rule> ruleList) {
    if (minLength > 0 && maxLength > 0 && minLength < maxLength) {
      ruleList.add(new LengthRule(minLength, maxLength));
    }
  }
}
