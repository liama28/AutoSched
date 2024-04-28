package com.example.AutoSched.GroupLink;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupLinkController {

    @Autowired
    GroupLinkRepository GroupLinkRepository;

    @GetMapping(value = "/getAllGroupLinkData")
    public List<GroupLink> getAllGroupLinkData() {
        return GroupLinkRepository.findAll();
    }

    @PostMapping(value = "saveGroupLink")
    public GroupLink saveGroupLink(@RequestBody GroupLink u) {
        return GroupLinkRepository.save(u);
    }
}

