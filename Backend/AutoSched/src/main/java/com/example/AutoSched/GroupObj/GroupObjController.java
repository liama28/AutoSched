package com.example.AutoSched.GroupObj;


import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserRepository;
import com.example.AutoSched.User.UserService;
import com.example.AutoSched.exception.ErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Logic for incoming and outgoing http requests for groups
 */
@Api(value = "Group Objects Controller", description = "REST APIs related to Group object handling")
@RestController
public class GroupObjController {

    /**
     * For mysql user queries
     */
    @Autowired
    GroupObjService GroupObjRepository;

    /**
     * For mysql group queries
     */
    @Autowired
    UserService UserRepository;

    /**
     * Success message to send out
     */
    private String success = "{\"code\":0,\"message\":\"success\"}";

    /**
     * Gets all group data
     * @return GroupObj List of all groups
     */
    @ApiOperation(value = "Gets all data from the Groups database on MySQL server")
    @GetMapping(value = "/group/getAllData")
    public List<GroupObj> getAllData() {
        return GroupObjRepository.findAll();
    }


    /**
     * Takes a JSON http body and creates a new group. Checks to see if a name is given and if the name is taken
     * @param groupName Name for the new group. Must be unique
     * @param s Session of the user making this request. Will be used to decide the group admin
     * @return Success message if group was successfully made else throws an error message w
     */
    @ApiOperation(value = "Saves a group with the given group name to the database")
    @PostMapping(value = "/group/save/{groupName}")
    public GroupObj saveGroup(@PathVariable String groupName, @RequestBody SessionId s) {
        User user = UserRepository.findById(s.getUserid());
        GroupObj groupCheck = GroupObjRepository.findByGroupname(groupName.toLowerCase());
        if(groupCheck != null) {
            throw new ErrorException(2, "Group name already taken");
        }
        GroupObj group = new GroupObj(groupName.toLowerCase(), user);
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        UserRepository.save(user);
        GroupObjRepository.save(group);
        return group;
    }

    /**
     * Adds user to group. This request is for instructors and admins and the HTTP season body must
     * authenticate an admin or instructor.
     * @param groupId Id of the group
     * @param userId Id of the user to add to the group
     * @param s Session of the user making this request
     * @return Sucess message if user was added to group successfully else throw an error message
     */
    @ApiOperation(value = "Adds the User with the given userId to the Group with the given groupId")
    @PutMapping("/group/{groupId}/add/{userId}")
    String addUserToGroup(@PathVariable int groupId,@PathVariable int userId, @RequestBody SessionId s){
        User user = UserRepository.findById(s.getUserid());
        User userAdd = UserRepository.findById(userId);
        GroupObj group = GroupObjRepository.findById(groupId);
        if(user == null || userAdd == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!user.isAdmin() && !(user.isInstructor() && group.getAdmin().equals(user))) {
            throw new ErrorException(3, "Request denied");
        }
        group.addUser(userAdd);
        userAdd.addGroup(group);
        GroupObjRepository.save(group);
        UserRepository.save(userAdd);
        UserRepository.save(user);
        return success;
    }

    /**
     * Sets or changes the group admin for a given group. The requester must be either an admin or the
     * group's admin to make this change
     * @param groupId Id to the group
     * @param userId Id to the user that will be made the admin
     * @param s Session of the user making this request. Should be the group admin or a system admin
     * @return Success message if the group's admin was set else thows an error message
     */
    @ApiOperation(value = "Sets the admin of the Group given by the groupId")
    @PutMapping("/group/{groupId}/setAdmin/{userId}")
    public String addAdminToGroup(@PathVariable int groupId, @PathVariable int userId, @RequestBody SessionId s){
        User user = UserRepository.findById(s.getUserid());
        User userAdmin = UserRepository.findById(userId);
        GroupObj group = GroupObjRepository.findById(groupId);
        if(user == null || userAdmin == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!user.equals(group.getAdmin()) && !user.isAdmin()) {
            throw new ErrorException(3, "Request denied");
        }
        group.setAdmin(user);
        UserRepository.save(user);
        GroupObjRepository.save(group);
        return success;
    }

    /**
     * Sets a given groups password. The request must be from the group admin or a system admin
     * @param groupId Id for the group
     * @param password The plaintext password to be hashed and set for the given group
     * @param s The season of the user making the request for authentication
     * @return Success message if password was successfully set else throws an error
     * @throws NoSuchAlgorithmException
     */
    @ApiOperation(value = "Sets the password to the group with the given groupId")
    @PutMapping("/group/{groupId}/setPassword/{password}")
    String addPasswordToGroup(@PathVariable int groupId, @PathVariable String password, @RequestBody SessionId s) throws NoSuchAlgorithmException {
        User user = UserRepository.findById(s.getUserid());
        GroupObj group = GroupObjRepository.findById(groupId);
        if(user == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!user.equals(group.getAdmin()) && !user.isAdmin() ) {
            throw new ErrorException(3, "Request denied");
        }
        if(password == null) {
            throw new ErrorException(5, "no password provided");
        }
        group.hashPassword(password);
        GroupObjRepository.save(group);
        UserRepository.save(user);
        return success;
    }

    /**
     * Deletes a group and removes all users from group with given group id
     * @param groupId Id of group to be deleted
     * @param s Session of the user making the request. Must be an admin or the groups admin
     * @return Success message if group was successfully deleted else throws an error
     */
    @ApiOperation(value = "Deletes the group at the given groupId")
    @DeleteMapping("/group/del/{groupId}")
    String delGroup(@PathVariable int groupId, @RequestBody SessionId s) {
        User user = UserRepository.findById(s.getUserid());
        GroupObj group = GroupObjRepository.findById(groupId);
        if(user == null || group == null) {
            throw new ErrorException(5, "User or group not found");
        }
        user.checkSession(s);
        if(!user.equals(group.getAdmin()) && !user.isAdmin()) {
            throw new ErrorException(3, "Request denied");
        }
        for(User i : group.getUsers()) {
            i.removeGroup(group);
            UserRepository.save(i);
        }
        UserRepository.save(user);
        GroupObjRepository.delete(group);
        return success;
    }

    /**
     * Searches for groups that start with a given string
     * @param name The given string that represents the start of a group name
     * @return A list of groups that start with the provided string
     */
    @ApiOperation(value = "Allows for the search of groups starting with the given name parameter")
    @GetMapping("/group/nameStartsWith/{name}")
    List<GroupObj> getUsersFromNameStartsWith(@PathVariable String name) {
        return GroupObjRepository.findByStartOfName("%"+name.toLowerCase()+"%");
    }


}

