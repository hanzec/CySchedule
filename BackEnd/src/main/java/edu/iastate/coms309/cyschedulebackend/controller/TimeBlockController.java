package edu.iastate.coms309.cyschedulebackend.controller;


import edu.iastate.coms309.cyschedulebackend.Service.TimeBlockService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
@RequestMapping("/api/timeblock/v1")
@Api(tags = "RestAPI Related to TimeBlock")
public class TimeBlockController {


    @Autowired
    TimeBlockService timeBlockService;

    @ApiOperation("Get All TimeBlock")
    @RequestMapping(value= "/all", method= RequestMethod.GET)
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

    @ApiOperation("add new TimeBlock")
    @RequestMapping(value= "/add", method= RequestMethod.POST)
    public Response getAllTimeBlockByUser(Principal principal, HttpServletRequest request, TimeBlock newTimeBlock){
        Response response = new Response();

       if(newTimeBlock.name == null)
           return response.BadRequested("No time block name").send(request.getRequestURI());
       if (newTimeBlock.endTime == null)
           return response.BadRequested("No ending time for time block").send(request.getRequestURI());
       if (newTimeBlock.startTime == null)
           return response.BadRequested("No starting time for time block").send(request.getRequestURI());
       if(newTimeBlock.adminUser == null)
           return response.BadRequested("There must be an admin user for this time block").send(request.getRequestURI());

       return response.Created().send(request.getRequestURI());
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

    @ApiOperation("delete timeBlock by id")
    @DeleteMapping(value= "/{blockID}")
    public Response deleteTimeBlock(HttpServletRequest request, @PathVariable Long blockID){
        Response response = new Response();
        timeBlockService.deleteTimeBlock(blockID);
        return response.noContent().send(request.getRequestURI());
    }

    @ApiOperation("update timeBlock by id")
    @PutMapping(value= "/{blockID}")
    public Response updateTimeBlock(HttpServletRequest request, @PathVariable Long blockID, TimeBlock newTimeBlock){
        Response response = new Response();
        TimeBlock timeBlock = timeBlockService.getTimeBlock(blockID);

        if (newTimeBlock.name != null)
            timeBlock.name = newTimeBlock.name;

        if(newTimeBlock.endTime != null)
            timeBlock.endTime = newTimeBlock.endTime;

        if(newTimeBlock.startTime != null)
            timeBlock.startTime = newTimeBlock.startTime;

        if(newTimeBlock.description != null)
            timeBlock.description = newTimeBlock.description;

        if(response.getResponseBody().isEmpty())
            return response.NotFound().send(request.getRequestURI());
        else
            return response.OK().send(request.getRequestURI());
    }
}
