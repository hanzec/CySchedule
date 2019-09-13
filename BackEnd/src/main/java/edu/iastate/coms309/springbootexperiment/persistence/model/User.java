package edu.iastate.coms309.springbootexperiment.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "user_account")
public class User {

    @Id
    @Column(name = "USERID",unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    private String email;

    private String firstName;

    private String lastName;

    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserRole> authorities = new HashSet<>();

}
