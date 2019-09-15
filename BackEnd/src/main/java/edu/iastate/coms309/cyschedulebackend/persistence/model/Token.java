package edu.iastate.coms309.cyschedulebackend.persistence.model;

public class Token {
    String token;
    
    String owner;
    
    long issueTime;
    
    long expireTime;

    public Token(long expireTime){
        this.expireTime = expireTime;
        this.issueTime = System.currentTimeMillis() / 1000L;
    }
    
}
