package tn.esprit.pidev.RestControllers.Reclamtions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.RestControllers.AuthController.Param.ResponseModel;
import tn.esprit.pidev.Services.ReclamationService.WebSocketServices.WebSocketService;

@RestController
@AllArgsConstructor
@Slf4j
//@RequestMapping("/api/websocket")
public class WebSocketController {
    private final WebSocketService webSocketService;
    @PostMapping("/send-message")
    public void sendMessage() {
        webSocketService.notifyFrontend("ikram");
    }

  /*  @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id, @RequestBody final
    ResponseModel notificationDAO) {
        webSocketService.notifyUser(id,notificationDAO.getResponse());
    }*/
}
