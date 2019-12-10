package edu.iastate.coms309.cyschedulebackend.persistence.model;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;
import java.time.ZonedDateTime;
import java.util.Set;

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
    public Long startTimeUnix;

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

    @OneToMany
    private Set<FileObject> uploadedFile;

    @ManyToMany
    @OrderBy("startTimeUnix ASC")
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(
                    name = "event_id",
                    referencedColumnName = "event_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "user_id"
            )
    )
    private List<UserInformation> relatedUser = new ArrayList<>();

}
