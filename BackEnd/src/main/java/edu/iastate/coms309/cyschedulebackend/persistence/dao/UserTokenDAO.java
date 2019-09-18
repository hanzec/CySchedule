package edu.iastate.coms309.cyschedulebackend.persistence.dao;

public interface UserTokenDAO {

    String genUserToken(String userID);

    void deleteUserToken(String userID,String token);

}
