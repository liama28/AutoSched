package com.example.AutoSched.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.GroupObj.GroupObjRepository;
import com.example.AutoSched.HttpBodies.AccountCredentials;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.exception.ExpiredSessionException;
import com.example.AutoSched.exception.InvalidAuthenticationException;
import com.example.AutoSched.exception.InvalidUsernameOrPasswordException;
import com.example.AutoSched.exception.UserTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupObjRepository groupObjRepository;

    private String failure = "{\"message\":\"failure\"}";
    private String success = "{\"message\":\"success\"}";
    //private String nameInUse = "{\"message\":\"username already in use\"}";


    // Gets all users name and id in a list of objects
    @GetMapping(value = "/user/getAllData")
    public List<User> getAllData() {
        return userRepository.findAll();
    }

    // Given a User object in the http body, this method creates a new User, hashes the password, and
    // generates the user id and session id
    // Body example:
    // {
    //      "name": "liam",
    //      "password": "liam_password"
    // }
    // returns session id ex
    // {
    //      "UserId": 1,
    //      "SessionId": 1234523142
    // {
    @PostMapping(value = "/user/save")
    public SessionId saveUser(@RequestBody AccountCredentials a) throws NoSuchAlgorithmException {
        if(a.getPassword() == null || a.getName() == null || a.getPassword().length() < 5) {
            throw new InvalidUsernameOrPasswordException();
        }
        User u = new User();
        u.setName(a.getName().toLowerCase());
        if(userRepository.findByUsername(u.getName()) != null) {
            throw new UserTakenException();
        }
        u.setPassword(a.passwordHash());
        SessionId s = new SessionId(u.getId());
        u.setSessionId(s.getSessionId());
        u.updateSessionExpire();
        userRepository.save(u);
        s.setUserId(u.getId()); // For some reason the session ID is 0 until userRepository.save is called
        return s;
    }

    // Given the user and friend, this method adds the friend to the user's friends list
    // Requires authentication body ex
    // /user/1/add/2
    // http body:
    // {
    //      "UserId": 1,
    //      "SessionId": 22352351
    // }
    // Returns success message if it works, a failure message if it does not.
    // Throws authentication or expired sessions errors when needed
    @PutMapping("/user/{user1Id}/add/{user2Id}")
    String addFriend(@PathVariable int user1Id,@PathVariable int user2Id, @RequestBody SessionId s){
        User user1 = userRepository.findById(user1Id);
        User user2 = userRepository.findById(user2Id);
        if(user1 == null || user2 == null || s == null) {
            return failure;
        }
        if(user1.getSessionId() != s.getSessionId()) {
            throw new InvalidAuthenticationException();
        }
        if(user1.sessionExpired()) {
            throw new ExpiredSessionException();
        }
        user1.addFriend(user2);
        user1.updateSessionExpire();
        userRepository.save(user1);
        return success;
    }

    //  Given a user and group, the user's group list is updated and the group's user list is updated
    //  Requires authentication body with user id and session id
    @PutMapping("/user/{userId}/add/{groupId}")
    String addUserToGroup(@PathVariable int userId,@PathVariable int groupId, @RequestBody SessionId s){
        User user = userRepository.findById(userId);
        GroupObj group = groupObjRepository.findById(groupId);
        if(user == null || group == null || s == null) {
            return failure;
        }
        if(user.getSessionId() != s.getSessionId()) {
            throw new InvalidAuthenticationException();
        }
        if(user.sessionExpired()) {
            throw new ExpiredSessionException();
        }
        user.updateSessionExpire();
        user.addGroup(group);
        group.addUser(user);
        groupObjRepository.save(group);
        userRepository.save(user);
        return success;
    }

    // given body:
    // {
    //      "name": "<username>",
    //      "password": "<password>"
    // }
    @PostMapping("user/login")
    public SessionId login(@RequestBody AccountCredentials a) throws NoSuchAlgorithmException {
        if (a.getName() == null || a.getPassword() == null) {
            throw new InvalidUsernameOrPasswordException();
        }
        User user = userRepository.findByUsername(a.getName());
        if (user == null) {
            throw new InvalidUsernameOrPasswordException();
        }
        if (!a.passwordHash().equals(user.getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }
        SessionId s = new SessionId(user.getId());
        user.setSessionId(s.getSessionId());
        user.updateSessionExpire();
        userRepository.save(user);
        return s;
    }

    // Returns a user given the username
    @GetMapping("/user/name/{username}")
    User getUserFromName(@PathVariable String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }

    // Returns the user given the id
    @GetMapping("/user/{id}")
    User getUserFromId(@PathVariable int id) {
        return userRepository.findById(id);
    }

    // Returns the user's friends given the user id
    @GetMapping("/user/{id}/friends")
    List<User> getFriendsFromId(@PathVariable int id) {return userRepository.findById(id).getFriends();}

    // Returns the user's groups given the user's id
    @GetMapping("/user/{id}/groups")
    List<GroupObj> getGroupsFromId(@PathVariable int id) {return userRepository.findById(id).getGroupobjs();}



}

