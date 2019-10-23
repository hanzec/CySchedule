package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
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
    @JoinColumn(name = "user_id")
    public User adminUser;

}
