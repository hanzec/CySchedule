package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event,String> {
    @Query("From Event e WHERE e.adminUserID = ?1 ORDER BY e.startTimeUnix ASC")
    List<Event> getAdminEvent(String userId);

    @Query("FROM Event e INNER JOIN e.relatedUser user WHERE user.UserID = ?1 ORDER BY e.startTimeUnix ASC")
    List<Event> getAllEvent(String userID);
}

