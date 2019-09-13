package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

public interface UserDAO {

    Boolean addUser(User user);

    User findByEmail(String email);
}
