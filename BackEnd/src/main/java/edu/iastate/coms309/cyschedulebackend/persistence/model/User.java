package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "user_account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "user_id")
})
public class User implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String userID ;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    private String jwtKey;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    private boolean enableStatus;

    private boolean lockedStatus;

    private boolean expireStatus;

    private boolean firstTimeUser;

    @Column(name = "role")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_account")
    private List<UserRole> authorities = new ArrayList<>();
}
