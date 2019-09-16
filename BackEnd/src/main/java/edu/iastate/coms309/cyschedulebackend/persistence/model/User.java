package edu.iastate.coms309.cyschedulebackend.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class User implements Serializable {

    @Id
    @Column(name = "user_id",unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    private String salt;

    private String email;

    private boolean enable;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserRole> authorities = new HashSet<>();

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
