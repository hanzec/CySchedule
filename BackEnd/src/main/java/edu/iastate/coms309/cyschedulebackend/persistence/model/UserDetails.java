package edu.iastate.coms309.cyschedulebackend.persistence.model;

import java.util.Set;
import javax.persistence.*;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@Entity
@Table(name = "user_details")
@GenericGenerator(name = "idGenerator", strategy = "uuid")
public class UserDetails implements Serializable{

    /*
        Maybe a improve point
            - Only managedEvent relation are checked and 100% correct

    */

    @Id
    @GeneratedValue(generator = "idGenerator")
    private long UserID;

    private String username;

    private String lastName;

    private String firstName;

    private Long registerTime;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Event> joinedEvent;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "adminUser"
    )
    private Set<Event> managedEvent;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy="user_credential"
    )
    private UserCredential userCredential;

    @JoinColumn(name = "user_jwt_token")
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Set<UserLoginToken> userLoginTokens;
}
