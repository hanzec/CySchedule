package edu.iastate.coms309.cyschedulebackend.controller;


import edu.iastate.coms309.cyschedulebackend.Service.TimeBlockService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
@RequestMapping("/api/v1/timeblock")
@Api(tags = "RestAPI Related to TimeBlock")
public class TimeBlockController {


    @Autowired
    TimeBlockService timeBlockService;

    @GetMapping(value= "/all")
    @ApiOperation("Get All TimeBlock")
    public Response getAllTimeBlockByUser(Principal principal, HttpServletRequest request){
        Response response = new Response();

        timeBlockService.getAllTimeBlock(principal.getName()).forEach(V->{
            response.addResponse(V.blockID,V);
        });

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @GetMapping(value= "/add")
    @ApiOperation("add new TimeBlock")
    public Response getAllTimeBlockByUser(Principal principal, HttpServletRequest request, Event newEvent){
        Response response = new Response();

       if(newEvent.name == null)
           return response.BadRequested("No time block name").send(request.getRequestURI());
       if (newEvent.endTime == null)
           return response.BadRequested("No ending time for time block").send(request.getRequestURI());
       if (newEvent.startTime == null)
           return response.BadRequested("No starting time for time block").send(request.getRequestURI());
       if(newEvent.adminUser == null)
           return response.BadRequested("There must be an admin user for this time block").send(request.getRequestURI());

       return response.Created().send(request.getRequestURI());
    }

    @ApiOperation("delete timeBlock by id")
    @DeleteMapping(value= "/{blockID}")
    public Response deleteTimeBlock(HttpServletRequest request, @PathVariable Long blockID){
        Response response = new Response();
        timeBlockService.deleteTimeBlock(blockID);
        return response.noContent().send(request.getRequestURI());
    }

    @ApiOperation("load timeBlock by id")
    @GetMapping(value= "/{blockID}")
    public Response loadTimeBlock(HttpServletRequest request, @PathVariable Long blockID){
        Response response = new Response();
        response.addResponse("TimeBlock",timeBlockService.getTimeBlock(blockID));

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @ApiOperation("update timeBlock by id")
    @PutMapping(value= "/{blockID}")
    public Response updateTimeBlock(HttpServletRequest request, @PathVariable Long blockID, Event newEvent){
        Response response = new Response();
        Event event = timeBlockService.getTimeBlock(blockID);

        if (newEvent.name != null)
            event.name = newEvent.name;

        if(newEvent.endTime != null)
            event.endTime = newEvent.endTime;

        if(newEvent.startTime != null)
            event.startTime = newEvent.startTime;

        if(newEvent.description != null)
            event.description = newEvent.description;

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }
}
