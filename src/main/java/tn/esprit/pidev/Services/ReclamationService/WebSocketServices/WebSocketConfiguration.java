package tn.esprit.pidev.Services.ReclamationService.WebSocketServices;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //web sockets starts communication with /notification
        registry.enableSimpleBroker("/topic");
        //web socket prefix
        registry.setApplicationDestinationPrefixes("/ws");
    }

    //to use in front end
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //front end  starts with this url to communicate with web socket
        registry
                .addEndpoint("/websocket")
                .setAllowedOrigins("*");
        registry
                .addEndpoint("/websocket")
                .setAllowedOrigins("*")
               .withSockJS();
    }

}
