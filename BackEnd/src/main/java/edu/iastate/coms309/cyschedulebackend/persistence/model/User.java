package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;

import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements Serializable{

    /*
        Maybe a improve point
            - Only managedEvent relation are checked and 100% correct

    */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Email
    @NaturalId
    private String email;

    private String jwtKey;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;


    @OneToMany
    @JoinColumn(name = "user_token")
    private Set<UserToken> userTokens;

    @JoinTable(name = "user_to_user_role_table")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Permission> permissions;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Event> joinedEvent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adminUser")
    private Set<Event> managedEvent;
}
