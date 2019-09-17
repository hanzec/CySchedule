package edu.iastate.coms309.cyschedulebackend.persistence.model;


import javax.persistence.*;

@Entity
@Table(name ="user_role")
public class UserRole{
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer roleID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    String roleName;

    String description;
}
