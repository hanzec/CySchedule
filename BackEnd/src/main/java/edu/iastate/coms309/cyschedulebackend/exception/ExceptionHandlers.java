package edu.iastate.coms309.cyschedulebackend.exception;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.exception.auth.EmailAlreadyExistException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.PasswordNotMatchException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.TokenAlreadyExpireException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.TokenVerifyFaildException;
import edu.iastate.coms309.cyschedulebackend.exception.event.EventNotFoundException;
import edu.iastate.coms309.cyschedulebackend.exception.event.NotPrimitiveException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlers {

    @Autowired
    Gson gson;

    @ExceptionHandler({
            TokenVerifyFaildException.class,
            EmailAlreadyExistException.class,
            TokenAlreadyExpireException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void GenericBadRequestException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendMessage(ex,request,response);
    }

    @ExceptionHandler({
            EventNotFoundException.class,
            UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void GenericNotFoundException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendMessage(ex,request,response);
    }

    @ExceptionHandler({
            NotPrimitiveException.class,
            PasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void GenericForbiddenException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendMessage(ex,request,response);
    }

    private void sendMessage(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
        List<ObjectError> errors =c.getBindingResult().getAllErrors();

        List<String> errorList = new ArrayList<>();

        errors.forEach(V -> { errorList.add(V.getDefaultMessage());});

        response.getWriter().write(
                gson.toJson(
                        new Response()
                                .BadRequested(ex.getMessage())
                                .send(request.getRequestURI())
                                .addResponse("Error Item",errorList)));
    }
}
