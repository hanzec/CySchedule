package edu.iastate.coms309.cyschedulebackend.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;


@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    PasswordUtil passwordUtil;

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    @RequestMapping("/login")
    public Response login(HttpServletRequest request){
        Response response = new Response();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");

        if(email == null || username == null || password == null||lastname == null||firstname == null)
            return response.send(request.getRequestURI()).BadRequested("Information is not enough");

        //Trying to Register new Account to Server
        //Check email is correct
        if (accountService.existsByEmail(email))
            return response.BadRequested("Email address already existed").send(request.getRequestURI());

        Long userID = accountService.createUser(password,firstname,lastname,email,username);

        return response.send(request.getRequestURI()).addHeader("Userid",userID.toString()).Created();
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
