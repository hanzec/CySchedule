package edu.iastate.coms309.cyschedulebackend.persistence.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel
public class Response {

    @Autowired
    Gson gson;

    @Expose
    String path;


    @Expose
    String message;

    @Expose
    Boolean success;

    @Expose
    String timestamp;

    @Expose
    private Map<String,Object> responseBody = new HashMap<>();

    public Response OK(){
        success = Boolean.TRUE;
        return this;
    }

    public Response Created(){
        success = Boolean.TRUE;
        return this;
    }

    public Response NotFound(){
        success = Boolean.FALSE;

        return this;
    }

    public Response Forbidden(){
        success = Boolean.FALSE;
        return this;
    }

    public Response noContent(){
        success = Boolean.TRUE;

        return this;
    }

    public Response Unauthorized(){
        success = Boolean.FALSE;

        return this;
    }

    public Response BadRequested(String msg){
        message = msg;
        success = Boolean.FALSE;

        return this;
    }

    public Response send(String path){
        this.path = path;
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public Response addResponse(String key, Object object){
        this.responseBody.put(key,object);
        return this;
    }
}
