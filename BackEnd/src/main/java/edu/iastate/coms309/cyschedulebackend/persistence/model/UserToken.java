package edu.iastate.coms309.cyschedulebackend.persistence.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class UserToken {

    @Expose
    String token;

    @Id
    @Expose
    String tokenID;

    @Expose
    Time expireTime;

    @Expose
    String refreshKey;

    @Expose
    @JoinTable(name = "user_permission")
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<Permission> permissions;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(
          name = "email"
    )
    UserCredential owner;
}
