package edu.iastate.coms309.springbootexperiment.persistence.dao;

import edu.iastate.coms309.springbootexperiment.persistence.model.User;

public interface UserDAO {

    Boolean addUser(User user);

    User findByEmail(String email);
}
