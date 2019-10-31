package com.cs309.cychedule.models;

import com.google.gson.annotations.Expose;
import lombok.Data;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
public class UserToken {

    String tokenID;

    String secret;

    Time expireTime;

    String refreshKey;

    private Set<Permission> permissions;
}
