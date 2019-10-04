package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;

public interface UserTokenDAO {

    boolean verify(UserToken userToken);

    UserToken genUserToken(String userID);

    void deleteUserToken(String userID,String token);

}
