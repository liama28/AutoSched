package com.example.AutoSched.HttpBodies;

import lombok.Data;

@Data
public class SimpleEvent {
    String start;
    String end;
    String name;

    public SimpleEvent(String start, String end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

}
