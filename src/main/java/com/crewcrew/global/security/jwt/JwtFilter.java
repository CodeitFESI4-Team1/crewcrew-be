package com.crewcrew.global.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crewcrew.domain.member.dto.CustomUserDetails;
import com.crewcrew.domain.member.entity.Member;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String authorization = request.getHeader("Authorization");
      if (authorization == null || !authorization.startsWith("Bearer "))
        throw new Exception("NOT_FOUND_TOKEN");

      String accessToken = authorization.split(" ")[1];
      jwtUtil.isExpired(accessToken);

      String category = jwtUtil.getCategory(accessToken);
      if (!category.equals("access")) throw new Exception("INVALID_TOKEN");

      Long userId = jwtUtil.getUserId(accessToken);
      String userEmail = jwtUtil.getUserEmail(accessToken);

      Member tempMember = Member.builder().id(userId).email(userEmail).build();
      CustomUserDetails customUserDetails = new CustomUserDetails(tempMember);

      Authentication authToken =
          new UsernamePasswordAuthenticationToken(
              customUserDetails, null, customUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authToken);

    } catch (ExpiredJwtException e) {
      request.setAttribute("exception", new Exception("EXPIRED_TOKEN"));
    } catch (Exception e) {
      request.setAttribute("exception", new Exception("INVALID_TOKEN"));
    }

    filterChain.doFilter(request, response);
  }
}
