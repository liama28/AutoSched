package com.example.AutoSched.User;

import com.example.AutoSched.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);

    @Query(value = "SELECT * FROM mydatabase.user WHERE name = ?1", nativeQuery = true)
    User findByUsername(String name);
}

