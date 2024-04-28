package com.experiment3.jpa.controller;

import com.experiment3.jpa.dto.GroupRequest;
import com.experiment3.jpa.entity.Groupies;
import com.experiment3.jpa.repository.GroupRepository;
import com.experiment3.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createGroup")
    public Groupies createGroup(@RequestBody GroupRequest request){
        return groupRepository.save(request.getGroup());
    }

    @GetMapping("/findAllGroups")
    public List<Groupies> findAllGroups(){
        return groupRepository.findAll();
    }
}
