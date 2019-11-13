package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
@Data
public class EventRequest {

    @NotBlank(message = "Event should contains a name")
    public String name;

    @NotNull(message = "Event should contains a endTime")
    public Date endTime;

    @NotNull(message = "Event should contains a startTime")
    public Date startTime;

    @NotBlank(message = "Event should contains a Location")
    public String location;

    @NotBlank(message = "Event should contains a description")
    public String description;
}
