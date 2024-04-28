package com.example.AutoSched.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepo;


    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void save(User user) {

        userRepo.save(user);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public User findById(int i) {
        return userRepo.findById(i);
    }

    public User findByUsername(String s) {
        return userRepo.findByUsername(s);
    }

    public List<User> findByStartOfName(String s) {
        return userRepo.findByStartOfName(s);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
}
