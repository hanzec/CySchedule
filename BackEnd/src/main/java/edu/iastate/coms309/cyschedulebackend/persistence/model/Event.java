package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(generator = "uuid2")
    public String eventID;

    public String name;

    @Column
    public ZonedDateTime endTime;

    @Column
    public ZonedDateTime startTime;

    public String location;

    public String description;

    @Column(
            name = "user_id",
            updatable = false,
            insertable = false
    )
    private String adminUserID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserInformation adminUser;

    @ManyToMany(mappedBy = "joinedEvent")
    private List<UserInformation> relatedUser;

}
