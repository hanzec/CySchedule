package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
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
    public Long addTimeBlock(TimeBlock timeBlock, Long userId){

        User user = userDetailsRepository.findByUserID(userId);

        timeBlock.adminUser = user;

        timeBlockRepository.saveAndFlush(timeBlock);

        user.getManagedTimeBlock().add(timeBlock);

        user.getJoinedTimeBlock().add(timeBlock);

        userDetailsRepository.saveAndFlush(user);

        return timeBlock.blockID;
    }

    @Transactional
    public void deleteTimeBlock(Long id){ timeBlockRepository.deleteById(id); }

    @Transactional
    public TimeBlock getTimeBlock(Long id){
        return timeBlockRepository.getOne(id);
    }

    @Transactional
    public Set<TimeBlock> getAllTimeBlock(String admin){ return userDetailsRepository.findByEmail(admin).getJoinedTimeBlock(); }

}
