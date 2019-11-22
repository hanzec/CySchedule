package edu.iastate.coms309.cyschedulebackend.Websocket;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URL;
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

import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
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
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class WebsocketTest {
	
	private final String url = "ws://localhost:8080/websocket";
	
	@Mock
	Session session;
	
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void testonOpen() {
		Session serb = mock(Session.class);
		System.out.println("test start");
		
		Session alpha = mock(Session.class);
		String s = "test0";
		String a = "test1";
		
		
		
		WebsocketDemo ws = mock(WebsocketDemo.class);
		try {
			//session = container.connectToServer(ws, new URL(url));
			ws.onOpen(serb);
			ws.onOpen(alpha);
			verify(ws).onOpen(serb);
			verify(ws).onOpen(alpha);
			System.out.println("test1 finish");
		}catch(IOException e) {
			e.printStackTrace();
			
		}
		
	}
	@Test
	public void testMessage() {
Session serb = mock(Session.class);
		
		Session alpha = mock(Session.class);
		String s = "test0";
		String a = "test1";
		
		WebsocketDemo ws = mock(WebsocketDemo.class);
		try {
			ws.onOpen(serb);
			ws.onOpen(alpha);
			verify(ws).onOpen(serb);
			verify(ws).onOpen(alpha);
			ws.onMessage(a + "|hello",serb);
			verify(ws).onMessage(a + "|hello",serb);
		}catch(IOException e) {
			
		}
		
	}

}
