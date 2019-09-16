package edu.iastate.coms309.cyschedulebackend.persistence.dao;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;

public interface TokenDAO {

    public UserToken getToken(String Token);

    public UserToken refreshToken(String token);

    public UserToken generateAuthToken(String userID);

    public UserToken generateAccessToken(String userID, TokenType type);

    public boolean isValid(String token, String userID);

    public enum TokenType{
        ACCESS_TOKEN, AUTH_TOKEN
    }
}
