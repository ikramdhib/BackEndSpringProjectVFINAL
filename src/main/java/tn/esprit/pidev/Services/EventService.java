package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.Repositories.EventRepo;
import tn.esprit.pidev.entities.Event;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService implements IServiceEvent{
    @Autowired
    private EventRepo eventRepository;


    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}