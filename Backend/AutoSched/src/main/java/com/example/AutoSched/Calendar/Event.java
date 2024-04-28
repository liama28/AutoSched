package com.example.AutoSched.Calendar;

import com.example.AutoSched.User.User;
import lombok.Data;
import java.time.LocalDateTime;
import javax.persistence.*;

@Data
@Entity
public class Event {
    public enum Type {
        WEEKLY, DAILY, MONTHLY, YEARLY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    private int userId;
    private String name;
    // When event starts
    private long start;
    // When event ends
    private long end;

    // Does it repeat?
    private boolean repeats;
    // How does it repeat? (WEEKLY, DAILY, MONTHLY, YEARLY)
    private Type repeatType;
    // End of repeat cycle
    private long repeatEnd;
//    private LocalDateTime trueEnd;
//
    public Event(int userId, long start, long end, String name) {
        this.start = start;
        this.end = end;
        this.repeatEnd = end;
        this.userId = userId;
        this.repeats = false;
        this.name = name;
    }
//
    public Event(int userId, long start, long end, long repeatEnd, Type repeat, String name) {
        this.start = start;
        this.end = end;
        this.repeats = true;
        this.repeatType = repeat;
        this.userId = userId;
        this.repeatEnd = repeatEnd;
        this.name = name;
    }

    public Event() {
        super();
    }

    public boolean repeats() {
        return repeats;
    }



//    private LocalDateTime trueEnd;
//
//    public Event(LocalDateTime start, LocalDateTime end) {
//        this.start = start;
//        this.end = end;
//        this.trueEnd = end;
//    }
//
//    public Event(LocalDateTime start, LocalDateTime end, Repeat repeat) {
//        this.start = start;
//        this.end = end;
//        this.repeat = repeat;
//        this.trueEnd = repeat.getEnd();
//    }
//
//    public boolean repeats() {
//        return repeat != null;
//    }
//
//
//    public boolean equals(Event event) {
//        return event.getId() == this.id;
//    }

}
