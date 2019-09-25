package edu.iastate.coms309.cyschedulebackend.controller;

import javax.servlet.http.HttpServletRequest;

import edu.iastate.coms309.cyschedulebackend.Service.TimeBlockService;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.TimeBlockDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.UserDAO;
import edu.iastate.coms309.cyschedulebackend.Service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TimeBlockController {
    TimeBlockDAO timeBlockDAO = new TimeBlockService();
}
