package edu.iastate.com309.cyschedulebackend.service;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.PermissionService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

public class UserTokenServiceTest {
    @Mock
    AccountService accountService;

    @Mock
    PermissionService permissionService;

    @InjectMocks
    UserTokenService userTokenService;

    @Before
    public void initAuthority(){
    }
}
