package com.solidwall.tartib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.core.exceptions.UnauthorisedException;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.repositories.UserRepository;

@Component
public class AuthenticationFacade {
    
    @Autowired
    private UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current*** user not found"));
            return user.getId();
        }
        
        throw new UnauthorisedException("User not authenticated");
    }

    public UserEntity getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("Current user not found"));
    }
}