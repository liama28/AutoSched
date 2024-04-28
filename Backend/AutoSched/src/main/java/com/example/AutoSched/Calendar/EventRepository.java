package com.example.AutoSched.Calendar;

import com.example.AutoSched.GroupObj.GroupObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    /**
     * Find group by id
     *
     * @param id Event id to find
     * @return Event with given id
     */
    Event findById(int id);

    @Query(value = "SELECT * FROM mydatabase.event WHERE user_id = ?1 AND start <= ?3 AND repeat_end >= ?2", nativeQuery = true)
    List<Event> findByRange(int id, long start, long end);

}