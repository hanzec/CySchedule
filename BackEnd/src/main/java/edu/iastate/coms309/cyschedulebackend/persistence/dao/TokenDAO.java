package edu.iastate.coms309.cyschedulebackend.persistence.dao;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;

public interface TokenDAO {

    public UserToken generateToken(String userID);

    public boolean isExpired(String token, String userID);

    public UserToken getToken(String token, String userID);

    public UserToken refreshToken(String token, String userID);

    public void grantTokenPermission(String userID, String token, TokenRight tokenRight);

    public enum TokenRight{
        Refresh, AccessAPIs,Authentication
    }
}
