package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserInformation;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.EventRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserCredentialRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserInformationRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Lazy
@Service
public class EventService {
    /*
    Maybe a improve point
            - Token never revoke
     */

    @Autowired
    EventRepository eventRepository;

    @Transactional
    public Event updateEvent(EventRequest newEvent, String eventID){
        Event event = eventRepository.getOne(eventID);

        //set event object
        event.setName(newEvent.getName());
        event.setEndTime(newEvent.getEndTime());
        event.setLocation(newEvent.getLocation());
        event.setStartTime(newEvent.getStartTime());
        event.setDescription(newEvent.getDescription());

        eventRepository.save(event);
        return event;
    }

    @Async
    @Transactional
    public void addEvent(EventRequest newEvent, UserInformation userInformation){
        Event event = new Event();

        //set event object
        event.setName(newEvent.getName());
        event.setAdminUser(userInformation);
        event.setEndTime(newEvent.getEndTime());
        event.setLocation(newEvent.getLocation());
        event.setStartTime(newEvent.getStartTime());
        event.setDescription(newEvent.getDescription());

        //set relation with user
        event.getAdminUser().getManagedEvent().add(event);

        eventRepository.save(event);
    }

    @Transactional
    public EventRequest getEvent(String id){
        Event event = eventRepository.getOne(id);
        EventRequest eventRequest = new EventRequest();


        eventRequest.setName(event.getName());
        eventRequest.setEndTime(event. getEndTime());
        eventRequest.setLocation(event.getLocation());
        eventRequest.setStartTime(event.getStartTime());
        eventRequest.setDescription(event.getDescription());

        return eventRequest;
    }

    @Async
    @Transactional
    public void deleteEvent(String id){ eventRepository.deleteById(id); }

    @Transactional
    @Cacheable(
            value = "false",
            unless = "#result == true"
    )
    public boolean existByID(String eventID){return eventRepository.existsById(eventID);}

    @Transactional
    @Cacheable(
            value = "false",
            key = "#userID + #eventID + '_has_permission'"
    )
    public boolean checkOwnerShip(String eventID, String userID){
        return eventRepository.getOne(eventID).getAdminUser().getUserID().equals(userID);
    }

//    @Transactional
//    public Set<Event> getAllEvent(String userID){ return eventRepository.getAllByAdminUser_UserID(userID); }userID

}
