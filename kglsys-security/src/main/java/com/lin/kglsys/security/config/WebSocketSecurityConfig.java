package com.lin.kglsys.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

/**
 * Dedicated configuration for WebSocket (STOMP) message security.
 * This class uses the modern, component-based approach recommended for Spring Boot 3.
 */
@Configuration
public class WebSocketSecurityConfig {

    /**
     * This bean defines the authorization rules for all incoming STOMP messages.
     * Spring's autoconfiguration will automatically detect this bean and apply it to the
     * WebSocket message broker.
     *
     * @return A fully configured AuthorizationManager for STOMP messages.
     */
    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager() {
        MessageMatcherDelegatingAuthorizationManager.Builder messages =
                MessageMatcherDelegatingAuthorizationManager.builder();

        messages
                .simpSubscribeDestMatchers("/**").authenticated()
                .simpMessageDestMatchers("/app/**").authenticated()
                .anyMessage().authenticated();

        return messages.build();
    }
}