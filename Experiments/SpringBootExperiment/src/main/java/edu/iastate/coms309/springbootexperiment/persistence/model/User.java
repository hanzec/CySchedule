package edu.iastate.coms309.springbootexperiment.persistence.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(name = "user_account")
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String uuid;

    private String email;

    private String firstName;

    private String lastName;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "users_roles",
//               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
//               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    private Collection<Role> roles;

}
