package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "RestAPI Related to Authentication")
public class UserController{

    @Autowired
    AccountService accountService;

    @Autowired
    FileManagementService fileManagementService;

    @GetMapping(value = "/avatar")
    @ApiOperation("get user avatar ")
    public Response getAvatar(Principal principal, HttpServletRequest request){
        Response response = new Response();
        UserInformation userInformation =  accountService.getUserInformation(principal.getName());

        if(userInformation.getAvatar() == null)
            return response.NotFound().send(request.getRequestURI());
        else{
            response.addResponse("FileName",userInformation.getAvatar().getFileName());
            response.addResponse("FileDownloadLink",fileManagementService.getFile(userInformation.getAvatar()));

            return response.OK().send(request.getRequestURI());
        }
    }

    @PostMapping(value = "/avatar")
    @ApiOperation("Update User avatar")
    public Response updateAvatar(@RequestParam("file") MultipartFile file, Principal principal, HttpServletRequest request) {
        Response response = new Response();
        UserInformation userInformation =  accountService.getUserInformation(principal.getName());

        if(userInformation.getAvatar() != null)
            fileManagementService.deleteFile(userInformation.getAvatar());

        try {
            userInformation.setAvatar(fileManagementService.putFile(file, "avatar"));
        } catch (IOException e) {
            return response.BadRequested("file upload failed").send(request.getRequestURI());
        }

        accountService.updateUserInformation(userInformation);

        return response.Created().send(request.getRequestURI());
    }


}
