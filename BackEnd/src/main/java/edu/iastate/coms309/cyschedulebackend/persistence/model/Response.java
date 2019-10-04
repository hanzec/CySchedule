package edu.iastate.coms309.cyschedulebackend.persistence.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Response {

    private Map<String, Object> responseBody = new HashMap<>();

    private Map<String, Object> responseHeader = new HashMap<>();

    public Response OK(){
        this.responseHeader.put("StatusCode", 200);
        this.responseHeader.put("Success",Boolean.TRUE);

        return this;
    }

    public Response Created(){
        this.responseHeader.put("StatusCode", 201);
        this.responseHeader.put("Success",Boolean.TRUE);
        return this;
    }

    public Response NotFound(){
        this.responseHeader.put("StatusCode",404);
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response Forbidden(){
        this.responseHeader.put("StatusCode",403);
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response Unauthorized(){
        this.responseHeader.put("StatusCode",401);
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response BadRequested(String msg){
        this.responseBody.put("Message",msg);
        this.responseHeader.put("StatusCode",401);
        this.responseHeader.put("Success",Boolean.FALSE);

        return this;
    }

    public Response send(){
        this.responseHeader.put("TimeStamp", new Timestamp(System.currentTimeMillis()));
        return this;
    }

    public void addResponse(String key, Object value){ this.responseBody.put(key, value);}

    public void addHeader(String key, String value){ this.responseHeader.put(key, value);}
}
