package com.solidwall.tartib.core.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.solidwall.tartib.core.filters.JwtRequestFilter;
import com.solidwall.tartib.core.handlers.AccessDeniedHandler;
import com.solidwall.tartib.core.utils.JwtAuthEntryPoint;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Autowired
  JwtAuthEntryPoint jwtAuthEntryPoint;

  @Autowired
  JwtRequestFilter jwtRequestFilter;

  @Autowired
  AccessDeniedHandler accessDeniedHandler;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.cors(Customizer.withDefaults());
    httpSecurity.csrf((csrf) -> csrf.disable());
    httpSecurity.authorizeHttpRequests(auth -> {
  auth.requestMatchers("/auth/**", "/auth/signin-with-token","/ws/**", "/api/ws/**","/api/forgot-password/**","/forgot-password/**" ).permitAll();
        // auth.requestMatchers("/**").permitAll();

      auth.anyRequest().authenticated();
    });
    httpSecurity.exceptionHandling(handling -> {
      handling.authenticationEntryPoint(jwtAuthEntryPoint);
      handling.accessDeniedHandler(accessDeniedHandler);
    });
    httpSecurity.sessionManagement(Customizer.withDefaults());
    httpSecurity.httpBasic(Customizer.withDefaults());
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();

  }

}
