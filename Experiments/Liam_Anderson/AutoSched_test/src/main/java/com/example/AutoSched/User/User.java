package com.example.AutoSched.User;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.HttpBodies.SessionId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name="users") // Not needed / Will generate value on its own
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    //@Column(name="name")
    private String name;

    //@Column(name="password")
    @JsonIgnore
    private String password;

    @JsonIgnore
    private int sessionId;

    @JsonIgnore
    private int privacyLevel;

    @JsonIgnore
    private long sessionExpire;

    @ManyToMany
    @JsonIgnore
    private List<User> friends = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<GroupObj> groupobjs = new ArrayList<>();

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.privacyLevel = 1;
    }

    public User() {
        this.privacyLevel = 1;
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public void addGroup(GroupObj group) {
        groupobjs.add(group);
    }

    public void updateSessionExpire() {
        this.sessionExpire = (System.currentTimeMillis() / 60000) + 60;
    }

    public boolean sessionExpired() {
        return (System.currentTimeMillis() / 60000) > this.sessionExpire;
    }

}

