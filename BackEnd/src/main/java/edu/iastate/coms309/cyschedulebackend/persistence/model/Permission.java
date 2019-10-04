package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_permission")
public class Permission {
    @Id
    @Column(name = "permission_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer permissionID;

    private String permissionName;

    private String permissionDescription;

    @Override
    public String toString(){return permissionName;}
}
