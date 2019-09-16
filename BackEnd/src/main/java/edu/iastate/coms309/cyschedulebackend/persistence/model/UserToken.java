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

    //Permission Part

    private boolean isAllowRefresh = false;

    private boolean isAllowAccessAPIs = false;

    private boolean isAllowAuthentication = false;

    public UserToken(){ this.issueTime = System.currentTimeMillis() / 1000L; }

    public String getToken(){return this.token;}

    public User getOwner(){return this.owner;}

    public long getIssueTime(){return this.issueTime;}

    public long getExpireTime(){return this.expireTime;}

    public String getRefreshPassword(){return this.refreshPassword;}

    public void setToken(String token){this.token = token;}

    public void setOwner(User owner){this.owner = owner;}

    public boolean isAllowRefresh(){return isAllowRefresh;}

    public boolean isAllowAccessAPIs(){return isAllowAccessAPIs;}

    public boolean isAllowAuthentication(){return isAllowAuthentication;}

    public void  setAllowRefresh(){this.isAllowRefresh = true;}

    public void  setAllowAccessAPIs(){this.isAllowAccessAPIs = true;}

    public void setAllowAuthentication(){this.isAllowAuthentication = true;}

    public void setIssueTime(long issueTime){this.issueTime = issueTime; }

    public void setExpireTime(long expireTime){this.expireTime = expireTime;}

    public void setRefreshPassword(String password){this.refreshPassword = password;}

}
