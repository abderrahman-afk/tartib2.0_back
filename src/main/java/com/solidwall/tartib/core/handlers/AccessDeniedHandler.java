package com.solidwall.tartib.core.handlers;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException e) throws IOException, ServletException {

    System.out.println("-----------AccessDeniedHandler----------------------");

    // You can create your own repsonse here to handle method level access denied
    // reponses..
    // Follow similar method to the bad credentials handler above.
  }

  /*
   * @Override
   * public void onAuthenticationFailure(HttpServletRequest request,
   * HttpServletResponse response,
   * AuthenticationException exception) throws IOException, ServletException {
   * 
   * System.out.println("-----------onAuthenticationFailure----------------------"
   * );
   * 
   * String username = request.getParameter("username");
   * UserEntity user = userRepository.findByUsername(username).get();
   * if (user != null) {
   * // Increment failed login attempts
   * user.setAccountNonExpired(false);
   * 
   * userRepository.save(user);
   * }
   * }
   */

}
