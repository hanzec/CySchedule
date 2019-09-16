package edu.iastate.coms309.cyschedulebackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebPageController {

    @RequestMapping("/teapot")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    void teapot(){}
}
