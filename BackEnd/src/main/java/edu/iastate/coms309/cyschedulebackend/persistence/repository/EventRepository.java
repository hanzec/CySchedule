package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event,String> {

//    @Query("SELECT * FROM event e WHERE e. ", nativeQuery = true)
//    public List<Event> getAllByAdminUser_UserID(String userId);
//
//    public boolean
}
