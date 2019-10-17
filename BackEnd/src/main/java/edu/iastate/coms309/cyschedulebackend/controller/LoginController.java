package edu.iastate.coms309.cyschedulebackend.controller;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "RestAPI Related to Authentication")
public class LoginController {

    @Autowired
    Gson gson;

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    @ApiOperation("Login API")
    @RequestMapping(value = "/login", method= RequestMethod.POST)
    public Response login(HttpServletRequest request){
        Response response = new Response();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("New user register" + username + "    " + password);
        User user = (User) accountService.loadUserByUsername(username);
        if(user.getPassword().equals(password))
            response.OK().addResponse(userTokenService.genUserToken(user.getUserID()));
        else
            response.Forbidden();
        return response.send(request.getRequestURI());
    }

    @ApiOperation("Used for register new account")
    @RequestMapping(value = "/register", method= RequestMethod.POST)
    public Response register(HttpServletRequest request){
        Response response = new Response();

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastName");
        String firstname = request.getParameter("firstName");

        //New Register will give all required field
        if(email == null || username == null || password == null||lastname == null||firstname == null)
            return response.send(request.getRequestURI()).BadRequested("Information is not enough");

        //There should not register with same email address
        if(!accountService.existsByEmail(email))
            return response.BadRequested("Username is Already Used").send(request.getRequestURI()).Created();


        //Trying to Register new Account to Server
        accountService.createUser(password,firstname,lastname,email,username);

        return response.send(request.getRequestURI()).Created();
    }

    @RequestMapping("/getChallenge")
    @ApiOperation("Used for get salt for specific user")
    public Response getChallenge(HttpServletRequest request){

        Response response = new Response();

        String email = request.getParameter("email");

        if(accountService.existsByEmail(email)) {
            response.addResponse("userId", accountService.getUserID(email));
            response.addResponse("userSalt", accountService.getUserSalt(email));
            response.addResponse("currentLoginChallenge",accountService.getChallengeKeys(email));

            return response.OK().send(request.getRequestURI());
        } else
            return response.NotFound().send(request.getRequestURI());
    }
}
