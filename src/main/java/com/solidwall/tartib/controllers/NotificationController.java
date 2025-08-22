package com.solidwall.tartib.controllers;

import com.solidwall.tartib.entities.Notification;
import com.solidwall.tartib.services.AuthenticationFacade;
import com.solidwall.tartib.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
  AuthenticationFacade authenticationFacade;


   

    @PostMapping("/read/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        long getCurrentUserId = authenticationFacade.getCurrentUserId();
        notificationService.markNotificationAsRead(notificationId , getCurrentUserId);
        
    }
    //get notifications for the current user
    @GetMapping("/read")
    public List<Notification> getreadNotifications() {
         long getCurrentUserId = authenticationFacade.getCurrentUserId();
    return notificationService.getReadNotifications(getCurrentUserId);
    }
}