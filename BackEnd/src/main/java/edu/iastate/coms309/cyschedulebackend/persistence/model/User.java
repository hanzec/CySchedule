package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User implements Serializable{

    @Id
    @Column(name = "user_id")
    private String userID ;

    private String email;

    private String salt;

    private String jwtKey;

    private String username;

    private String lastName;

    private String password;

    private String firstName;

    private Long registerTime;

    @JoinTable(name = "user_to_user_role_table")
    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserRole> userRoles;

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (email == null ? 0 : email.hashCode());
        result = PRIME * result + (userID == null ? 0 : userID.hashCode());
        return result;
    }

    /**
     * 覆盖equals方法，必须要有
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj instanceof User)) return false;
        User objKey = (User) obj;
        if(userID.equalsIgnoreCase(objKey.userID) &&
                email.equalsIgnoreCase(objKey.email)) {
            return true;
        }
        return false;
    }
}
