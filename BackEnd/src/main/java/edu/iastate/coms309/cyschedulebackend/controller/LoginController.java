package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAO;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    UserTokenService userTokenService;

    @RequestMapping("/login")
    public Response login(HttpServletRequest request){
        Response response = new Response();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = accountService.loadUserByEmail(username);
        if(user.getPassword().equals(password))
            response.OK().addResponse("LoginToken",userTokenService.genUserToken(user.getUserID()));
        else
            response.Forbidden();
        return response.send(request.getRequestURI());
    }

    @RequestMapping("/register")
    public Response register(HttpServletRequest request){
        Response response = new Response();

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastName");
        String firstname = request.getParameter("firstName");


        System.out.println("New user registered " + email + username + password + lastname + firstname);

        if(email.isEmpty() || username.isEmpty() || password.isEmpty()||lastname.isEmpty()||firstname.isEmpty())
            return response.send(request.getRequestURI()).BadRequested("Information is not enough");

        //Trying to Register new Account to Server
        if (!accountService.userExists(email)){
            accountService.createUser(password,firstname,lastname,email,username);
            return response.send(request.getRequestURI()).Created();
       }else
            return response.BadRequested("Email address already existed").send(request.getRequestURI());
    }

    @RequestMapping("/getSalt")
    public Response getSalt(HttpServletRequest request){
        Response response = new Response();

        String email = request.getParameter("email");

        if(accountService.userExists(email))
        {
            response.addResponse("userID", accountService.getUserID(email));
            response.addResponse("userSalt", accountService.getUserSalt(email));
            return response.OK().send(request.getRequestURI());
        } else
            return response.NotFound().send(request.getRequestURI());
    }

}
