package edu.iastate.coms309.cyschedulebackend.Websocket;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

import java.io.IOException;
import javax.websocket.Session;

public class WebsocketTest {

	private final String url = "ws://localhost:8080/websocket";

	@Mock
	Session session;


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

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
