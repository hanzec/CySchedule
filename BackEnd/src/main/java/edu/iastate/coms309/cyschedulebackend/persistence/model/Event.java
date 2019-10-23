package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long blockID;

    public String name;

    public String endTime;

    public String startTime;

    public String location;

    public String description;

    @ManyToOne
    @JoinColumn(name = "user_time_block")
    public User adminUser;

    @ManyToMany
    public List<User> relatedUser;
}
