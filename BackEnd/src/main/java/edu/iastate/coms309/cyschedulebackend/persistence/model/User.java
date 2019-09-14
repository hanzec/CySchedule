package edu.iastate.coms309.cyschedulebackend.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.iastate.coms309.cyschedulebackend.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "user_account")
public class User implements Serializable {

    @Id
    @Column(name = "user_id",unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    private String salt;

    private String email;

    private String lastName;

    private String password;

    private boolean enabled;

    private String firstName;

    private Long registerTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserRole> authorities = new HashSet<>();

    public String getSalt(){return this.salt;}

    public String getUserID(){return this.uuid;}

    public void enablAccount(){this.enabled = true;}

    public void setSalt(String salt){this.salt = salt;}

    public void setEmail(String email){this.email = email;}

    public void setUserID(String userID){this.uuid = userID;}

    public void setLastName(String lastName){this.lastName = lastName;}

    public void setPassword(String password){this.password = password;}

    public void setFirstName(String firstName){this.firstName = firstName;}

    public void setRegisterTime(Long registerTime){this.registerTime = registerTime;}
}
