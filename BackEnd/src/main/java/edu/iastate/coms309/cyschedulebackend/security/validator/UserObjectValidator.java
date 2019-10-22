package edu.iastate.coms309.cyschedulebackend.security.validator;

import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserObjectValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return  User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

    }
}
