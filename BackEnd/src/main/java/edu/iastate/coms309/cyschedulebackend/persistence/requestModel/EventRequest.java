package edu.iastate.coms309.cyschedulebackend.persistence.requestModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
@Data
public class EventRequest {

    @NotBlank(message = "Event should contains a name")
    public String name;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss zzz yyyy")
    @NotNull(message = "Event should contains a endTime")
    public Date endTime;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "EEE MMM dd HH:mm:ss zzz yyyy")
    @NotNull(message = "Event should contains a startTime")
    public Date startTime;

    @NotBlank(message = "Event should contains a Location")
    public String location;

    @NotBlank(message = "Event should contains a description")
    public String description;
}
