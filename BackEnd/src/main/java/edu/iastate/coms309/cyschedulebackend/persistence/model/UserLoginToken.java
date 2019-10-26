package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class UserLoginToken {

    long userID;

    String token;

    @Id
    String tokenID;

    String refreshKey;

    @ManyToOne
    @JoinColumn(name = "user_token")
    UserDetails owner;

    @OneToMany
    List<Permission> permissions;

}
