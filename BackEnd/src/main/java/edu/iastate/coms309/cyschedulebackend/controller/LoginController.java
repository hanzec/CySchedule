package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
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
    UserDAO userDAO = new AccountService();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordUtil passwordUtil;

    @RequestMapping("/login")
    public Response login(HttpServletRequest request){
        Response response = new Response();

        return response;
    }

    @RequestMapping("/register")
    public Response register(HttpServletRequest request){
        Response response = new Response();

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");

        //Trying to Register new Account to Server
        if (accountService.userExists(email)){
            accountService.createUser(password,firstname,lastname,email,username);
            return response.send().Created();
        }else
            return response.BadRequested("Email address already existed").send();
    }

    @RequestMapping("/getSalt")
    public Response getSalt(HttpServletRequest request){
        Response response = new Response();

        String email = request.getParameter("email");

        if(accountService.userExists(email))
        {
            response.addResponse("userID", accountService.gerUserID(email));
            response.addResponse("userSalt", accountService.getUserSalt(email));
            return response.OK().send();
        } else
            return response.NotFound().send();
    }

}
