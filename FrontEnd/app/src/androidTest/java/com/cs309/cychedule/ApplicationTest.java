package com.cs309.cychedule;


import android.net.wifi.hotspot2.pps.Credential;

import com.cs309.cychedule.utilities.cyScheduleServerSDK.models.RegisterRequest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
 public class ApplicationTest {
    private Credential.UserCredential userCredential;

    private RegisterRequest registerRequest;

    @InjectMocks
    AccountService accountService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserCredentialRepository userCredentialRepository;

    @Mock
    UserInformationRepository userInformationRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initRegisterRequest(){
        registerRequest = new RegisterRequest();
        registerRequest.setPassword("password");
        registerRequest.setUsername("username");
        registerRequest.setLastName("lastName");
        registerRequest.setFirstName("firstName");
        registerRequest.setEmail("user@example.com");

        userCredential = new Credential.UserCredential();
        userCredential.setPassword("password");
        userCredential.setEmail("user@example.com");
    }

    @Test(expected = EmailAlreadyExistException.class)
    public void testAddNewUserWithExistEmail(){
        Mockito.when(userCredentialRepository.existsById(anyString())).thenReturn(true);

        accountService.createUser(registerRequest);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserWithNonExistEmail(){
        Mockito.when(userCredentialRepository.existsById(anyString())).thenReturn(false);

        accountService.loadUserByUsername(anyString());
    }

    @Test(expected = PasswordNotMatchException.class)
    public void testResetPasswordWithWrongOldPassword() throws PasswordNotMatchException {
        Mockito.when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);
        Mockito.when(userCredentialRepository.getByUserID(anyString())).thenReturn(userCredential);

        accountService.resetPassword("123","123","123");
    }

}
