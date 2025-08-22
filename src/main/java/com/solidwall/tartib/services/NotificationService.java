package com.solidwall.tartib.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.solidwall.tartib.core.exceptions.NotFoundException;
import com.solidwall.tartib.dto.notification.NotificationDTO;
import com.solidwall.tartib.dto.notification.NotificationSummaryDTO;
import com.solidwall.tartib.entities.Notification;
import com.solidwall.tartib.entities.UserEntity;
import com.solidwall.tartib.repositories.NotificationRepository;
import com.solidwall.tartib.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;

  public void buildAndSendNotification(long currentUserId, String message, String type) {
    UserEntity currentUser = userRepository.findById(currentUserId)
            .orElseThrow(() -> new NotFoundException("Current user not found"));

    List<UserEntity> allUsers = userRepository.findAll();

    for (UserEntity user : allUsers) {
        boolean hasPermission = user.getUserRoles().stream()
                .anyMatch(role -> role.getRole().getRoleAccess().stream()
                        .anyMatch(permission -> permission.getAccess().getValue().equals(type)));
        if (!hasPermission) continue;

        Notification notif = Notification.builder()
                .message(currentUser.getUsername() + ": " + message)
                .type(type)
                .read(false)
                .createdBy(currentUser)
                .recipient(user) // set recipient
                .build();

        Notification savedNotif = notificationRepository.save(notif);

        NotificationDTO notifDTO = NotificationDTO.builder()
                .id(savedNotif.getId())
                .message(savedNotif.getMessage())
                .type(savedNotif.getType())
                .time(savedNotif.getCreatedAt())
                .read(savedNotif.isRead())
                .build();

        // Send to user via WebSocket
        messagingTemplate.convertAndSendToUser(
                user.getUsername(),
                "/queue/notifications",
                notifDTO
        );
        messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), notifDTO);


        // Optionally, send summary of unread notifications
        sendUnreadNotificationsToUser(user.getUsername(), user.getId());
    }
}


   
public void markNotificationAsRead(Long notificationId , Long recipientId) {
    Notification notification = notificationRepository.findByIdAndRecipientIdAndReadFalse(notificationId, recipientId) ;
    notification.setRead(true);
    notificationRepository.save(notification);

}

 public void sendUnreadNotificationsToUser(String username  ,Long userId) {
        List<Notification> unread = notificationRepository.findByRecipientIdAndReadFalse(userId);
        
        if (unread.isEmpty()) {
                     System.err.println("🤟");
            return; // No unread notifications to send
        }
        Map<String, List<Notification>> byType = unread.stream()
                .collect(Collectors.groupingBy(Notification::getType));
        NotificationSummaryDTO summary = new NotificationSummaryDTO();
        summary.setUnreadCount(unread.size());
        summary.setNotificationsByType(byType);

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                summary
        );
        System.err.println("🤟!!!!!!!*");
    }


 public List<Notification> getReadNotifications(Long userId) {
    return notificationRepository.findByRecipientIdAndReadFalse(userId);
 }



}