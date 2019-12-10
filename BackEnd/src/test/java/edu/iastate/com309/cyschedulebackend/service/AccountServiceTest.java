package edu.iastate.com309.cyschedulebackend.service;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.exception.auth.EmailAlreadyExistException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserCredentialRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserInformationRepository;

import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.RegisterRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.geom.RectangularShape;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AccountServiceTest {

    private UserCredential userCredential;

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

        userCredential = new UserCredential();
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
        Mockito.when(userCredentialRepository.getOne(anyString())).thenReturn(userCredential);

        accountService.resetPassword("123","123","123");
    }
}

