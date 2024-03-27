package tn.esprit.pidev.Services.ReclamationService.WebSocketServices;

public interface INotificationService {

    public void sendGlobalNotification();
    public void sendPrivateNotification(final String userId);
}
