package tn.esprit.pidev.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.Repositories.EventRepo;
import tn.esprit.pidev.Services.EventService;
import tn.esprit.pidev.entities.Event;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventRepo eventRepository;

    @GetMapping("/GetEvent")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping("/CreateEvent")
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("UpdateEvent/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setText(updatedEvent.getText());
        event.setStart_date(updatedEvent.getStart_date());
        event.setEnd_date(updatedEvent.getEnd_date());
        return eventRepository.save(event);
    }

    @DeleteMapping("DeleteEvent/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventRepository.deleteById(id);
    }
}