package com.example.AutoSched.Calendar;

import com.example.AutoSched.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;

    @OneToMany
    private List<Event> events = new ArrayList<>();


    public Calendar() {
    }

    private int binarySearch(List<Event> list, int l, int r, Event event) {
        if (r >= l) {
            int mid = l + (r - l) / 2;
            if (list.get(mid).equals(event)) {
                return mid;
            }
            if (list.get(mid).getId() > event.getId()) {
                return binarySearch(list, l, mid - 1, event);
            }
            return binarySearch(list,mid + 1, r, event);
        }
        return l;
    }
//
    public void addEvent(Event event) {
        if(events.size() == 0) {
            events.add(event);
            return;
        }
        int i = binarySearch(events, 0, events.size()-1, event);
        if(i == events.size()) {
            events.add(event);
            return;
        }
        if(event.getId() > events.get(i).getId()) {
            events.add(i+1, event);
            return;
        }
        events.add(i, event);
    }

        public boolean delEvent(Event event) {
        if(events.size() == 0) {
            return false;
        }
        int i = binarySearch(events,0, events.size()-1, event);
        if(i == events.size()) {
            return false;
        }
        if(events.get(i).equals(event)) {
            events.remove(i);
            return true;
        }
        return false;
    }
















    /*
    Experiment with end sorted events. Going to try id sorted events with specific mysql queries to grab events in data range
     */
//    public void addEvent(Event event) {
//        if(events.size() == 0) {
//            events.add(event);
//            return;
//        }
//        int i = endBinarySearch(events, 0, events.size()-1, event.trueEnd());
//        if(i == events.size()) {
//            events.add(event);
//            return;
//        }
//        if(event.trueEnd() > events.get(i).trueEnd()) {
//            events.add(i+1, event);
//            return;
//        }
//        events.add(i, event);
//    }
//
//    public boolean delEvent(Event event) {
//        if(events.size() == 0) {
//            return false;
//        }
//        int i = endBinarySearch(events,0, events.size()-1, event.trueEnd());
//        if(i == events.size()) {
//            return false;
//        }
//        i = findLeftMostEndIndex(i);
//        for(int j = i; j < events.size(); j++) {
//            if(events.get(j).trueEnd() != event.trueEnd()) {
//                return false;
//            }
//            if(events.get(j).getId() == event.getId()) {
//                events.remove(j);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private int endBinarySearch(List<Event> list, int l, int r, long end) {
//        if (r >= l) {
//            int mid = l + (r - l) / 2;
//            if (list.get(mid).trueEnd() == end) {
//                return mid;
//            }
//            if (list.get(mid).trueEnd() > end) {
//                return endBinarySearch(list, l, mid - 1, end);
//            }
//            return endBinarySearch(list,mid + 1, r, end);
//        }
//        return l;
//    }

//    public List<Event> findInEndRange(long l, long r) {
//        int lIndex = endBinarySearch(events, 0, events.size()-1, l);
//        if(lIndex == events.size()) {
//            lIndex -=1;
//        }
//        int rIndex = endBinarySearch(events, 0, events.size()-1, r);
//        if(rIndex == events.size()) {
//            rIndex -=1;
//        }
//        lIndex = findLeftMostEndIndex(lIndex);
//        rIndex = findRightMostEndIndex(rIndex);
//        return events.subList(lIndex, rIndex+1);
//    }
//
//    private int findLeftMostEndIndex(int i) {
//        for(int j = i; j > -1; j--) {
//            if(events.get(j).trueEnd() != events.get(i).trueEnd()) {
//                return j+1;
//            }
//        }
//        return 0;
//    }
//
//    private int findRightMostEndIndex(int i) {
//        for(int j = i; j < events.size(); j++) {
//            if (events.get(j).trueEnd() != events.get(i).trueEnd()) {
//                return j-1;
//            }
//        }
//        return events.size()-1;
//    }



}
