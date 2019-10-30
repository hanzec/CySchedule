package edu.iastate.coms309.cyschedulebackend.persistence.model;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JoinColumnOrFormula;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
public class UserLoginToken {

    @Expose
    String token;

    @Id
    @Expose
    String tokenID;

    @Expose
    String refreshKey;


    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE,CascadeType.REFRESH}
    )
    @JoinColumn(
          name = "email"
    )
    UserCredential owner;

    @OneToMany
    List<Permission> permissions;

}
