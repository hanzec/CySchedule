package edu.iastate.coms309.cyschedulebackend.persistence.model;

import java.util.Set;
import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

@Setter
@Getter
@Entity
@Table(name = "user_information")
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class UserInformation implements Serializable{

    /*
        Maybe a improve point
            - Only managedEvent relation are checked and 100% correct

    */

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "uuid2")
    private String UserID;


    private String username;

    private String lastName;

    private String firstName;

    private Long registerTime;

    @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "user_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "event_id"
            )
    )
    private Set<Event> joinedEvent;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "adminUser",
            targetEntity = Event.class
    )
    private Set<Event> managedEvent;

    @OneToOne(
            optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "userInformation"
    )
    private UserCredential userCredential;

    @JoinColumn(name = "user_jwt_token")
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = UserLoginToken.class
    )
    private Set<UserLoginToken> userLoginTokens;
}
