package edu.iastate.coms309.cyschedulebackend.Websocket;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel
public class WebsocketResponse {
	@Autowired
    Gson gson;

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
    private Map<String,Object> responseBody = new HashMap<>();

    public WebsocketResponse OK(){
        this.status = "200";
        success = Boolean.TRUE;
        return this;
    }

    public WebsocketResponse Created(){
        this.status = "201";
        success = Boolean.TRUE;
        return this;
    }

    public WebsocketResponse NotFound(){
        this.status = "404";
        success = Boolean.FALSE;

        return this;
    }

    public WebsocketResponse Forbidden(){
        this.status = "403";
        success = Boolean.FALSE;
        return this;
    }

    public WebsocketResponse noContent(){
        this.status = "204";
        success = Boolean.TRUE;

        return this;
    }

    public WebsocketResponse Unauthorized(){
        this.status = "401";
        success = Boolean.FALSE;

        return this;
    }

    public WebsocketResponse BadRequested(String msg){
        message = msg;
        this.status = "401";
        success = Boolean.FALSE;

        return this;
    }

    public WebsocketResponse send(String path){
        this.path = path;
        this.timestamp = new Timestamp(System.currentTimeMillis()).toString();
        return this;
    }

    public WebsocketResponse addResponse(String key, Object object){
        this.responseBody.put(key,object);
        return this;
    }

}
