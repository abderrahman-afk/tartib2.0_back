package com.solidwall.tartib.dto.notification;




import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
  
    private Date time;
   
    private boolean read;

 

}
