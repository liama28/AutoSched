package com.example.AutoSched.User;

import com.example.AutoSched.Calendar.Calendar;
import com.example.AutoSched.Calendar.CalendarRepository;
import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.exception.ErrorException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * User object to represent users
 */
@Entity
@Data
public class User {

    /**
     * Different types of privileges:
     * USER: Has access to their data, groups, other users' data that allow it
     * INSTRUCTOR: Has the privilege of a user plus the abilty to add students to groups they create
     * ADMIN: Full access to users' and groups' data and operations
     */
    public enum Privilege {
        USER, INSTRUCTOR, ADMIN
    }

    @Transient
    @JsonIgnore
    @Autowired
    UserRepository userRepository;


    /**
     * Unique integer to distinguish the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    /**
     * unique username
     */
    private String name;

    /**
     * Password string hashed with sha-256 and encoded with UTF-8
     */
    @JsonIgnore
    private String password;

    /**
     * Random number assigned to user for authentication. Expires after an hour of no activity
     */
    @JsonIgnore
    private int sessionId;

    /**
     * Privacy level for how much data the user is willing to share (ex. Calendar data to friends only)
     */
    @JsonIgnore
    private int privacyLevel;

    /**
     * Time in seconds since Jan 1st 1970 when the session id expires. Updates each time the session id is used
     */
    @JsonIgnore
    private long sessionExpire;

    /**
     * The users privilege.
     * The three privileges:
     * USER: Has access to their data, groups, other users' data that allow it
     * INSTRUCTOR: Has the privilege of a user plus the abilty to add students to groups they create
     * ADMIN: Full access to users' and groups' data and operations
     */
    @JsonIgnore
    private Privilege privilege;

    /***
     * List of Users that represents the users friends
     */
    @ManyToMany
    @JsonIgnore
    private List<User> friends = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    private Calendar calendar;

    /**
     * List of Users that represents other users that want to have a friend relationship with this user
     */
    @ManyToMany
    @JsonIgnore
    private List<User> friendRequests = new ArrayList<>();

    /**
     * List of Users that represents users this user wants to be friends with
     */
    @ManyToMany
    @JsonIgnore
    private List<User> sentRequests = new ArrayList<>();

    /**
     * List of GroupObj the represents all the groups this user is part of
     */
    @ManyToMany
    @JsonIgnore
    private List<GroupObj> groupobjs = new ArrayList<>();

    /**
     * Create a user with a given username and password. Privacy level will be set to 1 by default
     * and privilege will be set to user
     * @param name username string. Should be lower case and unique
     * @param password password string. Should be hashed with sha-256 and encoded with UTF-8
     */
    public User(String name, String password){
        this.name = name;
        this.password = password;

        this.privacyLevel = 1;
        this.privilege = Privilege.USER;
    }

