package edu.iastate.coms309.cyschedulebackend.persistence.dao;


import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

public interface UserDAO{

    void deleteUser(String userID);

    String gerUserID(String email);

    boolean userExists(String email);

    String getUserSalt(String email);

    User loadUserByEmail(String email);

    User loadUserByUserID(String userId);

    String getPasswordByEmail(String email);

    void changePassword(String userID, String password);

    String updateEmail(String oldEmail,String newEmail);

    String createUser(String password, String firstName, String lastName, String email, String username);
}
