package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "RestAPI Related to Authentication")
public class UserController{

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

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
            Map<String,String> map = new HashMap<>();
            map.put("FileName",userInformation.getAvatar().getFileName());
            map.put("FileDownloadLink",fileManagementService.getFile(userInformation.getAvatar()));
            response.addResponse("avatar", map);
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

    @GetMapping(value = "/token")
    @ApiOperation("delete current login Token")
    public Response getAllToken(Principal principal, HttpServletRequest request){
        Response response = new Response();
        List<UserToken> tokens = userTokenService.getAllTokenBelongToUser(accountService.getUserEmail(principal.getName()));

        tokens.forEach(V -> {
            response.getResponseBody().put(V.getTokenID(),V);
        });
        return response.OK().send(request.getRequestURI());
    }

    @DeleteMapping(value = "/token/{tokenID}")
    @ApiOperation("delete current login Token")
    public Response revokeToken(Principal principal, HttpServletRequest request,@PathVariable String tokenID){
        Response response = new Response();
        UserToken token = userTokenService.getTokenObject(tokenID);

        if(token.getUserEmail().equals(accountService.getUserEmail(principal.getName())))
            return response.OK().send(request.getRequestURI());
        else
            return response.Forbidden().send(request.getRequestURI());
    }

}
