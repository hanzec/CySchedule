package edu.iastate.coms309.cyschedulebackend.Websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.server.standard.SpringConfigurator;


@ServerEndpoint(value="/websocketDemo/{userId}",configurator = SpringConfigurator.class)
public class WebsocketDemo {
    
    private Logger logger = LoggerFactory.getLogger(WebsocketDemo.class);
    
    private static int onlineCount = 0;

   
    private static Map<Long, Set<WebsocketDemo>> userSocket = new HashMap<>();

    
    private Session session;
    private String userId;

  
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session) throws IOException{
        this.session = session;
        this.userId = userId;
        onlineCount++;
        
        if (userSocket.containsKey(this.userId)) {
            logger.debug("User:{} login with mutiple device",this.userId);
            userSocket.get(this.userId).add(this); //增加该用户set中的连接实例
        }else {
            logger.debug("User:{}login with first device",this.userId);
            Set<WebsocketDemo> addUserSet = new HashSet<>();
            addUserSet.add(this);
            userSocket.put(Long.valueOf(this.userId), addUserSet);
        }
        logger.debug("User{}login with {} device",userId,userSocket.get(this.userId).size());
        logger.debug("current User Online{},Total user{}",userSocket.size(),onlineCount);
        
    }

    
    @OnClose
    public void onClose(){
        
        if (userSocket.get(this.userId).size() == 0) {
            userSocket.remove(this.userId);
        }else{
            userSocket.get(this.userId).remove(this);
        }
        logger.debug("User{}login with {} device",this.userId,userSocket.get(this.userId).size());
        logger.debug("current User Online{},Total user{}",userSocket.size(),onlineCount);
    }

    
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("receive message from User {}: {}",this.userId,message);
        if(session ==null)  logger.debug("session null");
    }

    
    @OnError
    public void onError(Session session, Throwable error){
        logger.debug("User {} message send error",this.userId);
        error.printStackTrace();
    }

    
    public Boolean sendMessageToUser(String userId,String message){
        if (userSocket.containsKey(userId)) {
            logger.debug("send message to User {}: {}",userId,message);
            for (WebsocketDemo WS : userSocket.get(userId)) {
                logger.debug("sessionId is:{}",WS.session.getId());
                try {
                    WS.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.debug("User {} message send error",userId);
                    return false;
                }
            }
            return true;
        }
        logger.debug("User {} not exist",userId);
        return false;
    }

}