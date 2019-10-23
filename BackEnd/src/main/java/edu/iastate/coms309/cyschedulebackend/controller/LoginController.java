package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
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
    public Response login(HttpServletRequest request){
        Response response = new Response();
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null)
            return response.BadRequested("email should not empty").send(request.getRequestURI());
        if (password == null)
            return response.BadRequested("password should not empty").send(request.getRequestURI());

        if(accountService.existsByEmail(email))
            return response.BadRequested("User is not existe").send(request.getRequestURI());

        if(accountService.checkPassword(email,password))
            response.OK().addResponse("loginToken",userTokenService.creat(accountService.getUserID(email)));
        else
            response.Forbidden();
        return response.send(request.getRequestURI());
    }

    @PostMapping(value = "/register")
    @ApiOperation("Used for register new account")
    public Response register(HttpServletRequest request, LoginRequest user){
        Response response = new Response();

        //New Register will give all required field
        if(user.getEmail() == null || user.getUsername() == null || user.getPassword() == null||user.getLastName() == null||user.getFirstName() == null)
            return response.send(request.getRequestURI()).BadRequested("Information is not enough");

        //There should not register with same email address
        if(accountService.existsByEmail(user.getEmail()))
            return response.BadRequested("Username is Already Used").send(request.getRequestURI());


        //Trying to Register new Account to Server
        accountService.createUser(user);

        return response.send(request.getRequestURI()).Created();
    }
}
