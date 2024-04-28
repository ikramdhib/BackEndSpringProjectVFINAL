package tn.esprit.pidev.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private EventService eventService;

    @GetMapping("/GetAllEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @PostMapping("/AddEvent/{userId}")
    public ResponseEntity<Event> addEvent(@RequestBody Event event, @PathVariable String userId) {
        Event createdEvent = eventService.addEvent(event, userId);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

}