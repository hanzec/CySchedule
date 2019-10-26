package edu.iastate.coms309.cyschedulebackend.controller;


import edu.iastate.coms309.cyschedulebackend.Service.EventService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
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

    @GetMapping(value = "/all")
    @ApiOperation("Get All TimeBlock")
    public Response getAllEventByUser(Principal principal, HttpServletRequest request) {
        Response response = new Response();

        eventService.getAllEvent(principal.getName()).forEach(V -> {
            response.addResponse(V.getEventID(), V);
        });

        if (response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @PostMapping(value = "/add")
    @ApiOperation("add new TimeBlock")
    public Response addNewEvent(Principal principal, HttpServletRequest request, EventRequest newEvent) {
        Response response = new Response();
        newEvent.setUserID(principal.getName());
        eventService.addEvent(newEvent);
        return response.Created().send(request.getRequestURI());
    }

    @ApiOperation("delete timeBlock by id")
    @DeleteMapping(value = "/{eventID}")
    public Response deleteTimeBlock(Principal principal, HttpServletRequest request, @PathVariable String eventID) {
        Response response = new Response();

        // check ownership
        if (eventService.checkOwnerShip(eventID, principal.getName()))
            return response.Forbidden().send(request.getRequestURI()).Created();

        eventService.deleteEvent(eventID);
        return response.noContent().send(request.getRequestURI()).Created();
    }

    @ApiOperation("load timeBlock by id")
    @GetMapping(value = "/{eventID}")
    public Response loadTimeBlock(Principal principal, HttpServletRequest request, @PathVariable String eventID) {
        Response response = new Response();
        response.addResponse("TimeBlock", eventService.getEvent(eventID));

        if (response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }

    @ApiOperation("update timeBlock by id")
    @PutMapping(value = "/{eventID}")
    public Response updateTimeBlock(Principal principal, HttpServletRequest request, @PathVariable String eventID, EventRequest newEvent) {
        Response response = new Response();

        // check ownership
        if (!eventService.checkOwnerShip(eventID, principal.getName()))
            return response.Forbidden().send(request.getRequestURI()).Created();

        // check existence
        if (!eventService.existByID(eventID))
            return response.NotFound().send(request.getRequestURI());

        eventService.updateEvent(newEvent, eventID);
        return response.OK().send(request.getRequestURI());
    }
}
