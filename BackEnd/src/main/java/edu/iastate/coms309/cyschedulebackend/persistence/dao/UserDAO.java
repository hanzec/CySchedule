package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import java.util.List;

public interface UserDAO {

    User findByUserID(String UUID);

    List findByEmail(String email);

    void deleteUser(String userID);

    Boolean checKEmail(String email);

    String createUser(String password, String firstName, String lastName, String email, String username);

}
