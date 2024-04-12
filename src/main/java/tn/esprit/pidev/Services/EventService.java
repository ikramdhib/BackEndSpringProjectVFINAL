package tn.esprit.pidev.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.EventRepo;
import tn.esprit.pidev.entities.Event;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepo eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }
}