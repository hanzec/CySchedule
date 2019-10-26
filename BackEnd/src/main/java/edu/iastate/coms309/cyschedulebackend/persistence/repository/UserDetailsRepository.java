package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByEmail(String eamil);

    boolean existsByEmail(String email);
}