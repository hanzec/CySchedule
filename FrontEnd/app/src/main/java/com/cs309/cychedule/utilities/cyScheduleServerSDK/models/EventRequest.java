package com.cs309.cychedule.utilities.cyScheduleServerSDK.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class EventRequest {
    
    public String name;
    
    public String endTime;
    
    public String startTime;
    
    public String location;

    public String description;

    public String userID;
}
