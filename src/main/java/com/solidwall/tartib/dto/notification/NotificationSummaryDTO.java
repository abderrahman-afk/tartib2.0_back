package com.solidwall.tartib.dto.notification;



import com.solidwall.tartib.entities.Notification;
import java.util.List;
import java.util.Map;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class NotificationSummaryDTO {
    private int unreadCount;
    private Map<String, List<Notification>> notificationsByType;

    
}
