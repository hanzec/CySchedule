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

    @OneToOne
    private FileObject avatar;

    @ManyToMany(mappedBy = "joinedEvent")
    private Set<Event> joinedEvent;

    @OrderBy("start_time ASC")
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "adminUser",
            targetEntity = Event.class
    )
    private Set<Event> managedEvent;

    @OneToMany
    private Set<FileObject> uploadedFile;

    @OneToOne(
            optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "userInformation"
    )
    private UserCredential userCredential;
}
