package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;


@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "RestAPI Related to Authentication")
public class AuthController {
    /*
    Maybe a improve point
    */

    final AccountService accountService;

    final UserTokenService userTokenService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthController(AccountService accountService, UserTokenService userTokenService) {
        this.accountService = accountService;
        this.userTokenService = userTokenService;
    }

    @PostMapping(value = "/login")
    @ApiOperation("Login API")
    public Response login(HttpServletRequest request, @Validated LoginRequest loginRequest) throws PasswordNotMatchException {
        UserCredential userCredential = (UserCredential) accountService.loadUserByUsername(loginRequest.getEmail());

        accountService.checkPassword(loginRequest.getEmail(),loginRequest.getPassword());

        logger.debug("User [ " + loginRequest.getEmail() + " ] is permit to login");
        return new Response()
                .OK()
                .addResponse("loginToken",userTokenService.newToken(userCredential))
                .send(request.getRequestURI());
    }

    @PostMapping(value = "/register")
    @ApiOperation("Used for register new account")
    public Response register(HttpServletRequest request,@Validated RegisterRequest user){

        //Trying to Register new Account to Server
        accountService.createUser(user);

        logger.debug("User [ " + user.getEmail() + " ] is success registered");
        return new Response()
                .Created()
                .send(request.getRequestURI());
    }

//    @PostMapping(value = "/refresh")
//    @ApiOperation("Used for refresh Token")
//    public Response register(HttpServletRequest request, @Validated TokenRequest tokenRequest){
//        UserToken userToken = userTokenService.getTokenObject(tokenRequest.getTokenID());
//
//        if(userToken.)
//        //Trying to Register new Account to Server
//        accountService.createUser(user);
//
//        logger.debug("User [ " + user.getEmail() + " ] is success registered");
//
//        return new Response().send(request.getRequestURI()).Created();
//    }
}
