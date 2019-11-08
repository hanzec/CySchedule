package com.cs309.cychedule.utilities.cyScheduleServerSDK.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ServerResponse {

    String path;

    String status;

    String message;

    Boolean success;

    String timestamp;

    private Map<String,Object> responseBody;

    public String getMessage(){return message;}

    public boolean isSuccess(){return success;}
}
