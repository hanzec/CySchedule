package com.cs309.cychedule;

import android.content.Context;

import com.cs309.cychedule.activities.MainActivity;
import com.cs309.cychedule.activities.SessionManager;
import com.cs309.cychedule.utilities.UserUtil;
import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.EventRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.matchers.Null;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
// public class ApplicationTest extends ApplicationTestCase<Application> {
//     public ApplicationTest() {
//         super(Application.class);
//     }
// }
public class ApplicationTest {
	
	private static final String secret = "Secret";
	private static final String tokenID = "Token ID";
	private static final String refreshKey = "Refresh Key";
	
	@Mock
	Context context;
	
	@Mock
	SessionManager sessionManager;
	

	@Before
	public void initSessionManager(){
		MockitoAnnotations.initMocks(this);
		// sessionManager.createSession(secret, tokenID, refreshKey);
	}
	
	@Test(expected = NullPointerException.class)
	public void tokenTest() throws NullPointerException{
		assertNotNull(context);
		MainActivity mainAct = mock(MainActivity.class);
		when(mainAct.generateToken(anyString(),anyString(),anyString()))
				.thenAnswer(new Answer<String>() {
					public String answer(InvocationOnMock invocation) {
						System.out.println("Mockito: token maker with null input is testing!");
						return "TEST";
					} });
		when(mainAct.generateToken(anyString(), anyString(), anyString()))
				.thenThrow(NullPointerException.class);
		mainAct.generateToken("URL","","PWD");
	}
	

	@Test
	public void sessionSetterTest(){
		// Context context = mock(Context.class);
		sessionManager = new SessionManager("TEST_ID","TEST_NAME");
		SessionManager spy = spy(sessionManager);
		assertNotNull(spy);
		doCallRealMethod().when(spy).createSession(anyString(),anyString(),anyString());
		spy.initSession("tokenID22222","user11111");
		assertEquals("tokenID22222",spy.getTokenID());
		assertEquals("user11111",spy.getUserName());
	}
}
