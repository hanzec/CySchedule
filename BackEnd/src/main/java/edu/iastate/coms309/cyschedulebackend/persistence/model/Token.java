package edu.iastate.coms309.cyschedulebackend.persistence.model;

import javax.swing.*;
import java.io.Serializable;

public class Token{
    private String token;
    
    private String owner;
    
    private long issueTime;
    
    private long expireTime;

    private String refreshPassword;

    public Token(){ this.issueTime = System.currentTimeMillis() / 1000L; }

    public String getToken(){return this.token;}

    public String getOwner(){return this.owner;}

    public long getIssueTime(){return this.issueTime;}

    public long getExpireTime(){return this.expireTime;}

    public String getRefreshPassword(){return this.refreshPassword;}

    public void setToken(String token){this.token = token;}

    public void setOwner(String owner){this.owner = owner;}

    public void setIssueTime(long issueTime){this.issueTime = issueTime; }

    public void setExpireTime(long expireTime){this.expireTime = expireTime;}

    public void setRefreshPassword(String password){this.refreshPassword = password;}

}
