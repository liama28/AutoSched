package com.example.AutoSched.GroupObj;


import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.acl.Group;
import java.util.List;

@RestController
public class GroupObjController {

    @Autowired
    GroupObjRepository GroupObjRepository;

    @Autowired
    UserRepository UserRepository;

    private String failure = "{\"message\":\"failure\"}";
    private String success = "{\"message\":\"success\"}";

    // Returns all groups and their users in an JSON with an array of objects
    @GetMapping(value = "/group/getAllData")
    public List<GroupObj> getAllData() {
        return GroupObjRepository.findAll();
    }

    // Takes a JSON http body and creates a new group. Checks to see if a name is given and if the name is taken
    // Body example:
    // {
    //      "name": "2_hb_1"
    // {
    @PostMapping(value = "/group/save")
    public GroupObj saveGroup(@RequestBody GroupObj u) {
        if(u.getName() == null) {
            return null;
        }
        u.setName(u.getName().toLowerCase());
        if(GroupObjRepository.findByGroupname(u.getName()) != null) {
            return null;
        }
        return GroupObjRepository.save(u);
    }

    // Given the group id and user id in the path, this method adds a new user to the group's user
    // list and adds the group to the users group list
    @PutMapping("/group/{groupId}/add/{userId}")
    String addUserToGroup(@PathVariable int groupId,@PathVariable int userId){
        User user = UserRepository.findById(userId);
        GroupObj group = GroupObjRepository.findById(groupId);
        if(user == null || group == null) {
            return failure;
        }
        group.addUser(user);
        user.addGroup(group);
        GroupObjRepository.save(group);
        UserRepository.save(user);
        return success;
    }

    // Given the group id and user id, this method sets the admin of the group to the user
    @PutMapping("/group/{groupId}/setAdmin/{userId}")
    GroupObj addAdminToGroup(@PathVariable int groupId, int userId){
        User user = UserRepository.findById(userId);
        GroupObj group = GroupObjRepository.findById(groupId);
        group.setAdmin(user);
        return GroupObjRepository.save(group);
    }

}

