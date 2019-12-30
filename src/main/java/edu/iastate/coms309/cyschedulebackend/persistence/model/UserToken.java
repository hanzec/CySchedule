package edu.iastate.coms309.cyschedulebackend.persistence.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class UserToken {

    @Id
    @Expose
    String tokenID;

    @Expose
    String secret;

    @Expose
    Time expireTime;

    @Expose
    String refreshKey;

    @Column(
            name = "email",
            updatable = false,
            insertable = false
    )
    private String userEmail;

    @Expose
    @JoinTable(name = "token_permission")
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<Permission> permissions;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(name = "email")
    UserCredential owner;
}
