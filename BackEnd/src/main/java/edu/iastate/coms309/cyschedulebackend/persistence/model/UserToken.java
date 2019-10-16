package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Data;

@Data
public class UserToken{

    private String token;

    private Long userID;

    private String refreshKey;

    public UserToken(String token,Long userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = null;;
    }

    public UserToken(String token,String refreshKey,Long userID){
        this.token = token;
        this.userID = userID;
        this.refreshKey = refreshKey;
    }
}
