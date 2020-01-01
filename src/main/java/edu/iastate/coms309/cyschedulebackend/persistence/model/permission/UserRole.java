package edu.iastate.coms309.cyschedulebackend.persistence.model.permission;

import com.amazonaws.handlers.StackedRequestHandler;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long roleId;

    private String roleName;

    private String description;

    @ManyToMany
    private Set<Permission> permissions = new HashSet<>();

    public UserRole(String roleName, String description){
        this.roleName = roleName;
        this.description = description;
    }

    @Override
    public String toString(){return roleName + "::" + description;}
}
