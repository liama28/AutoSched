package com.example.AutoSched.User;

import com.example.AutoSched.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}

