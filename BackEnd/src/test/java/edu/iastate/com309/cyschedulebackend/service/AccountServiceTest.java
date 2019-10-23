package edu.iastate.com309.cyschedulebackend.service;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserDetailsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;


import static reactor.core.publisher.Mono.when;

public class AccountServiceTest {
    @Mock
    UserDetailsRepository userDetailsRepository;

    @InjectMocks
    AccountService accountService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewUserWithExistEmail(){
    }
}
