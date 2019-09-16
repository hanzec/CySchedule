package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Token;

public interface TokenDAO {

    public Token getToken(String Token);

    public Token refreshToken(String token);

    public Token generateAuthToken(String userID);

    public Token generateAccessToken(String userID);

    public boolean isValid(String token, String userID);
}
