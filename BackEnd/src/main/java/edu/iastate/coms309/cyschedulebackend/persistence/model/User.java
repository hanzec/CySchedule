package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collection;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User implements UserDetails, Serializable{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID ;

    @Email
    @NaturalId
    private String email;

    private String jwtKey;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    @JoinTable(name = "user_to_user_role_table")
    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserRole> userRoles;

    public User(String password, String firstname, String lastname, String email, String username) {
        this.email = email;
        this.password = password;
        this.lastName = lastname;
        this.username = username;
        this.firstName = firstname;
    }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return userRoles; }
}
