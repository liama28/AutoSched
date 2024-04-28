package com.example.AutoSched.Calendar;

import com.example.AutoSched.HttpBodies.EventBody;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.HttpBodies.SimpleEvent;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserRepository;
import com.example.AutoSched.exception.ErrorException;
import com.example.AutoSched.locations.Locations;
import javassist.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/calendar/save")
    public Event saveEvent(@RequestBody EventBody e) {
        User user = userRepository.findById(e.getSessionId().getUserid());
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        Calendar calendar = user.getCalendar();
        user.checkSession(e.getSessionId());
        Event event = e.getEvent();
        event.setUserId(user.getId());
        if(!event.isRepeats()) {
            event.setRepeatEnd(event.getEnd());
        }
        eventRepository.save(event);
        calendar.addEvent(event);
        calendarRepository.save(calendar);
        return event;
    }

    @PostMapping("/calendar/events")
    public List<Event> getEvents(@RequestBody SessionId s) {
        User user = userRepository.findById(s.getUserid());
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        Calendar calendar = user.getCalendar();
        user.checkSession(s);
        return calendar.getEvents();
    }

    @PostMapping("/calendar/range/{from}/{to}")
    public List<Event> getEventsInRange(@RequestBody SessionId s, @PathVariable long from, @PathVariable long to) {
        User user = userRepository.findById(s.getUserid());
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        return eventRepository.findByRange(user.getId(), from, to);
    }

    @GetMapping("calendar/epoch/{time}")
    public LocalDateTime test2(@PathVariable long time) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.of("-06:00"));
        return dateTime;
    }

    @PostMapping("/calendar/day/{epochTime}")
    public List<SimpleEvent> getEventsOnDay(@RequestBody SessionId s, @PathVariable long epochTime) {
        User user = userRepository.findById(s.getUserid());
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        user.checkSession(s);
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.of("-06:00"));
        LocalDateTime beginTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0);
        System.out.println(user.getId() + " " +  beginTime.toString()+ " : " + beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + " " + (beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + 86400));

        List<Event> events = eventRepository.findByRange(user.getId(), beginTime.toEpochSecond(ZoneOffset.of("-06:00")), beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + 86400);
        System.out.println(events);
        List<SimpleEvent> tempEvents = new ArrayList<>();
        for(Event i : events) {
            LocalDateTime tempStart = LocalDateTime.ofEpochSecond(i.getStart(), 0, ZoneOffset.of("-06:00"));
            LocalDateTime tempEnd = LocalDateTime.ofEpochSecond(i.getEnd(), 0, ZoneOffset.of("-06:00"));
            SimpleEvent tempSimple = null;
            if(i.repeats()) {
                if(i.getRepeatType() == Event.Type.DAILY || (i.getRepeatType() == Event.Type.WEEKLY && tempStart.getDayOfWeek() == dateTime.getDayOfWeek())
                    || (i.getRepeatType() == Event.Type.MONTHLY && tempStart.getDayOfMonth() == dateTime.getDayOfMonth()) || (i.getRepeatType() == Event.Type.YEARLY &&
                        tempStart.getDayOfYear() == tempStart.getDayOfYear())) {
                    LocalDateTime dailyTempStart = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), tempStart.getHour(), tempStart.getMinute());
                    LocalDateTime dailyTempEnd =   LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), tempEnd.getHour(), tempEnd.getMinute());
                    tempSimple = new SimpleEvent(dailyTempStart.toString(), dailyTempEnd.toString(), i.getName());
                }
            }
            else {
                tempSimple = new SimpleEvent(tempStart.toString(), tempEnd.toString(), i.getName());
            }
            tempEvents.add(tempSimple);

        }
        return tempEvents;
    }


    @GetMapping("/calendar/test")
    public EventBody test() {
        EventBody eventBody = new EventBody();
        SessionId sessionId = new SessionId(1, 2);
        eventBody.setSessionId(sessionId);
        Event event = new Event(1, 100, 100, 200, Event.Type.DAILY, "COM309");
        eventBody.setEvent(event);
        return eventBody;
    }

    @DeleteMapping("/calendar/del/{eventId}")
    public String delEvent(@RequestBody SessionId s, @PathVariable int eventId) {
        User user = userRepository.findById(s.getUserid());
        if(user == null) {
            throw new ErrorException(5, "User not found");
        }
        Event event = eventRepository.findById(eventId);
        if(event == null) {
            throw new ErrorException(5, "Event not found");
        }
        user.checkSession(s);
        if(user.getCalendar().delEvent(event)) {
            calendarRepository.save(user.getCalendar());
            eventRepository.delete(event);
            return "{\"code\":0,\"message\":\"success\"}";
        }
        throw new ErrorException(1, "Error");
    }

//    @PostMapping("/calendar/day/{epochTime}/{groupId}")
//    public List<SimpleEvent> getEventsGroup(@RequestBody SessionId s, @PathVariable long epochTime, ) {
//        User user = userRepository.findById(s.getUserid());
//        Group group = group
//        if(user == null) {
//            throw new ErrorException(5, "User not found");
//        }
//        user.checkSession(s);
//
//        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(epochTime, 0, ZoneOffset.of("-06:00"));
//        LocalDateTime beginTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0);
//        System.out.println(user.getId() + " " +  beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + " " + (beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + 86400));
//        List<Event> events = eventRepository.findByRange(user.getId(), beginTime.toEpochSecond(ZoneOffset.of("-06:00")), beginTime.toEpochSecond(ZoneOffset.of("-06:00")) + 86400);
//        System.out.println(events);
//        List<SimpleEvent> tempEvents = new ArrayList<>();
//        for(Event i : events) {
//            LocalDateTime tempStart = LocalDateTime.ofEpochSecond(i.getStart(), 0, ZoneOffset.of("-06:00"));
//            LocalDateTime tempEnd = LocalDateTime.ofEpochSecond(i.getEnd(), 0, ZoneOffset.of("-06:00"));
//            SimpleEvent tempSimple = null;
//            if(i.repeats()) {
//                if(i.getRepeatType() == Event.Type.DAILY || (i.getRepeatType() == Event.Type.WEEKLY && tempStart.getDayOfWeek() == dateTime.getDayOfWeek())
//                        || (i.getRepeatType() == Event.Type.MONTHLY && tempStart.getDayOfMonth() == dateTime.getDayOfMonth()) || (i.getRepeatType() == Event.Type.YEARLY &&
//                        tempStart.getDayOfYear() == tempStart.getDayOfYear())) {
//                    LocalDateTime dailyTempStart = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), tempStart.getHour(), tempStart.getMinute());
//                    LocalDateTime dailyTempEnd =   LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), tempEnd.getHour(), tempEnd.getMinute());
//                    tempSimple = new SimpleEvent(dailyTempStart.toString(), dailyTempEnd.toString(), i.getName());
//                }
//            }
//            else {
//                tempSimple = new SimpleEvent(tempStart.toString(), tempEnd.toString(), i.getName());
//            }
//            tempEvents.add(tempSimple);
//
//        }
//        return tempEvents;
//    }
}
