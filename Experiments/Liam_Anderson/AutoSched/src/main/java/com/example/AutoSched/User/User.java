package com.example.AutoSched.User;

import com.example.AutoSched.FriendLink.FriendLink;
import com.example.AutoSched.GroupLink.GroupLink;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @OneToMany
    private List<FriendLink> friendlinks;

    @OneToMany
    private List<GroupLink> grouplinks;

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        password = p;
    }
}

