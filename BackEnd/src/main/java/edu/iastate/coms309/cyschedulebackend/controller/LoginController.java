package edu.iastate.coms309.cyschedulebackend.controller;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAOImpl;

import java.util.LinkedHashMap;

@RestController
public class LoginController {
    UserDAO userDAO = new UserDAOImpl();

    @RequestMapping("/v1/register")
    public ResponseEntity<String> register(HttpServletRequest request){

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");

        //Trying to Register new Account to Server
        userDAO.checKEmail(email);
        String userid = userDAO.createUser(password,firstname,lastname,email,username);

        //construct response using gson
        LinkedHashMap<String,String> response = new LinkedHashMap<String, String>();
        response.put("userid",userid);
        response.put("username", username);

    }

}
