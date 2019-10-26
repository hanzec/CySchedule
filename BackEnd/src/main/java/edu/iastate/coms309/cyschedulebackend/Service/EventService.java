package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.EventRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserDetailsRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class EventService {
    /*
    Maybe a improve point
            - Token never revoke
     */

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Transactional
    public Event updateEvent(EventRequest newEvent, Long eventID){
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

    @Transactional
    public Event addEvent(EventRequest newEvent){
        Event event = new Event();

        //set event object
        event.setName(newEvent.getName());
        event.setEndTime(newEvent.getEndTime());
        event.setLocation(newEvent.getLocation());
        event.setStartTime(newEvent.getStartTime());
        event.setDescription(newEvent.getDescription());
        event.setAdminUserDetails(userDetailsRepository.getOne(newEvent.getUserID().longValue()));

        //set relation with user
        event.getAdminUserDetails().getManagedEvent().add(event);

        //submit object
        eventRepository.save(event);
        userDetailsRepository.save(event.getAdminUserDetails());

        return event;
    }

    @Transactional
    public void deleteEvent(Long id){ eventRepository.deleteById(id); }

    @Transactional
    public EventRequest getEvent(Long id){
        Event event = eventRepository.getOne(id);
        EventRequest eventRequest = new EventRequest();


        eventRequest.setName(event.getName());
        eventRequest.setEndTime(event.getEndTime());
        eventRequest.setLocation(event.getLocation());
        eventRequest.setStartTime(event.getStartTime());
        eventRequest.setDescription(event.getDescription());

        return eventRequest;
    }

    @Transactional
    public Event getEventInstance(Long id){
        return eventRepository.getOne(id);
    }

    @Transactional
    public Set<Event> getAllEvent(String admin){ return userDetailsRepository.findByEmail(admin).getJoinedEvent(); }

}
