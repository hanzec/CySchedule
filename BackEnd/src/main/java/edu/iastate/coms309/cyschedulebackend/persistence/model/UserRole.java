package edu.iastate.coms309.cyschedulebackend.persistence.model;

import javax.persistence.*;

@Entity
@Table(name ="user_role")
public class UserRole {
    @Id
    @Column(name = "AUTHORITY")
    private String authority;

    @ManyToOne
    @JoinColumn(name = "USERID")
    private User user;
}
