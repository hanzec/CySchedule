package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class UserLoginToken {

    String token;

    @Id
    String tokenID;

    String refreshKey;


    @ManyToOne
    @JoinColumn(name = "user_token")
    UserInformation owner;

    @OneToMany
    List<Permission> permissions;

}
