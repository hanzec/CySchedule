package edu.iastate.coms309.cyschedulebackend.persistence.model;

import edu.iastate.coms309.cyschedulebackend.persistence.model.permission.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import javax.persistence.*;
import java.util.Collection;
import javax.validation.constraints.Email;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Cacheable
@Table(name = "user_credential")
public class UserCredential implements org.springframework.security.core.userdetails.UserDetails {

    @Id
    @Email
    private String email;

    private String jwtKey;

    private String password;

    private boolean lockStatus = true;

    private boolean activeStatus = false;

    @Column(
            name = "user_id",
            updatable = false,
            insertable = false
    )
    private String userID;


    @JoinTable(name = "user_permission")
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<UserRole> permissions;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(
            unique=true,
            name="user_id",
            referencedColumnName = "user_id"
    )
    private UserInformation userInformation;

    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return permissions;}

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
