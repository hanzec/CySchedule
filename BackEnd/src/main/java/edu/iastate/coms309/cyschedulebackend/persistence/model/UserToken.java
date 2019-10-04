package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Data;

@Data
public class UserToken{

    private String token;

    private String userID;

    private String refreshKey;

    public UserToken(String token,String userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = null;;
    }

    public UserToken(String token,String refreshKey,String userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = refreshKey;
    }
}
