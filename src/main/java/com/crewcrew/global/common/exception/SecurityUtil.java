package com.crewcrew.global.common.exception;

import java.util.Optional;

import com.crewcrew.domain.member.dto.CustomUserDetails;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {

  public static String getCurrentUsername(CustomUserDetails userDetails) {
    if (userDetails == null) {
      throw new ApiException(ErrorCode.USER_NOT_FOUND);
    }
    return userDetails.getUsername();
  }

  public static String getEmailOrNull(CustomUserDetails userDetails) {
    return Optional.ofNullable(userDetails).map(CustomUserDetails::getUsername).orElse(null);
  }
}
