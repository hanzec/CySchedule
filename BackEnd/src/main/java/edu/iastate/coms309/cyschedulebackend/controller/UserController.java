package edu.iastate.coms309.cyschedulebackend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "RestAPI Related to Authentication")
public class UserController{

    @ApiOperation("get user avatar ")
    @RequestMapping(value = "/avatar", method= RequestMethod.GET)
    public void getAvatar(HttpServletRequest request, HttpServletResponse response){
    }
}
