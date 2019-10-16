package edu.iastate.coms309.cyschedulebackend.controller;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    Gson gson;

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    @RequestMapping("/login")
    public Response login(HttpServletRequest request){
        Response response = new Response();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("New user register" + username + "    " + password);
        User user = (User) accountService.loadUserByUsername(username);
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

        if(email == null || username == null || password == null||lastname == null||firstname == null)
            return response.send(request.getRequestURI()).BadRequested("Information is not enough");

        //Trying to Register new Account to Server
        if (!accountService.existsByEmail(email)){
            accountService.createUser(password,firstname,lastname,email,username);
            return response.send(request.getRequestURI()).Created();
       }else
            return response.BadRequested("Username is Already Used").send(request.getRequestURI()).Created();
    }

    @RequestMapping("/getSalt")
    public Response getSalt(HttpServletRequest request){
        Response response = new Response();

        String email = request.getParameter("email");

        if(accountService.existsByEmail(email))
        {
            response.addResponse("userID", accountService.getUserID(email));
            response.addResponse("userSalt", accountService.getUserSalt(email));
            return response.OK().send(request.getRequestURI());
        } else
            return response.NotFound().send(request.getRequestURI());
    }

}
