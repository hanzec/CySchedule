package edu.iastate.coms309.cyschedulebackend.persistence.model.permission;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission")
public class Permission implements GrantedAuthority {
    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long permissionId;

    @Expose
    private String permissionName;

    private String description;

    public Permission(String description, String permissionName){
        this.description = description;
        this.permissionName = permissionName;
    }

    @Override
    public String getAuthority() { return permissionName; }

    @Override
    public String toString(){return permissionName + "::" + description;}
}
