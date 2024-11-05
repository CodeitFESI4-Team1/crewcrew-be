package com.crewcrew.domain.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.crewcrew.domain.member.annotation.ExistEmail;
import com.crewcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

  private final MemberRepository memberRepository;

  @Override
  public void initialize(ExistEmail constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = memberRepository.findByEmail(value).isPresent();

    if (isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("EXIST_EMAIL").addConstraintViolation();
    }

    return !isValid;
  }
}
