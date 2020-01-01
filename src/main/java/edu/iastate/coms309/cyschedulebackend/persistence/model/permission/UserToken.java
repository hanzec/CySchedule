package edu.iastate.coms309.cyschedulebackend.persistence.model.permission;

import com.google.gson.annotations.Expose;
import edu.iastate.coms309.cyschedulebackend.persistence.model.permission.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;


@Data
public class UserToken {

    @Expose
    String secret;

    @Expose
    String userID;

    @Expose
    String tokenID;

    @Expose
    String deviceID;

    @Expose
    TokenType tokenType;

    @Expose
    private Set<Permission> permissions;

    public enum TokenType{
        access_token, refresh_Token
    }
}
