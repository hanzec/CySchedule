package edu.iastate.coms309.cyschedulebackend.persistence.model;


import lombok.Data;
import java.util.Set;
import javax.persistence.*;

@Data
@Entity
@Table(name ="user_role")
public class UserRole{
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer roleID;

    @ManyToMany
    @JoinColumn(name = "user_role_to_id")
    private Set<User> user;

    private String roleName;

    private String description;

    @ManyToMany
    @JoinColumn(name = "user_role_to_permission")
    private Set<Permission> rolePermissions;

    @Override
    public String toString(){ return roleName;}
}
