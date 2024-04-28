package com.example.AutoSched.User;

import com.example.AutoSched.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Used to query the mysql database for users
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds user by id
     * @param id unique user identification integer
     * @return User with given id
     */
    User findById(int id);

    /**
     * Find user by username
     * @param name unique username
     * @return User with username
     */
    @Query(value = "SELECT * FROM mydatabase.user WHERE name = ?1", nativeQuery = true)
    User findByUsername(String name);

    /**
     * Finds users by start of username
     * @param name start of username string
     * @return A list of users with start of username
     */
    @Query(value = "SELECT * FROM mydatabase.user WHERE name like ?1", nativeQuery = true)
    List<User> findByStartOfName(String name);

    /**
     *
     * @return All users in the database
     */
    List<User> findAll();
}

