package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import java.util.List;

public interface UserDAO {

    User findByUserID(String UUID);

    User findByEmail(String email);

    void deleteUser(String userID);

    String gerUserID(String email);

    String getUserSalt(String email);

    Boolean checkEmail(String email);

    String updateEmail(String oldEmail,String newEmail);

    String createUser(String password, String firstName, String lastName, String email, String username);

}
