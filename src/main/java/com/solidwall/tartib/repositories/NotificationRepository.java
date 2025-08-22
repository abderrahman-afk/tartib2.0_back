package com.solidwall.tartib.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.solidwall.tartib.entities.Notification;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Additional query methods can be defined here if needed
    // For example, to find notifications by user:
    // List<Notification> findByUser(UserEntity user);  
  List<Notification> findByRecipientIdAndReadFalse(Long recipientId);
  Notification findByIdAndRecipientIdAndReadFalse(Long notificationId, Long recipientId);


}