    /**
     *
     * @param id
     * @param name
     * @param password
     */
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.privacyLevel = 1;
        this.privilege = Privilege.USER;

    }

    /**
     * Creates a user with default privacy 1 and default privilege USER.
     * Name and password will be null
     */
    public User() {
        this.privacyLevel = 1;
        this.privilege = Privilege.USER;
    }

    /**
     * Attempts to add a user to the User list friends
     * @param user The user to add to friends
     * @return True if the user was added. False if (1. the user is already in
     * list (can't add the same person twice) or 2. the user has the same id as
     * the parents of the friend list (can't add yourself))
     */
    public boolean addFriend(User user) {
        if(user.id == this.id) {
            return false;
        }
        if(friends.size() == 0) {
            friends.add(user);
            return true;
        }
        int i = binarySearch(this.friends, 0, friends.size() - 1, user.getId());
        if(friends.size() == i) {
            friends.add(user);
            return true;
        }
        if(friends.get(i).equals(user)) {
            return false;
        }
        if(user.getId() > friends.get(i).getId()) {
            friends.add(i+1, user);
            return true;
        }
        friends.add(i, user);
        return true;
    }

    /**
     * Attempts to add user to User list sentRequests
     * @param user The user to add to sentRequest
     * @return True if the user was added. False if (1. the user is already in list (can't send two request
     * to the same person) or 2. the user has the same id as the parents of the sentRequest
     * (you can't send your self a friend request))
     */
    public boolean addSentRequest(User user) {
        if(user.id == this.id) {
            return false;
        }
        if(sentRequests.size()==0) {
            sentRequests.add(user);
            return true;
        }
        int i = binarySearch(this.sentRequests, 0, sentRequests.size() - 1, user.getId());
        if(sentRequests.size() == i) {
            sentRequests.add(user);
            return true;
        }
        if(sentRequests.get(i).equals(user)) {
            return false;
        }
        if(user.getId() > sentRequests.get(i).getId()) {
            sentRequests.add(i+1, user);
            return true;
        }
        sentRequests.add(i, user);
        return true;
    }

    /**
     * Attempts to add user to User list friendRequests
     * @param user The user to add to friendRequests
     * @return True if the user was added. False if (1. the user is already in list (can't send two request
     * to the same person) or 2. the user has the same id as the parents of the sentRequest
     * (you can't send your self a friend request))
     */
    public boolean addFriendRequest(User user) {
        if(user.id == this.id) {
            return false;
        }
        if(friendRequests.size() == 0) {
            friendRequests.add(user);
            return true;
        }
        int i = binarySearch(this.friendRequests, 0, friendRequests.size() - 1, user.getId());
        if(friendRequests.size() == i) {
            friendRequests.add(user);
            return true;
        }
        if(friendRequests.get(i).getId() == user.getId()) {
            return false;
        }
        if(user.getId() > friendRequests.get(i).getId()) {
            friendRequests.add(i+1, user);
            return true;
        }
        friendRequests.add(i, user);
        return true;
    }

    /**
     * Attempts to remove User from sentRequests
     * @param user The user to remove from sentRequest
     * @return True if user was removed. False if User was not found in the list
     */
    public boolean removeSentRequest(User user) {
        if(sentRequests.size() == 0) {
            return false;
        }
        int i = binarySearch(sentRequests, 0, sentRequests.size()-1, user.getId());
        if(i == sentRequests.size()) {
            return false;
        }
        if(!sentRequests.get(i).equals(user)) {
            return false;
        }
        else sentRequests.remove(i);
        return true;
    }

    /**
     * Attempts to remove User from friends
     * @param user The user to remove from friends
     * @return True if user was removed. False if user was not found in the list
     */
    public boolean removeFriend(User user) {
        if(friends.size() == 0) {
            return false;
        }
        int i = binarySearch(friends, 0, friends.size()-1, user.getId());
        if(i == friends.size()) {
            return false;
        }
        if(!friends.get(i).equals(user)) {
            return false;
        }
        else friends.remove(i);
        return true;
    }

    /**
     * Attempts to accept (Move user from friendRequest to friends) friend request
     * @param user The user to accept as a friend from friendRequest
     * @return True if the users friend request was accepted. False if (1. user
     * is already a friend, 2. if the user is not found in friendRequest, 3. user is equal to parent)
     */
    public boolean acceptRequest(User user) {
        if(friendRequests.size() == 0) {
            return false;
        }
        int i = binarySearch(friendRequests, 0, friendRequests.size()-1, user.getId());
        if(i == friendRequests.size()) {
            return false;
        }
        if(!friendRequests.get(i).equals(user)) {
            return false;
        }
        if(!this.addFriend(friendRequests.get(i))) {
            return false;
        }
        if(!user.removeSentRequest(this)) {
            return false;
        }
        friendRequests.remove(i);
        return true;
    }

    /**
     * Attempts to deny (remove user from friendRequests) friend request
     * @param user The user to deny as a friend from friendRequest
     * @return True if the user was removed from friendRequest. False if
     * user was not found
     */
    public boolean denyRequest(User user) {
        if(friendRequests.size() == 0) {
            return false;
        }
        int i = binarySearch(friendRequests, 0, friendRequests.size()-1, user.getId());
        if(i == friendRequests.size()) {
            return false;
        }
        if(friendRequests.get(i).getId() != user.getId()) {
            return false;
        }
        if(!user.removeSentRequest(this)) {
            return false;
        }
        friendRequests.remove(i);
        return true;
    }

    /**
     * Attempts to add group to groupobjs
     * @param group The group to add to groupobjs
     * @return True if group was added. False if group is already groupobjs
     */
    public boolean addGroup(GroupObj group) {
        for (GroupObj i : groupobjs) {
            if(i.equals(group)) {
                return false;
            }
        }
        groupobjs.add(group);
        return true;
    }

    /**
     * Attempts to remove group from groupobjs
     * @param group The group to be removed
     * @return True if group was removed. False if group is not found
     */
    public boolean removeGroup(GroupObj group) {
        int j = 0;
        for (GroupObj i : groupobjs) {
            if(i.equals(group)) {
                groupobjs.remove(0);
                return true;
            }
            j++;
        }
        return false;
    }

    /**
     * Changes the session expire time to an hour from now
     */
    public void updateSessionExpire() {
        this.sessionExpire = (System.currentTimeMillis() / 60000) + 60;
    }

    /**
     * Checks to see if session is expired
     * @return Returns true if current time is greater than session expire time
     */
    public boolean sessionExpired() {
        return (System.currentTimeMillis() / 60000) > this.sessionExpire;
    }

    /**
     * recursive function that performs a binary search on a user list that is sorted
     * from low to high user id
     * @param list the User list to search through
     * @param l Left search domain value
     * @param r right search domain value
     * @param id User id to look for
     * @return Index of user or where it should be if not found
     */
    private int binarySearch(List<User> list, int l, int r, int id) {
        if (r >= l) {
            int mid = l + (r - l) / 2;
            if (list.get(mid).getId() == id) {
                return mid;
            }
            if (list.get(mid).getId() > id) {
                return binarySearch(list, l, mid - 1, id);
            }
            return binarySearch(list,mid + 1, r, id);
        }
        return l;
    }

    /**
     * Check to see if the given user is friends with the parents user
     * @param user Check to see if user is in friends
     * @return True if user is in friends. False if it's not
     */
    public boolean isFriend(User user) {
        if(friends.size() == 0) {
            return false;
        }
        int i = binarySearch(friends, 0, friends.size()-1, user.getId());
        if(i == friends.size()) {
            return false;
        }
        if (friends.get(i).id == user.getId()){
            return true;
        }
        return false;
    }

    /**
     * Check to see if the given user is a requesting to be friends with the parents user
     * @param user Check to see if user is in friendRequests
     * @return True if user is in friendRequest. False if it is not
     */
    public boolean isRequester(User user) {
        if(friendRequests.size() == 0) {
            return false;
        }
        int i = binarySearch(friendRequests, 0, friendRequests.size()-1, user.getId());
        if(i == friendRequests.size()) {
            return false;
        }
        if (friendRequests.get(i).id == user.getId()){
            return true;
        }
        return false;
    }

    /**
     * Check to see if the given session information is correct (has not expired and correct session id)
     * @param s Session id
     * @throws ErrorException Either (1. Request denied (incorrect session id) or 2. Session Expired)
     */
    public void checkSession(SessionId s) throws ErrorException {
        if(s.getSessionid() != sessionId) {
            throw new ErrorException(3, "Request denied");
        }
        if(sessionExpired()) {
            throw new ErrorException(4, "Session Expired");
        }
        updateSessionExpire();
    }

    /**
     *
     * @return True if user is admin. False if not
     */
    @JsonIgnore
    public boolean isAdmin() {
        return this.privilege == Privilege.ADMIN;
    }

    /**
     *
     * @return True if user is a instructor. False if not
     */
    @JsonIgnore
    public boolean isInstructor() {
        return this.privilege == Privilege.INSTRUCTOR;
    }

    /**
     * Sees if given user has the same id as parents user
     * @param user User to check
     * @return True if the two users are equal. False if they are not
     */
    public boolean equals(User user) {
        return this.id == user.id;
    }

    /**
     * Hashes given password with sha-256 and encodes it with UTF-8 and set it as the password
     * @param passwordString Plain-text password
     * @throws NoSuchAlgorithmException
     */
    public void hashPassword(String passwordString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] pwHash = md.digest(passwordString.getBytes(StandardCharsets.UTF_8));
        this.password = new String(pwHash, StandardCharsets.UTF_8);
    }

}

