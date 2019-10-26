package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    public String endTime;

    public String startTime;

    public String location;

    public String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserInformation adminUser;

    @ManyToMany(mappedBy = "joinedEvent")
    private List<UserInformation> relatedUser;

}
