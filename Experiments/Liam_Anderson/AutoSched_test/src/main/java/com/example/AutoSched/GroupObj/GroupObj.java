package com.example.AutoSched.GroupObj;

import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name="groupobj")
@Data
public class GroupObj {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column(name="id")
    private int id;

    //@Column(name="name")
    private String name;

    //@Column(name="admin")
    @OneToOne
    private User admin;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    public GroupObj(String name, User admin) {
        this.name = name;
        this.admin = admin;
    }

    public GroupObj() {
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}

