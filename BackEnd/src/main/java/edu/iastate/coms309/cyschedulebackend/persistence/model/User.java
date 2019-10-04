package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.StaleStateException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user_account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "user_id")
})
public class User implements Serializable{

    @Id
    @Column(name = "user_id", nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String userID ;

    @Column(name = "salt")
    private String salt;

    @Column(name = "jwt_key")
    private String jwtKey;

    @Column(name = "email")
    private String email;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    @Column(name = "role")
    @JoinTable(name = "user_to_user_role_table")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_account")
    private Set<UserRole> userRoles;
}
