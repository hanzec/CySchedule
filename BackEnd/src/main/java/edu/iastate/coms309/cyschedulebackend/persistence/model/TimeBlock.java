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

    public Long adminUser;

    public TimeBlock(){};

    public TimeBlock(String name, Long adminUser, Time startTime, Time endTime){
        this.name = name;
        this.adminUser = adminUser;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
