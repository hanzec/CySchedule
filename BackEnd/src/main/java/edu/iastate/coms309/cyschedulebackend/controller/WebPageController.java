package edu.iastate.coms309.cyschedulebackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class WebPageController {

    @RequestMapping("/test")
    String test(String request){
        return request;
    }

    @RequestMapping("/testGet")
    String testGet(){
        return "I am the cySchedule server!";
    }

    @RequestMapping("/teapot")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    void teapot(){}

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
