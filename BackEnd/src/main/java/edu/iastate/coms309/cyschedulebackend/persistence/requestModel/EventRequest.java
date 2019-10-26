package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class EventRequest {

    @NotBlank(message = "Event should contains a name")
    public String name;

    @NotBlank(message = "Event should contains a endTime")
    public String endTime;

    @NotBlank(message = "Event should contains a startTime")
    public String startTime;

    @NotBlank(message = "Event should contains a Location")
    public String location;

    @NotBlank(message = "Event should contains a description")
    public String description;

    public String userID;
}
