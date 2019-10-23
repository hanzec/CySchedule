package edu.iastate.com309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@AutoConfigureTestDatabase
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest {

    @Mock
    TokenService tokenService;

    @Mock
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initUser(){
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("lastName");
        user.setFirstName("firstName");
        user.setUsername("userName");
    }

    @Test
    public void addNewUser(){
    }
}
