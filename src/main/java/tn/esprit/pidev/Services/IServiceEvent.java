package tn.esprit.pidev.Services;

import tn.esprit.pidev.entities.Event;

import java.util.List;

public interface IServiceEvent {
    Event addEvent(Event event , String userId);
    List<Event> getAllEvents(String userId);


}