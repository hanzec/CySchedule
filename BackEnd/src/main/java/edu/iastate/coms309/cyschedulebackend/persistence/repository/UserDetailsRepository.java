package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

    User findByUserID(Long userID);

    User findByEmail(String eamil);

    Boolean existsByEmail(String email);
}
