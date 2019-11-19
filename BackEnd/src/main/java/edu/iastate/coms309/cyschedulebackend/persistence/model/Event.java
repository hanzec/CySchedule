package edu.iastate.coms309.cyschedulebackend.persistence.model;


import com.google.gson.annotations.Expose;
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
    @Expose
    @Column(name = "event_id")
    @GeneratedValue(generator = "uuid2")
    public String eventID;

    @Expose
    public String name;

    @Column
    @Expose
    public ZonedDateTime endTime;

    @Column
    @Expose
    public ZonedDateTime startTime;

    @Expose
    public String location;

    @Expose
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
