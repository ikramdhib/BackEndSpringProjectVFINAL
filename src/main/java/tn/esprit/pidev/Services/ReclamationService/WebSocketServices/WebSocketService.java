package tn.esprit.pidev.Services.ReclamationService.WebSocketServices;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import tn.esprit.pidev.RestControllers.AuthController.Param.ResponseModel;

@Service
@AllArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public void notifyFrontend(final String message) {
        ResponseModel responseNotification = new ResponseModel();
        responseNotification.setResponse(message);
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("topic/messages",responseNotification);
    }


    public void notifyUser(final String id, final String message) {
        ResponseModel responseNotification = new ResponseModel();
        responseNotification.setResponse(message);
        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id,"topic/private-messages",responseNotification);
    }
}
