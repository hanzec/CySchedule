package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.exception.auth.EmailAlreadyExistException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;


@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "RestAPI Related to Authentication")
public class LoginController {
    /*
    Maybe a improve point
        - /getChallenge api may leak user information whatever user exist or not should generate same information

    */

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/login")
    @ApiOperation("Login API")
    public Response login(HttpServletRequest request, @Validated LoginRequest loginRequest) throws PasswordNotMatchException {
        UserCredential userCredential = (UserCredential) accountService.loadUserByUsername(loginRequest.getEmail());

        if(!accountService.checkPassword(loginRequest.getEmail(),loginRequest.getPassword()))
            throw new PasswordNotMatchException(loginRequest.getEmail());

        logger.debug("User [ " + loginRequest.getEmail() + " ] is permit to login");
        return new Response()
                .OK()
                .addResponse("loginToken",userTokenService.creat(userCredential))
                .send(request.getRequestURI());
    }

    @PostMapping(value = "/register")
    @ApiOperation("Used for register new account")
    public Response register(HttpServletRequest request,@Validated RegisterRequest user){

        //Trying to Register new Account to Server
        accountService.createUser(user);

        logger.debug("User [ " + user.getEmail() + " ] is success registered");

        return new Response().send(request.getRequestURI()).Created();
    }
}
