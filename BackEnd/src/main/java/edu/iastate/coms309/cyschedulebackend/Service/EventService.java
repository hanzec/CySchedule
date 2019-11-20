package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.exception.event.EventNotFoundException;
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

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class EventService {
    /*
    Maybe a improve point
        - need to check ownership before get object
     */

    @Autowired
    EventRepository eventRepository;

    @Transactional
    public Event updateEvent(EventRequest newEvent, String eventID) throws ParseException {
        Event event = eventRepository.getOne(eventID);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E LLL dd HH:mm:ss z yyyy",Locale.US);

        //set event object
        event.setName(newEvent.getName());
        event.setEndTime(ZonedDateTime.parse(newEvent.getEndTime(),format));
        event.setLocation(newEvent.getLocation());
        event.setStartTime(ZonedDateTime.parse(newEvent.getEndTime(),format));
        event.setDescription(newEvent.getDescription());

        eventRepository.save(event);
        return event;
    }

    @Transactional
    public void addEvent(EventRequest newEvent, UserInformation userInformation) throws ParseException {
        Event event = new Event();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E LLL dd HH:mm:ss z yyyy",Locale.US);

        //set event object
        event.setName(newEvent.getName());
        event.setAdminUser(userInformation);
        event.setLocation(newEvent.getLocation());
        event.setStartTimeUnix(event.getStartTime().toEpochSecond());
        event.setEndTime(ZonedDateTime.parse(newEvent.getEndTime(),format));
        event.setStartTime(ZonedDateTime.parse(newEvent.getEndTime(),format));
        event.setDescription(newEvent.getDescription());

        //set relation with user
        event.getAdminUser().getManagedEvent().add(event);

        eventRepository.save(event);
    }

    @Transactional
    public EventRequest getEvent(String id) throws EventNotFoundException {
        Event event = eventRepository.getOne(id);
        EventRequest eventRequest = new EventRequest();

        try {
            eventRequest.setName(event.getName());
            eventRequest.setLocation(event.getLocation());
            eventRequest.setDescription(event.getDescription());
            eventRequest.setEndTime(event.getEndTime().toString());
            eventRequest.setStartTime(event.getStartTime().toString());
            event.setStartTimeUnix(event.getStartTime().toEpochSecond());
        }catch (EntityNotFoundException e){
            throw new EventNotFoundException(id);
        }

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


    @Transactional
    public List<Event> getAllManagedEvent(String userID){
        return eventRepository.getManagedEvent(userID);
    }

    @Transactional
    public List<Event> getAllEvent(String userID) {
        List<Event> events = new ArrayList<>();

        events.addAll(eventRepository.getJoinedEvent(userID));
        events.addAll(eventRepository.getManagedEvent(userID));
        return events;
    }

}
