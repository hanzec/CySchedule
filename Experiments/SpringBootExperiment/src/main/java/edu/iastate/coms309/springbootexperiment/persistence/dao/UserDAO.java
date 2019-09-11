package edu.iastate.coms309.springbootexperiment.persistence.dao;

import edu.iastate.coms309.springbootexperiment.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {

    Boolean addUser(User user);

    User findByEmail(String email);

    @Override
    void delete(User user);
}
