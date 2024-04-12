package tn.esprit.pidev.Services.ReclamationService.WebSocketServices;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.RestControllers.AuthController.Param.ResponseModel;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService implements INotificationService{

    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void sendGlobalNotification() {
        log.info("eeeeeeeeeeeeeeeeeeeoooooo");
        ResponseModel message = new ResponseModel();
        message.setResponse("global notif");
        messagingTemplate.convertAndSend("/topic/messages", message.getResponse());
    }

    @Override
    public void sendPrivateNotification(String userId) {
        ResponseModel message = new ResponseModel();
        message.setResponse("global notif");
     messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", message);     }

}
