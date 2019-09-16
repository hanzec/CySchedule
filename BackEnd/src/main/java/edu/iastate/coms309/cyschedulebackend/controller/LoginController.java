package edu.iastate.coms309.cyschedulebackend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAO;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAOImpl;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    UserDAO userDAO = new UserDAOImpl();

    @RequestMapping("/api/v1/register")
    public ResponseEntity<?> register(HttpServletRequest request){

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");

        //Trying to Register new Account to Server
        if (userDAO.checkEmail(email)){
            userDAO.createUser(password,firstname,lastname,email,username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else
            return new ResponseEntity<>("Email alereay Used",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/api/v1/getSalt")
    public ResponseEntity<Map<String,String>> getSalt(HttpServletRequest request){

        String email = request.getParameter("email");

        if(userDAO.checkEmail(email))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else{
            Map<String,String> response = new HashMap<String,String>();
            response.put("userID",userDAO.gerUserID(email));
            response.put("userSalt",userDAO.getUserSalt(email));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

}
