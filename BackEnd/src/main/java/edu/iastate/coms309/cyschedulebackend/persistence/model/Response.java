package edu.iastate.coms309.cyschedulebackend.persistence.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Response {

    String path;

    String status;

    String timestamp;


    private Map<String, Object> responseBody = new HashMap<>();

    private Map<String, Object> responseHeader = new HashMap<>();

    public Response OK(){
        this.status = "200";
        this.responseHeader.put("Success",Boolean.TRUE);

        return this;
    }

    public Response Created(){
        this.status = "201";
        this.responseHeader.put("Success",Boolean.TRUE);
        return this;
    }

    public Response NotFound(){
        this.status = "404";
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response Forbidden(){
        this.status = "403";
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response Unauthorized(){
        this.status = "401";
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response BadRequested(String msg){
        this.status = "401";
        this.responseBody.put("Message",msg);
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response send(String path){
        this.path = path;
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public Response addResponse(String key, Object value){
        this.responseBody.put(key, value);
        return this;
    }

    public Response addHeader(String key, String value){
        this.responseHeader.put(key, value);
        return this;
    }
}
