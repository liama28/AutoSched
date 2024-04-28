package com.example.AutoSched.User;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/getAllUserData")
    public List<User> getAllData() {
        return userRepository.findAll();
    }

    @PostMapping(value = "saveUser")
    public User saveUser(@RequestBody User u) {
        return userRepository.save(u);
    }
}

