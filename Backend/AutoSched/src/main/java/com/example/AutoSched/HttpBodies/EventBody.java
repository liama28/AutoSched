package com.example.AutoSched.HttpBodies;

import com.example.AutoSched.Calendar.Event;
import lombok.Data;

@Data
public class EventBody {
    private SessionId sessionId;
    private Event event;
}
