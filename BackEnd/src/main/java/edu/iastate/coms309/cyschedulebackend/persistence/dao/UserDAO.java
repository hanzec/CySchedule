package edu.iastate.coms309.cyschedulebackend.persistence.dao;


import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserRole;

public interface UserDAO{

    void deleteUser(String userID);

    String gerUserID(String username);

    boolean userExists(String email);

    String getUserSalt(String email);

    String getJwtKey(String userID);

    User loadUserByEmail(String email);

    User loadUserByUserID(String userId);

    String getPasswordByEmail(String email);

    void setUserRole(String userID, UserRole role);

    void changePassword(String userID, String password);

    String updateEmail(String oldEmail,String newEmail);

    String createUser(String password, String firstName, String lastName, String email, String username);
}
