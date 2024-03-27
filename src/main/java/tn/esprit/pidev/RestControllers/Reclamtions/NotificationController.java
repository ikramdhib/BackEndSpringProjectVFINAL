package tn.esprit.pidev.RestControllers.Reclamtions;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;
import tn.esprit.pidev.RestControllers.AuthController.Param.ResponseModel;
import tn.esprit.pidev.Services.ReclamationService.WebSocketServices.NotificationService;

import java.security.Principal;
@RestController
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;

  /*  @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseModel getMessage(final ResponseModel notificationDAO) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseModel();
    }*/


    @MessageMapping("/private-message")
    @SendTo("/topic/private-messages")
    public ResponseModel getPrivateMessage(final ResponseModel notificationDAO, final Principal principal) throws
            InterruptedException {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseModel();
    }
}
