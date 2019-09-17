package edu.iastate.coms309.cyschedulebackend.controller;


import edu.iastate.coms309.cyschedulebackend.Service.UserService;
import edu.iastate.coms309.cyschedulebackend.Utils.PasswordUtil;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RestApiController {
    @Autowired
    UserService userService;

    @Autowired
    PasswordUtil passwordUtil;

    @RequestMapping("/api/v1/register")
    public ResponseEntity<?> register(HttpServletRequest request){

        //retire information from HTTP request
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");

        //Trying to Register new Account to Server
        if (userService.userExists(email)){
            userService.createUser(password,firstname,lastname,email,username);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else
            return new ResponseEntity<>("Email alereay Used",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/api/v1/token")
    public ResponseEntity<?> getUserToken(HttpServletRequest request){
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String combinedPassword = userService.getPasswordByEmail(email) + String.valueOf(System.currentTimeMillis()/60000);
        if(password.equals(passwordUtil.generatePasswordPBKDF2(combinedPassword,userService.getUserSalt(email))))


    }

    @RequestMapping("/api/v1/{userid}/getSalt")
    public ResponseEntity<Map<String,String>> getSalt(HttpServletRequest request){

        String email = request.getParameter("email");

        if(userService.userExists(email))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else{
            Map<String,String> response = new HashMap<String,String>();
            response.put("userID",userService.gerUserID(email));
            response.put("userSalt",userService.getUserSalt(email));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }
}
