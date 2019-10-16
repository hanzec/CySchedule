package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.TimeBlockRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Set;

@Service
public class TimeBlockService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimeBlockRepository timeBlockRepository;

    @Transactional
    public Long addTimeBlock(TimeBlock timeBlock){

        User user = userRepository.findByUserID(timeBlock.adminUser);

        timeBlockRepository.saveAndFlush(timeBlock);

        user.getUserTimeBlock().add(timeBlock);

        userRepository.saveAndFlush(user);

        return timeBlock.blockID;
    }

    @Transactional
    public Set<TimeBlock> getAllTimeBlock(String admin){
        return userRepository.findByEmail(admin).getUserTimeBlock();
    }

    @Transactional
    public TimeBlock getTimeBlockByID(Long id){
        return timeBlockRepository.getOne(id);
    }


}
