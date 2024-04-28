package com.example.AutoSched.User;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.example.AutoSched.Calendar.Calendar;
import com.example.AutoSched.Calendar.CalendarRepository;
import com.example.AutoSched.Calendar.Event;
import com.example.AutoSched.Calendar.EventRepository;
import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.GroupObj.GroupObjRepository;
import com.example.AutoSched.GroupObj.GroupObjService;
import com.example.AutoSched.HttpBodies.AccountCredentials;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.exception.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Logic for incoming and outgoing http requests for users
 */
@Api(value = "User Object Controller", description = "REST APIs related to User object handling")
@RestController
public class UserController {

    /**
     * Used to make mysql queries for users
     */
    @Autowired
    UserService userRepository;

    /**
     * Used to make mysql queries or groups
     */
    @Autowired
    GroupObjService groupObjRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    /**
     * Success string that represents a JSON object that is returned when a
     * request if successfully completed
     */
    private String success = "{\"code\":0,\"message\":\"success\"}";


    /**
     * Gets all users name and id in a list of objects
     * @return A list of all users
     */
    @ApiOperation(value = "Gets all users name and id in a list of objects")
    @GetMapping(value = "/user/getAllData")
    public List<User> getAllData() {
        return userRepository.findAll();
    }

    /**
     * Given account credentials in the http body, this method creates a new User, hashes the password, and
     * generates the user id and session id
     * @param a Account credentials (Username and plaintext password
     * @return The sessionId (user id and session id) when successful, throws error when not
     */

    @ApiOperation(value = "Given account credentials in the http body, this method creates a new User, " +
            "hashes the password, and generates the user id and session id")
    @PostMapping(value = "/user/save")
    public SessionId saveUser(@RequestBody AccountCredentials a) throws NoSuchAlgorithmException {
        if(a.getName() == null || a.getPassword() == null) {
            throw new ErrorException(1, "No username or password given");
        }
        User u = new User();
        u.setName(a.getName().toLowerCase());
        if(userRepository.findByUsername(u.getName()) != null) {
            throw new ErrorException(2, "Username already taken");
        }
        Calendar calendar = new Calendar();
        u.setCalendar(calendar);
        calendarRepository.save(calendar);
        u.setPassword(a.passwordHash());
        SessionId s = new SessionId(u.getId());
        u.setSessionId(s.getSessionid());
        u.updateSessionExpire();
        userRepository.save(u);
        s.setUserid(u.getId()); // For some reason the session ID is 0 until userRepository.save is called
        return s;
    }

