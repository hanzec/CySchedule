package edu.iastate.coms309.springbootexperiment.persistence.model;

public class Token {
    String token;
    
    String tokenID;
    
    long issueTime;
    
    long expireTime;

    public Token(long expireTime){
        this.expireTime = expireTime;
        this.issueTime = System.currentTimeMillis() / 1000L;
    }
    
}
