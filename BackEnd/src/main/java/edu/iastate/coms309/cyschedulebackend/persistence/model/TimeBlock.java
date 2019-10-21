package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Set;

@Data
@Entity
public class TimeBlock {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long blockID;

    public String name;

    public Time endTime;

    public Time startTime;

    public String location;

    public String description;

    @OneToMany
    public User adminUser;

    @ManyToOne
    public Set<User> relatedUser;
}
