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

    @GetMapping("/GetEvent/{userId}")
    public List<Event> getAllOffresForUser(@PathVariable String userId) {
        return eventService.getAllEvents(userId);
    }

    @PostMapping("/addEvent/{userId}") // Ajoutez le chemin d'accès pour l'ID de l'utilisateur
    public ResponseEntity<Event> addEvent(@RequestBody Event event, @PathVariable String userId) {
        Event newEvent = eventService.addEvent(event, userId); // Appelez la méthode du service avec l'ID de l'utilisateur
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }




}