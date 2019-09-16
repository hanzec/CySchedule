package edu.iastate.coms309.cyschedulebackend.persistence.model;

import ch.qos.logback.classic.db.names.ColumnName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "user_id")
})
public class User implements Serializable{

    @Id
    @Column(name = "user_id", nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    private boolean enable;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    @Column(name = "role")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_account")
    private List<UserRole> authorities = new ArrayList<>();

    @Column(name = "owned_Token" )
    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY,mappedBy = "user_account")
    private List<UserToken> userTokens = new ArrayList<>();

    public String getSalt(){return this.salt;}

    public String getUserID(){return this.uuid;}

    public void setSalt(String salt){this.salt = salt;}

    public boolean getEnableStatus(){return this.enable;}

    public void setEmail(String email){this.email = email;}

    public void setUserID(String userID){this.uuid = userID;}

    public void setLastName(String lastName){this.lastName = lastName;}

    public void setPassword(String password){this.password = password;}

    public void setUsername(String username){this.username = username;}

    public void setFirstName(String firstName){this.firstName = firstName;}

    public void setRegisterTime(Long registerTime){this.registerTime = registerTime;}
}
