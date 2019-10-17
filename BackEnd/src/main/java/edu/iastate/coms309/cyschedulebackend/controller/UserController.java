package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user/v1")
@Api(tags = "RestAPI Related to Authentication")
public class UserController{

    @ApiOperation("get user avatar ")
    @RequestMapping(value = "/avatar", method= RequestMethod.GET)
    public void getAvatar(HttpServletRequest request, HttpServletResponse response){
    }
}
