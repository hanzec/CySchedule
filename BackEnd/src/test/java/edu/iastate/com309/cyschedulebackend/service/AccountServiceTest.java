package edu.iastate.com309.cyschedulebackend.service;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.UserInformationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class AccountServiceTest {
    @Mock
    UserInformationRepository userInformationRepository;

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
