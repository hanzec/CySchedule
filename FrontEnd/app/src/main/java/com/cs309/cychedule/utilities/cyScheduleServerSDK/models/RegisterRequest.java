package com.cs309.cychedule.utilities.cyScheduleServerSDK.models;


import lombok.Data;

@Data
public class RegisterRequest {

    String email;

    String password;

    String lastName;

    String username;

    String firstName;
}
