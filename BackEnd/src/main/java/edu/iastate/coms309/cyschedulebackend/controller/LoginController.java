package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.RegisterRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(value = "/login")
    @ApiOperation("Login API")
    public Response login(HttpServletRequest request, LoginRequest loginRequest){
        Response response = new Response();

        if(!accountService.existsByEmail(loginRequest.getEmail()))
            return response.BadRequested("User is not existe").send(request.getRequestURI());

        if(accountService.checkPassword(loginRequest.getEmail(),loginRequest.getPassword()))
            response.OK().addResponse("loginToken",userTokenService.creat(loginRequest.getEmail()));
        else
            response.Forbidden();
        return response.send(request.getRequestURI());
    }

    @PostMapping(value = "/register")
    @ApiOperation("Used for register new account")
    public Response register(HttpServletRequest request, RegisterRequest user){
        Response response = new Response();

        //There should not register with same email address
        if(accountService.existsByEmail(user.getEmail()))
            return response.BadRequested("Username is Already Used").send(request.getRequestURI());


        //Trying to Register new Account to Server
        accountService.createUser(user);

        return response.send(request.getRequestURI()).Created();
    }
}
