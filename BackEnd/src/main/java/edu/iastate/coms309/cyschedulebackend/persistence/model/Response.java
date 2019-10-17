package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
public class Response {

    String path;

    String status;

    String message;

    Boolean success;

    String timestamp;

    private HashMap<Object,Object> responseBody = new HashMap<>();

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

    public Response addResponse(Object key,Object object){
        this.responseBody.put(key,object);
        return this;
    }
}
