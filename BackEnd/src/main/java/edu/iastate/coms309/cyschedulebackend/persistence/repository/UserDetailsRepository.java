package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import java.util.List;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

    User findByUserID(Long userID);

    User findByEmail(String eamil);

    Boolean existsByEmail(String email);
}
