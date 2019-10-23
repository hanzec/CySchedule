package edu.iastate.coms309.cyschedulebackend.controller;


import edu.iastate.coms309.cyschedulebackend.Service.EventService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Event;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.EventRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
@RequestMapping("/api/v1/event")
@Api(tags = "RestAPI Related to TimeBlock")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping(value= "/all")
    @ApiOperation("Get All TimeBlock")
    public Response getAllEventByUser(Principal principal, HttpServletRequest request){
        Response response = new Response();

        eventService.getAllEvent(principal.getName()).forEach(V->{
            response.addResponse(V.getBlockID().toString(),V);
        });

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @PostMapping(value= "/add")
    @ApiOperation("add new TimeBlock")
    public Response addNewEvent(Principal principal, HttpServletRequest request, EventRequest newEvent){
        Response response = new Response();

       if(newEvent.getName() == null)
           return response.BadRequested("No time block name").send(request.getRequestURI());
       if (newEvent.getEndTime() == null)
           return response.BadRequested("No ending time for time block").send(request.getRequestURI());
       if (newEvent.getStartTime() == null)
           return response.BadRequested("No starting time for time block").send(request.getRequestURI());
       if(newEvent.getUserID() == null)
           return response.BadRequested("There must be an admin user for this time block").send(request.getRequestURI());

       eventService.addEvent(newEvent);
       return response.Created().send(request.getRequestURI());
    }

    @ApiOperation("delete timeBlock by id")
    @DeleteMapping(value= "/{blockID}")
    public Response deleteTimeBlock(HttpServletRequest request, @PathVariable Integer blockID){
        Response response = new Response();
        eventService.deleteEvent(blockID.longValue());
        return response.noContent().send(request.getRequestURI());
    }

    @ApiOperation("load timeBlock by id")
    @GetMapping(value= "/{blockID}")
    public Response loadTimeBlock(HttpServletRequest request, @PathVariable Integer blockID){
        Response response = new Response();
        response.addResponse("TimeBlock", eventService.getEvent(blockID.longValue()));

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @ApiOperation("update timeBlock by id")
    @PutMapping(value= "/{blockID}")
    public Response updateTimeBlock(HttpServletRequest request, @PathVariable Integer blockID, EventRequest newEvent){
        Response response = new Response();
        Event event = eventService.getEvent(blockID.longValue());

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
