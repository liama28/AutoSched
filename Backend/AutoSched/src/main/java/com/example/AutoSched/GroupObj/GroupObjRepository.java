package com.example.AutoSched.GroupObj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Used to query groups in the mysql database
 */
public interface GroupObjRepository extends JpaRepository<GroupObj, Integer> {
    /**
     * Find group by id
     * @param id Group id to find
     * @return GroupObj with given id
     */
    GroupObj findById(int id);

    /**
     * Finds group by name
     * @param name Group name to find
     * @return GroupObj with given name
     */
    @Query(value = "SELECT * FROM mydatabase.group_obj WHERE name = ?1", nativeQuery = true)
    GroupObj findByGroupname(String name);

    /**
     * Find group that starts with a given string
     * @param name String of group that starts with
     * @return A list of groups whose name starts with string name
     */
    @Query(value = "SELECT * FROM mydatabase.group_obj WHERE name like ?1", nativeQuery = true)
    List<GroupObj> findByStartOfName(String name);
}

