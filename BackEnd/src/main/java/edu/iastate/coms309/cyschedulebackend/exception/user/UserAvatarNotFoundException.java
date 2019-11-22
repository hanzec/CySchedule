package edu.iastate.coms309.cyschedulebackend.exception.user;

public class UserAvatarNotFoundException extends Exception {
    public UserAvatarNotFoundException(String id){ super("Avatar not found for user :" + id);}
}
