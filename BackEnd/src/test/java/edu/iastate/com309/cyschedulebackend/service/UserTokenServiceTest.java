package edu.iastate.com309.cyschedulebackend.service;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.PermissionService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.*;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserTokenServiceTest {

    @Mock
    AccountService accountService;

    @Mock
    PermissionService permissionService;

    @InjectMocks
    UserTokenService userTokenService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void initAuthority(){
        Set<Permission> emptyPermissionList = new HashSet<>();
        when(accountService.getPermissions(any(Long.class))).thenReturn(emptyPermissionList);
        when(accountService.getJwtKey(any(Long.class))).thenReturn("ad3171ec-716f-471c-9939-cb5e9121a79e");
    }

    @Test
    public void loadNullTokenString(){
        assertNull(userTokenService.load(null));
    }

    @Test
    public void loadTokenString(){
        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbklEIjoiOWVlOWUxNzYtN2MxOS00MTFjLWFiZmYtMzI4ODY0NjBlMGVkIiwiaXNzIjoiQ3lTY2hlZHVsZSIsInBlcm1pc3Npb24iOltdLCJleHAiOjE1NzE4NTUwMjksInVzZXJJRCI6MSwianRpIjoiOWVlOWUxNzYtN2MxOS00MTFjLWFiZmYtMzI4ODY0NjBlMGVkIn0.HIdx_Wrf-8IFrgf3SzpOFEUEeS_PUqDdF5krceyg4Kk";

        UserToken userToken = userTokenService.load(token);

        //make sure token is storage in userToken
        assertEquals(token,userToken.getToken());

        //make sure user id is 1
        assertEquals((long) 1,userToken.getUserID());

        //make sure tokenID is 9ee9e176-7c19-411c-abff-32886460e0ed
        assertEquals("9ee9e176-7c19-411c-abff-32886460e0ed",userToken.getTokenID());
    }

    @Test
    public void loadIncorrectToken(){
        final String token = "not correct string";

        UserToken userToken = userTokenService.load(token);

        //userToken should be null because incorrect string
        assertNull(userToken);
    }
}
