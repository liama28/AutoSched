package com.example.AutoSched.GroupObj;

import com.example.AutoSched.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupObjRepository extends JpaRepository<GroupObj, Integer> {
    GroupObj findById(int id);

    @Query(value = "SELECT * FROM mydatabase.group_obj WHERE name = ?1", nativeQuery = true)
    GroupObj findByGroupname(String name);
}

