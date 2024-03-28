package tn.esprit.pidev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Enregistrement d'un point de terminaison WebSocket que les clients utiliseront pour se connecter à votre serveur WebSocket.
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configuration d'un simple broker de messages en mémoire qui conservera les messages destinés aux topics commençant par "/topic".
        registry.enableSimpleBroker("/topic");
        // Préfixe pour les destinations auxquelles les clients enverront les messages.
        registry.setApplicationDestinationPrefixes("/app");
    }
}