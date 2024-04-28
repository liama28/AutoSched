package com.example.AutoSched.GroupObj;

import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Group object to represent groups (Group is a reserved word in mysql so class cant be named group)
 */
@Entity
@Data
public class GroupObj {

    /**
     * Unique number to identify the group
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Unique group name. Should be lower case
     */
    private String name;

    /**
     * User that represents the admin / owner of the group with high privileges on group operations
     */
    @OneToOne
    private User admin;

    /**
     * List of Users that belong to the group
     */
    @ManyToMany
    private List<User> users = new ArrayList<>();

    /**
     * String for the group password. Not required. Should be hashed with sha-256 and encoded in UTF-8
     */
    @JsonIgnore
    private String password;

    /**
     * Boolean to represent if a password is needed to join by a user
     */
    private boolean needsPassword;

    /**
     * Creates a GroupObj with a name and admin
     * @param name unique group name. Should be lower case
     * @param admin A User that is set as the admin
     */
    public GroupObj(String name, User admin) {
        this.name = name;
        this.admin = admin;
        this.needsPassword = false;
    }

    /**
     * Creates a group without name, password, or admin
     */
    public GroupObj() {
        this.needsPassword = false;
    }

    /**
     * Attempts to add user to users
     * @param user The user to add
     * @return True if user was added. False if user is already in users
     */
    public boolean addUser(User user) {
        if(user.equals(this)){
            return false;
        }
        int j = 0;
        for(User i : this.users) {
            if(i.equals(user)) {
                return false;
            }
        }
        this.users.add(user);
        return true;
    }

    /**
     * Attempts to remove user from users
     * @param user The user to remove
     * @return Return true if user is removed. False if user is not in list
     */
    public boolean removeUser(User user) {
        int j = 0;
        for (User i : this.users) {
            if(i.equals(user)) {
                this.users.remove(j);
                return true;
            }
            j++;
        }
        return false;
    }

    /**
     * Hashs a string with sha-256 and sets it as the password
     * @param passwordString String to be hashed
     * @throws NoSuchAlgorithmException
     */
    public void hashPassword(String passwordString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] pwHash = md.digest(passwordString.getBytes(StandardCharsets.UTF_8));
        this.password = new String(pwHash, StandardCharsets.UTF_8);
        this.needsPassword = true;
    }

    /**
     * Sets the password and sets need password to true
     * @param password String to be set as password
     */
    public void setPassword(String password) {
        this.password = password;
        this.needsPassword = true;
    }

    /**
     * Checks a hashed password with a given plaintext string
     * @param password String to be hashed and checked
     * @return True if password matches and false if it does not
     * @throws NoSuchAlgorithmException
     */
    public boolean checkPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] pwHash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String temp = new String(pwHash, StandardCharsets.UTF_8);
        return this.password.equals(temp);
    }

    /**
     * Checks to see if given group is equal (same id) has the parent class
     * @param group Group to be checked
     * @return True if the groups are equal and fails if they are not
     */
    public boolean equals(GroupObj group) {
        return group.getId() == this.id;
    }
}

