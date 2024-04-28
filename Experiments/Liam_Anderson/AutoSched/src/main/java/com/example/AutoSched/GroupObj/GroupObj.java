package com.example.AutoSched.GroupObj;

import com.example.AutoSched.FriendLink.FriendLink;
import com.example.AutoSched.GroupLink.GroupLink;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="groupobj")
public class GroupObj {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="admin")
    private int admin;

    @OneToMany
    private List<GroupLink> grouplinks;

    public GroupObj(String name, int admin) {
        this.name = name;
        this.admin = admin;
    }

    public GroupObj() {
    }

    public String getName() {
        return name;
    }

    public int getAdmin() {
        return admin;
    }

    public void setName(String n) {
        name = n;
    }

    public int getId() {
        return id;
    }
    public void setAdmin(int admin) {
        this.admin = admin;
    }

}

