package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Value;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Token;

public class TokenDAOImpl implements TokenDAO {

    @Value("${account.security.tokenLength}")
    Integer tokenLength;

    @Override
    public Token getToken(String Token) {
        return null;
    }

    @Override
    public Token refreshToken(String token) {
        return null;
    }

    @Override
    public Token generateAuthToken(String userID) {

    }

    @Override
    public Token generateAccessToken(String userID) {
        return null;
    }

    @Override
    public boolean isValid(String token, String userID) {
        return false;
    }
}
