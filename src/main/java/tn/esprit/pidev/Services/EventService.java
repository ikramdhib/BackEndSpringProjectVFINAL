package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.EventRepo;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import tn.esprit.pidev.entities.Event;
import tn.esprit.pidev.entities.User;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService implements IServiceEvent{
    @Autowired
    private EventRepo eventRepository;
    @Autowired
    private  UserServiceImpl userService;


    @Override
    public Event addEvent(Event event , String userId) {
        // Récupérez l'utilisateur à partir de l'ID
        User user = userService.getUserById2(userId);
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID spécifié n'existe pas.");
        }

        // Associez l'utilisateur à l'événement
        event.setUser(user);

        // Enregistrez l'événement avec l'utilisateur associé
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();

    }
}