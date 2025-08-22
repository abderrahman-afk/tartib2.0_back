package com.solidwall.tartib.core.filters;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.solidwall.tartib.core.configs.CustomUserDetailsService;
import com.solidwall.tartib.core.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      username = jwtUtil.extractUsername(jwt);
      // Debug: print full claims
      try {
        // Use JwtUtil's extractAllClaims via reflection if private, or duplicate logic here
        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.parserBuilder()
          .setSigningKey(jwtUtil.getClass().getDeclaredField("secretKey").get(jwtUtil).toString().getBytes())
          .build()
          .parseClaimsJws(jwt)
          .getBody();
        System.out.println("[DEBUG] JWT Claims: " + claims);
      } catch (Exception e) {
        System.out.println("[DEBUG] Could not print JWT claims: " + e.getMessage());
      }
    }

    // IP address check
    if (jwt != null) {
      String tokenIp = jwtUtil.extractIp(jwt);
      String requestIp = request.getHeader("X-Forwarded-For");
      if (requestIp == null || requestIp.isEmpty()) {
        requestIp = request.getRemoteAddr();
      }
      System.out.println("[DEBUG] Token IP: " + tokenIp);
      System.out.println("[DEBUG] Request IP: " + requestIp);
      if (tokenIp != null && !tokenIp.equals(requestIp)) {
        System.out.println("[DEBUG] IP address mismatch. Rejecting request.");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "IP address mismatch");
        return;
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

     if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
      // Check if token is expiring soon and refresh it
      if (jwtUtil.isTokenExpiringSoon(jwt)) {
          String newToken = jwtUtil.refreshToken(jwt);
          response.setHeader("New-Token", newToken);
          System.out.println("[DEBUG] Token refreshed for user: " + username);
      }

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities());
      usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }

    }
    chain.doFilter(request, response);
  }

}
