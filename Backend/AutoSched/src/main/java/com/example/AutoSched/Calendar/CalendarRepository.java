package com.example.AutoSched.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    /**
     * Find group by id
     *
     * @param id Calendar id to find
     * @return Calendar with given id
     */
    Calendar findById(int id);


}