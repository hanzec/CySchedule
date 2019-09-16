package edu.iastate.coms309.cyschedulebackend.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.swing.*;
import java.io.Serializable;

@Entity
@Table(name ="user_token")
public class UserToken implements Serializable{

    @Id
    @Column(name = "token_id",unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String tokenID;

    @OneToOne
    @JoinColumn(name="user_id")
    private User owner;

    private String token;
    
    private long issueTime;
    
    private long expireTime;

    private String refreshPassword;

    public UserToken(){ this.issueTime = System.currentTimeMillis() / 1000L; }

    public String getToken(){return this.token;}

    public User getOwner(){return this.owner;}

    public long getIssueTime(){return this.issueTime;}

    public long getExpireTime(){return this.expireTime;}

    public String getRefreshPassword(){return this.refreshPassword;}

    public void setToken(String token){this.token = token;}

    public void setOwner(User owner){this.owner = owner;}

    public void setIssueTime(long issueTime){this.issueTime = issueTime; }

    public void setExpireTime(long expireTime){this.expireTime = expireTime;}

    public void setRefreshPassword(String password){this.refreshPassword = password;}

}
