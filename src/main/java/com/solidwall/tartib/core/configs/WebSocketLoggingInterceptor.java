package com.solidwall.tartib.core.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
public class WebSocketLoggingInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getUser() != null) {
            System.out.println("🔍 Principal name in WS session: " + accessor.getUser().getName());
        } else {
            System.out.println("⚠ No principal in WS session");
        }
        return message;
    }
}
