package com.cs309.cychedule.models;


import lombok.Data;

@Data
public class Permission{

    private Integer roleID;

    private String roleName;

    private String description;
}
