package edu.iastate.coms309.cyschedulebackend.persistence.model;

import com.google.gson.annotations.Expose;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
@Data
@ApiModel
public class Response {

    @Expose
    String path;

    @Expose
    String status;

    @Expose
    String message;

    @Expose
    Boolean success;

    @Expose
    String timestamp;

    @Expose
    private HashMap<String,Object> responseBody = new HashMap<>();

    public Response OK(){
        this.status = "200";
        success = Boolean.TRUE;
        return this;
    }

    public Response Created(){
        this.status = "201";
        success = Boolean.TRUE;
        return this;
    }

    public Response NotFound(){
        this.status = "404";
        success = Boolean.FALSE;

        return this;
    }

    public Response Forbidden(){
        this.status = "403";
        success = Boolean.FALSE;
        return this;
    }

    public Response noContent(){
        this.status = "204";
        success = Boolean.TRUE;

        return this;
    }

    public Response Unauthorized(){
        this.status = "401";
        success = Boolean.FALSE;

        return this;
    }

    public Response BadRequested(String msg){
        message = msg;
        this.status = "401";
        success = Boolean.FALSE;

        return this;
    }

    public Response send(String path){
        this.path = path;
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public Response addResponse(String key,Object object){
        this.responseBody.put(key,object);
        return this;
    }
}
