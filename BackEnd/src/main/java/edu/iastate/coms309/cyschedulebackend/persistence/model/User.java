package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_account",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "user_id")
})
public class User implements Serializable,AuthenticationInfo {

    @Id
    @Column(name = "user_id", nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String userID ;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;


    @Column(name = "role")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user_account")
    private List<UserRole> authorities = new ArrayList<>();

<<<<<<< HEAD

=======
    @Override
    public PrincipalCollection getPrincipals() {
        return userID;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
>>>>>>> 526f294a662aad248b89a200aa9c6948ceb9c735
}
