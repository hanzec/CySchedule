package edu.iastate.coms309.cyschedulebackend.controller;

import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.exception.io.FileUploadFailedException;
import edu.iastate.coms309.cyschedulebackend.exception.user.UserAvatarNotFoundException;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.*;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.ChangePasswordRequest;
import edu.iastate.coms309.cyschedulebackend.persistence.requestModel.LoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
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
@Api(tags = "RestAPI Related user Account")
public class UserController{

    @Autowired
    AccountService accountService;

    @Autowired
    UserTokenService userTokenService;

    @Autowired
    FileManagementService fileManagementService;

    @GetMapping(value = "/")
    @ApiOperation("Get user information")
    public Response getUserInformation(Principal principal, HttpServletRequest request){
        UserInformation userInformation = accountService.getUserInformation(principal.getName());

        return new Response()
                .OK()
                .addResponse("userName",userInformation.getUsername())
                .addResponse("lastName",userInformation.getLastName())
                .addResponse("firstName",userInformation.getFirstName())
                .send(request.getRequestURI());
    }

    @PostMapping(value = "/password")
    @ApiOperation("Reset password aip")
    public Response resetPassword(
            Principal principal,
            HttpServletRequest request,
            @Validated ChangePasswordRequest changePasswordRequest) throws PasswordNotMatchException {

        accountService.resetPassword(
                principal.getName(),
                changePasswordRequest.getNewPassword(),
                changePasswordRequest.getOldPassword());

        return new Response()
                .OK()
                .send(request.getRequestURI());
    }

//    @GetMapping(value = "/avatar")
//    @ApiOperation("get user avatar ")
//    public Response getAvatar(Principal principal, HttpServletRequest request) throws UserAvatarNotFoundException {
//
//        FileObject avatar = accountService.getAvatar(principal.getName());
//
//        return new Response()
//                .OK()
//                .addResponse("FileName",avatar.getFileName())
//                .addResponse("FileDownloadLink",fileManagementService.getFile(avatar))
//                .send(request.getRequestURI());
//    }
//
//    @PostMapping(value = "/avatar")
//    @ApiOperation("Update User avatar")
//    public Response updateAvatar(@RequestParam("file") MultipartFile file, Principal principal, HttpServletRequest request) throws FileUploadFailedException {
//
//        //ignore exception if there is not avatar before
//        try {
//            fileManagementService.deleteFile(accountService.getAvatar(principal.getName()));
//        } catch (UserAvatarNotFoundException ignored) {}
//
//        accountService.updateAvatar(principal.getName(),fileManagementService.putFile(file, "avatar"));
//
//        return new Response()
//                .Created()
//                .send(request.getRequestURI());
//    }

    @GetMapping(value = "/token")
    @ApiOperation("get All existed token")
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
