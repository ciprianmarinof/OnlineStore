package com.example.finalproject.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles"
            ,joinColumns = {@JoinColumn(name = "user_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles = new ArrayList<>();


    public void addRole(Role role){
        this.roles.add(role);
        role.getUsers().add(this);
    }

}