    /**
     * Attempts to delete user from given user id
     * @param userId Id of user to be deleted
     * @param s Session id to authenticate user making this request. Must be user being deleted or admin
     * @return Success message when successful else throws an error
     */
    @ApiOperation(value = "Attempts to delete user from given user id")
    @DeleteMapping(value = "/user/del/{userId}")
    public String delUser(@PathVariable int userId, @RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        User userDel = userRepository.findById(userId);
        if (user == null || userDel == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        if(!user.equals(userDel) && !user.isAdmin()) {
            throw new ErrorException(3, "Request denied");
        }
        for (User i : user.getFriends()) {
            i.removeFriend(user);
            userRepository.save(i);
        }
        for(GroupObj i : user.getGroupobjs()) {
            i.removeUser(user);
            groupObjRepository.save(i); 
        }
        for(User i : user.getSentRequests()) {
            i.denyRequest(user);
            user.removeSentRequest(i);
            userRepository.save(i);
        }
//        for(Event i : user.getCalendar().getEvents()) {
//            user.getCalendar().delEvent(i);
//            eventRepository.delete(i);
//        }
//        calendarRepository.delete(user.getCalendar());
        userRepository.delete(userDel);
        return success;
    }

    /**
     * Attempts to add friend to user that is authenticated with session id. If the friend has not
     * sent a friend request a friend request will be sent. If a friend request has been sent by friend user
     * both users will have each other added to their friends list and the request will be removed
     * @param userId Id of the attended friend
     * @param s Session id of the requester
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to add a friend to the user that is authenticated with session id")
    @PutMapping("/user/addFriend/{userId}")
    public String addFriend(@PathVariable int userId, @RequestBody SessionId s){
        User user = userRepository.findById(s.getUserid());
        User userFriend = userRepository.findById(userId);
        if(userFriend == null || user == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        if(user.acceptRequest(userFriend)) {
            if(!userFriend.addFriend(user)) {
                throw new ErrorException(6, "Add friend error");
            }
        }
        else {
            if(!userFriend.addFriendRequest(user)) {
                throw new ErrorException(6, "Add friend error");
            }
            if(!user.addSentRequest(userFriend)) {
                throw new ErrorException(6, "Add sent request error");
            }
        }
        userRepository.save(user);
        userRepository.save(userFriend);
        return success;
    }

    /**
     * Attempts to deleted friend from users friends list and the friends list of the friend
     * @param userId Id of friend to be deleted from list
     * @param s Session id of the requester
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to deleted friend from users friends list and the friends list of the friend")
    @DeleteMapping("/user/delFriend/{userId}")
    String delFriend(@PathVariable int userId, @RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        User friendDel = userRepository.findById(userId);
        if(user == null || friendDel == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        if(!user.removeFriend(friendDel)) {
            throw new ErrorException(6, "remove friend error");
        }
        if(!friendDel.removeFriend(user)) {
            throw new ErrorException(6, "remove friend error");
        }
        userRepository.save(user);
        userRepository.save(friendDel);
        return success;
    }

    /**
     * Attempts to accept friendRequest from friendRequests
     * @param userId Id of user to accept friend request from
     * @param s Session id of user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to accept friendRequest from friendRequests")
    @PutMapping("/user/friendRequest/{userId}/accept")
    String acceptFriendRequest(@PathVariable int userId, @RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        User userAccept = userRepository.findById(userId);
        if(userAccept == null|| user == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        if(!user.acceptRequest(userAccept)) {
            throw new ErrorException(6, "accept request error");
        }
        userAccept.addFriend(user);
        userRepository.save(user);
        userRepository.save(userAccept);
        return success;
    }

    /**
     * Attempts to deny friendRequest from friendRequests
     * @param userId Id of user to deny friend request from
     * @param s Session id of user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to deny friendRequest from friendRequests")
    @PutMapping("/user/friendRequest/{userId}/deny")
    String denyFriendRequest(@PathVariable int userId, @RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        User userDeny = userRepository.findById(userId);
        if(user == null ||userDeny == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        if(!user.denyRequest(userDeny)) {
            throw new ErrorException(6, "deny request error");
        }
        userRepository.save(user);
        return success;
    }

    /**
     * Attempts to add user to group. This method it for instructors and admins and must authenticate
     * as one or the other to perform this operation
     * @param userId Id of user to be added to group
     * @param groupId Id of group
     * @param s Session id of user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = " Attempts to add user to group. This method it for instructors and admins and must authenticate " +
            "as one or the other to perform this operation")
    @PutMapping("/user/{userId}/addGroup/{groupId}")
    String addUserToGroup(@PathVariable int userId,@PathVariable int groupId, @RequestBody SessionId s){
        User user = userRepository.findById(s.getUserid());
        User user1 = userRepository.findById(userId);
        GroupObj group = groupObjRepository.findById(groupId);
        if(user == null || group == null || user1 == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!user.isAdmin() && !(user.isInstructor() && group.getAdmin().equals(user))) {
            throw new ErrorException(3, "Request denied");
        }
        user1.addGroup(group);
        group.addUser(user1);
        groupObjRepository.save(group);
        userRepository.save(user);
        userRepository.save(user1);
        return success;
    }


    /**
     * Attempts to add user to group without a password
     * @param groupId Id of group
     * @param s Session id of user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to add user to group without a password")
    @PutMapping("/user/addGroup/{groupId}")
    public String addToGroup(@PathVariable int groupId, @RequestBody SessionId s){
        User user = userRepository.findById(s.getUserid());
        GroupObj group = groupObjRepository.findById(groupId);
        if(user == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        if(group.getPassword() != null) {
            throw new ErrorException(3, "Password needed");
        }
        user.checkSession(s);
        user.addGroup(group);
        group.addUser(user);
        groupObjRepository.save(group);
        userRepository.save(user);
        return success;
    }

    /**
     * Attempts to remove group from users groups
     * @param groupId Id of group to be removed
     * @param s Session Id of the user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to remove group from users groups")

    @DeleteMapping("/user/delGroup/{groupId}")
    String removeFromGroup(@PathVariable int groupId, @RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        GroupObj group = groupObjRepository.findById(groupId);
        if(user == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!group.removeUser(user)) {
            throw new ErrorException(5, "User not found in group");
        }
        if(!user.removeGroup(group)) {
            throw new ErrorException(5, "User not in group");
        }
        userRepository.save(user);
        groupObjRepository.save(group);
        return success;
    }

    /**
     * Attempts to add user to group with given plaintext password
     * @param groupId Id of group
     * @param password Plaintext string for the groups password
     * @param s Seassion id of user making the request
     * @return Returns success message if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to add user to group with given plaintext password")

    @PutMapping("/user/addGroup/{groupId}/{password}")
    String addToGroupWithPassword(@PathVariable int groupId, @PathVariable String password, @RequestBody SessionId s) throws NoSuchAlgorithmException {
        User user = userRepository.findById(s.getUserid());
        GroupObj group = groupObjRepository.findById(groupId);
        if(user == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        if(!group.checkPassword(password)) {
            throw new ErrorException(3, "Incorrect password");
        }
        user.checkSession(s);
        user.addGroup(group);
        group.addUser(user);
        groupObjRepository.save(group);
        userRepository.save(user);
        return success;
    }


    /**
     * Attempts to login (create session id) to user with provided user credentials
     * @param a Account credentials (username and password)
     * @return Returns session id if successful. Else it will throw an error
     */
    @ApiOperation(value = "Attempts to login (create session id) to user with provided user credentials")

    @PostMapping("user/login")
    public SessionId login(@RequestBody AccountCredentials a) throws NoSuchAlgorithmException {
        if (a.getName() == null || a.getPassword() == null) {
            throw new ErrorException(1, "Missing username or password");
        }
        User user = userRepository.findByUsername(a.getName());
        if (user == null) {
            throw new ErrorException(5, "user not found");
        }
        if (!a.passwordHash().equals(user.getPassword())) {
            throw new ErrorException(3, "Request denied");
        }
        SessionId s = new SessionId(user.getId());
        user.setSessionId(s.getSessionid());
        user.updateSessionExpire();
        userRepository.save(user);
        return s;
    }

    /**
     * Finds a user with a given username
     * @param username Username used to query database
     * @return User with the username given. Returns null if no user is found
     */
    @ApiOperation(value = "Finds a user with a given username")

    @GetMapping("/user/name/{username}")
    User getUserFromName(@PathVariable String username) {
        return userRepository.findByUsername(username.toLowerCase());
    }

    /**
     * Finds users whose users' start with the given string
     * @param name Start of username to query in database
     * @return A list of users whose usernames' start with the provided string
     */
    @ApiOperation(value = "Finds users whose users' start with the given string")

    @GetMapping("/user/nameStartsWith/{name}")
    List<User> getUsersFromNameStartsWith(@PathVariable String name) {
        return userRepository.findByStartOfName("%"+name.toLowerCase()+"%");
    }

    /**
     * Returns users friend requests
     * @param s Session id of the user making the request
     * @return The users friend requests (list of users) if correctly authenticated.
     * Else throws an error
     */
    @ApiOperation(value = "Returns users friend requests")

    @PostMapping("/user/friendRequest")
    List<User> getFriendRequest(@RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        if(user == null ) {
            throw new ErrorException(5, "user not found");
        }
        user.checkSession(s);
        userRepository.save(user);
        return user.getFriendRequests();
    }

    /**
     * Returns users sent friend requests
     * @param s Session id of the user making the request
     * @return The users sent request (list of user) if currently authenticated.
     * Else throws an error
     */
    @ApiOperation(value = "Returns users sent friend requests")

    @PostMapping("/user/sentRequest")
    List<User> getSentRequest(@RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        if(user == null ) {
            throw new ErrorException(5, "user not found");
        }
        user.checkSession(s);
        userRepository.save(user);
        return user.getSentRequests();
    }

    /**
     * Returns user with provided id
     * @param id Id to be searched for
     * @return User with given id. Null if not user is found
     */
    @ApiOperation(value = "Returns user with provided id")

    @GetMapping("/user/{id}")
    User getUserFromId(@PathVariable int id) {
        User user = userRepository.findById(id);
//        if(user == null) {
//            throw new ErrorException(5, "User not found");
//        }
        return user;
    }

    /**
     * Searched for users friends with provided user id
     * @param id Id of user to find
     * @return A list of users. Throws an error if user is not found
     */
    @ApiOperation(value = "Searches for users friends with provided user id")

    @GetMapping("/user/{id}/friends")
    List<User> getFriendsFromId(@PathVariable int id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        return user.getFriends();
    }

    /**
     * Searches for users groups with provided user id
     * @param id Id of user to find
     * @return A list of groups. Throws an error if user is not found
     */
    @ApiOperation(value = "Searches for users groups with provided user id")

    @GetMapping("/user/{id}/groups")
    List<GroupObj> getGroupsFromId(@PathVariable int id) {
        User user = userRepository.findById(id);
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        return user.getGroupobjs();
    }

}

