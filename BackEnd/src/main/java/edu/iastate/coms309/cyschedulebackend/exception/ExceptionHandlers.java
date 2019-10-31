package edu.iastate.coms309.cyschedulebackend.exception;

import com.google.gson.Gson;
import edu.iastate.coms309.cyschedulebackend.exception.auth.TokenAlreadyExpireException;
import edu.iastate.coms309.cyschedulebackend.exception.auth.TokenVerifyFaildException;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
            TokenAlreadyExpireException.class,
            MethodArgumentNotValidException.class
    })
    public void GenericApiException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
        List<ObjectError> errors =c.getBindingResult().getAllErrors();

        List<String> errorList = new ArrayList<>();

        errors.forEach(V -> { errorList.add(V.getDefaultMessage());});

        Response result = new Response();
        response.getWriter().write(
                gson.toJson(
                        result
                                .BadRequested(ex.getMessage())
                                .send(request.getRequestURI())
                                .addResponse("Error Item",errorList)));
    }
}
