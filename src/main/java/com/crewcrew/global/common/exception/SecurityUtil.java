package com.crewcrew.global.common.exception;

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
}
