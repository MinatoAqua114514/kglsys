package com.lin.kglsys.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 启用WebSocket消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个STOMP端点，客户端将使用它来连接。
        // "/ws" 是连接的端点URL。
        // withSockJS() 是为不支持WebSocket的浏览器提供备用选项。
        registry.addEndpoint("/api/v1/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 配置一个简单的内存消息代理。
        // 客户端订阅以 "/topic" 或 "/queue" 开头的目的地时，消息会路由到代理。
        // 我们主要使用 "/queue" 进行点对点消息（用户特定）。
        registry.enableSimpleBroker("/topic", "/queue");

        // 定义了发送到服务器的目标前缀。
        // 例如，客户端发送消息到 "/app/hello"，它将被路由到带有@MessageMapping("/hello")注解的方法。
        registry.setApplicationDestinationPrefixes("/app");

        // 这是为点对点消息（例如，发送给特定用户）设置的前缀。
        // 当我们使用 SimpMessagingTemplate.convertAndSendToUser() 或指定 /user/{userId}/... 时，
        // Spring会自动处理，确保消息只发送给该用户。
        registry.setUserDestinationPrefix("/user");
    }
}