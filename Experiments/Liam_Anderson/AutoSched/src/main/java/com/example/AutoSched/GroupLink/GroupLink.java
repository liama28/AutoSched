package com.example.AutoSched.GroupLink;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="grouplink")
public class GroupLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "groupid", referencedColumnName = "id")
    @JsonIgnore
    private GroupObj groupobj;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    public GroupLink(GroupObj groupobj, User user) {
        this.groupobj = groupobj;
        this.user = user;
    }
    public GroupLink(){
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public GroupObj getGroupid() {
        return groupobj;
    }

    public void setGroupid(GroupObj groupobj) {
        this.groupobj = groupobj;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

