package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinColumnOrFormula;

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


    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(
          name = "user_id",
          referencedColumnName = "user_id"
    )
    UserInformation owner;

    @OneToMany
    List<Permission> permissions;

}
