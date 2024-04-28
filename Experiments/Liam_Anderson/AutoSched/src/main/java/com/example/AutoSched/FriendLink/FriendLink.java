package com.example.AutoSched.FriendLink;

import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="friendlink")
public class FriendLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user1", referencedColumnName = "id")
    @JsonIgnore
    private User user1;

    @OneToOne
    //@JoinColumn(name = "user2", referencedColumnName = "id")
    @JsonIgnore
    private User user2;

    public FriendLink(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
    public FriendLink() {
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public int getId() {
        return id;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}

