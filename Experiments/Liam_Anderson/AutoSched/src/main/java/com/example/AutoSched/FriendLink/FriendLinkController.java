package com.example.AutoSched.FriendLink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendLinkController {

    @Autowired
    FriendLinkRepository FriendLinkRepository;

    @GetMapping(value = "/getAllFriendLinkData")
    public List<FriendLink> getAllData() {
        return FriendLinkRepository.findAll();
    }

    @PostMapping(value = "saveFriendLink")
    public FriendLink saveFriendLink(@RequestBody FriendLink u) {
        return FriendLinkRepository.save(u);
    }
}

