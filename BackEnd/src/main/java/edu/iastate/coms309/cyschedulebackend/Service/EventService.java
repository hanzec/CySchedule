package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.TimeBlockRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class TimeBlockService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    TimeBlockRepository timeBlockRepository;

    @Transactional
    public Long addTimeBlock(Event event, Long userId){

        User user = userDetailsRepository.findByUserID(userId);

        event.adminUser = user;

        timeBlockRepository.saveAndFlush(event);

        user.getManagedEvent().add(event);

        user.getJoinedEvent().add(event);

        userDetailsRepository.saveAndFlush(user);

        return event.blockID;
    }

    @Transactional
    public void deleteTimeBlock(Long id){ timeBlockRepository.deleteById(id); }

    @Transactional
    public Event getTimeBlock(Long id){
        return timeBlockRepository.getOne(id);
    }

    @Transactional
    public Set<Event> getAllTimeBlock(String admin){ return userDetailsRepository.findByEmail(admin).getJoinedEvent(); }

}
