package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;
import java.sql.Time;

@Data
public class EventRequest {
    public String name;

    public String endTime;

    public String startTime;

    public String location;

    public String description;

    public Long userID;
}
