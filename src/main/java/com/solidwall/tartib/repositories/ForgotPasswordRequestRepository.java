package com.solidwall.tartib.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solidwall.tartib.entities.ForgotPasswordRequestEntity;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.enums.ForgotPasswordStatus;

@Repository
public interface ForgotPasswordRequestRepository extends JpaRepository<ForgotPasswordRequestEntity, Long> {
    
    List<ForgotPasswordRequestEntity> findByStatus(ForgotPasswordStatus status);
    
    List<ForgotPasswordRequestEntity> findByUser(UserEntity user);
    
    Optional<ForgotPasswordRequestEntity> findByUserAndStatus(UserEntity user, ForgotPasswordStatus status);
    
    List<ForgotPasswordRequestEntity> findByEmailOrderByCreatedAtDesc(String email);
}