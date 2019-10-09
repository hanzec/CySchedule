package edu.iastate.coms309.cyschedulebackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebPageController {

    @RequestMapping("/test")
    string test(String request){
        return request;
    }

    @RequestMapping("/testGet")
    String testGet(){
        return "I am the cySchedule server!";
    }
    @RequestMapping("/teapot")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    void teapot(){}
}
