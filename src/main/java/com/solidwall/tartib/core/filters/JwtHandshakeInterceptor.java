package com.solidwall.tartib.core.filters;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.solidwall.tartib.core.configs.CustomUserDetailsService;
import com.solidwall.tartib.core.utils.JwtUtil;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String uri = request.getURI().toString();
        String token = null;

        if (uri.contains("token=")) {
            token = uri.substring(uri.indexOf("token=") + 6).split("&")[0];
            System.out.println("[Handshake] Extracted token: " + token);
        }

        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            attributes.put("userDetails", userDetails);

            // Set the Principal for Spring Security
            attributes.put("principal", (Principal) userDetails::getUsername);

            System.out.println("[Handshake] Principal set: " + userDetails.getUsername());
        } else {
            System.out.println("[Handshake] Invalid or missing token");
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No-op
    }
}
