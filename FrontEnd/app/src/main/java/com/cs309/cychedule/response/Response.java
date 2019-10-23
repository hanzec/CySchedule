package com.cs309.cychedule.response;


import java.util.HashMap;


public class Response {

    String path;

    String status;

    String message;

    Boolean success;

    String timestamp;

    private HashMap<String,Object> responseBody = new HashMap<>();


}
