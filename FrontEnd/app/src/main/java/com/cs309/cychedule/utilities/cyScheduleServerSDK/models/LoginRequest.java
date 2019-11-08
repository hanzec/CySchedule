package com.cs309.cychedule.utilities.cyScheduleServerSDK.models;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank
    public String password;

    @NotBlank
    public String email;
}
