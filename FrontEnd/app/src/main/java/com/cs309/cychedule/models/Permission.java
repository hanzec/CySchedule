package com.cs309.cychedule.models;


import lombok.Data;

/**
 * This is the server permission model
 */
@Data
public class Permission{

    private Integer roleID;

    private String roleName;

    private String description;
}
