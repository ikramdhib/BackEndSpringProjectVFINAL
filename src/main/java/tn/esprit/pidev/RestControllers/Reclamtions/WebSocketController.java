package tn.esprit.pidev.RestControllers.Reclamtions;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidev.entities.Notification;

@RestController
@AllArgsConstructor
@Slf4j
 public class WebSocketController {

    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public String send(final String message) throws Exception {
        return message;
    }
    @MessageMapping("/applications")
    public void sendNotificationToUser(Notification message) throws Exception {
        String destination = "/specific/user/" +message.getUserId();
        simpMessagingTemplate.convertAndSend(destination, message.getMessage());
    }
}
