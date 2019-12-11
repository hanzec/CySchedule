package edu.iastate.coms309.cyschedulebackend.Websocket;


import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserInformation;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
//import org.springframework.web.socket.server.standard.SpringConfigurator;
//import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;


@ServerEndpoint(value="/websocket/{email}")
public class WebsocketDemo {
    
    private Logger logger = LoggerFactory.getLogger(WebsocketDemo.class);
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Session> TestSet = new ConcurrentHashMap<>();
    private static int onlineCount = 0;
    private Session session;
    private String userId;
    private UserInformation user;
    private static AccountService accountService;
    private Set<Event> r;
    
    public static void setAccountService(AccountService as) {
    	WebsocketDemo.accountService = as;
    }
    
    @OnOpen
    public void onOpen(Session session,@PathParam("email") String email) throws IOException{
        this.session = session;
        
        userId = email;
        webSocketSet.put(userId, session);
//        try {
//        	session.getBasicRemote().sendText(userId);
//        	webSocketSet.forEach((K,V) ->{
//				
//					
//					try {
//						session.getBasicRemote().sendText(K);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				
//			});
//        	
//        }catch (IOException e) {
//        	e.printStackTrace();
//        }
        
        
//        if(session.getUserPrincipal()== null) {
//        	this.user = null;
//        	this.userId = "test"+String.valueOf(onlineCount);
//        	try {
//    			session.getBasicRemote().sendText("hello " + userId);
//    			//logger.debug("{} message send",userId);
//    			//r = new HashSet();
//                //r = user.getManagedEvent();
//    			webSocketSet.put(userId, this);
//    			TestSet.put(userId,session );
//    			session.getBasicRemote().sendText(""+TestSet.size());
//    			TestSet.forEach((K,V) ->{
//    				try {
//						session.getBasicRemote().sendText(K);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    			});
//    			this.sendToUser("test0|hello", session);
//    			
//    		} catch (IOException e) {
//    			e.printStackTrace();
//                //logger.debug("User {} message send error",userId);
//    		}
//        }
//        else {
//        	this.user = accountService.getUserInformation(session.getUserPrincipal().getName());
//            this.userId = session.getUserPrincipal().getName();
//            session.getBasicRemote().sendText("hello " + userId);
            logger.debug("new connection import");
            
            
//        }
        onlineCount++;
      
        //logger.debug("current User Online{},Total user{}",userSocket.size(),onlineCount);
        
    }


    
    @OnClose
    public void onClose(){
        
      /*if (userSocket.get(this.userId).size() == 0) {
            userSocket.remove(this.userId);
        }else{
            userSocket.get(this.userId).remove(this);
        }
        logger.debug("User{}login with {} device",this.userId,userSocket.get(this.userId).size());
        logger.debug("current User Online{},Total user{}",userSocket.size(),onlineCount);
        */
    	webSocketSet.remove(userId);
    	logger.debug("close connection from {}",this.userId);
    	
    }

    
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("receive message from User {}: {}",this.userId,message);
        this.sendToUser(message,session);
    }
    //for test use only now
    public void sendToUser(String message,Session session) {
        String sendUserno = message.split("\\|")[0];
        String sendMessage = message.split("\\|")[1];
        
        // if(accountService.existsByEmail(sendUserno)) {
        	
//        	try {
            	//session.getBasicRemote().sendText(sendUserno);
            	webSocketSet.forEach((K,V) ->{
    				if(K.equals(sendUserno)) {
    					
    					try {
    						V.getBasicRemote().sendText("ms|"+this.userId+"|"+sendMessage);
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				}
    			});
                
//                if (webSocketSet.contains(sendUserno)) {
//                	webSocketSet.get(sendUserno).getBasicRemote().sendText("ms|"+this.userId+"|"+sendMessage);
//                    //System.out.println("ms|"+this.userId+"|"+sendMessage);
//                } else {
//                    this.session.getBasicRemote().sendText("Target user is current offline now, try again later");
//                    //System.out.println("Target user is current offline now, try again later");
//                    
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        	
//         }
//         else {
//         	try {
// 				this.session.getBasicRemote().sendText("Target email doesn't exist");
// 			} catch (IOException e) {
// 				// TODO Auto-generated catch block
// 				e.printStackTrace();
// 			}
//         }
        
        //String now = getNowTime();
        
    }


    
    @OnError
    public void onError(Session session, Throwable error){
        logger.debug("User {} message send error",this.userId);
        error.printStackTrace();
    }
    
    public void pushfromServertoUser(String email, String message) {
    	if(accountService.existsByEmail(email)) {
    		webSocketSet.forEach((K,V) ->{
				if(K.equals(email)) {
					
					try {
						V.getBasicRemote().sendText("ms|"+"serverpush"+"|"+message);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
    		
    	}
    	else {
    		logger.debug("error when authrization, email invalid");
    	}
    	
    }

    
/*    public Boolean sendMessageToUser(String userId,String message){
        if (userSocket.containsKey(userId)) {
            logger.debug("send message to User {}: {}",userId,"Hello world"+userId);
            for (WebsocketDemo WS : userSocket.get(userId)) {
                logger.debug("sessionId is:{}",WS.session.getId());
                try {
                    WS.session.getBasicRemote().sendText("Hello world "+userId);
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
    */
    public Event findcloseEvent(Set<Event> EL) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date date = new Date();
    	
    	Event r;
    	List<Event> list = null;
    	for(Event e : EL) {
    		
    		Date st = null;
    		try {
				st = sdf.parse(e.startTime.toString());
			} catch (ParseException e1) {
				
			}
    		
    		if(date.before(st)) {
    			list.add(e);
    		}
    	}
    	r= list.get(0);
    	Date CT = null;
    	try {
			CT = sdf.parse(r.startTime.toString());
		} catch (ParseException e1) {
		}
    	for(Event e:list) {
    		Date TT = null;
    		try {
				TT =sdf.parse(r.startTime.toString());
			} catch (ParseException e1) {
				
			}
    		if(TT.before(CT)) {
    			CT = TT;
    			r = e;
    		}
    				
    	}
    	
    	return r;
    }
    
    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    
    public ArrayList<Event> setEvent() {
    	ArrayList<Event> p = new ArrayList();
    	Event e1 = new Event();
    	e1.setName("asd");
    	e1.setDescription("test");
    	e1.setLocation("123123123");
    	e1.setStartTime(ZonedDateTime.now());
    	e1.setEndTime(ZonedDateTime.now());
    	e1.setEventID("95b6231f-efe3-4333-81de-0869d1312510");
    	//e1.setAdminUser(user);
    	p.add(e1);
    	Event e2 = new Event();
    	e2.setName("asd");
    	e2.setDescription("test");
    	e2.setLocation("123123123");
    	e2.setStartTime(ZonedDateTime.now());
    	e2.setEndTime(ZonedDateTime.now());
    	e2.setEventID("a2762515-7e61-4336-be74-89468c10b1b9");
    	//e2.setAdminUser(user);
    	p.add(e2);
    	Event e3 = new Event();
    	e3.setName("asd");
    	e3.setDescription("test");
    	e3.setLocation("123123123");
    	e3.setStartTime(ZonedDateTime.now());
    	e3.setEndTime(ZonedDateTime.now());
    	e3.setEventID("b2cd26d0-710c-49fd-ba8d-1e507411b183");
    	//e3.setAdminUser(user);
    	p.add(e3);
    	Event e4 = new Event();
    	e4.setName("asd");
    	e4.setDescription("test");
    	e4.setLocation("123123123");
    	e4.setStartTime(ZonedDateTime.now());
    	e4.setEndTime(ZonedDateTime.now());
    	e4.setEventID("598e7f33-9bc7-4f85-a59d-4584af78652a");
    	//e4.setAdminUser(user);
    	p.add(e4);
    	return p;
    }

}