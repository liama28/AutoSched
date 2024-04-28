package com.mockito.tests;


import com.example.AutoSched.Calendar.Calendar;
import com.example.AutoSched.Calendar.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCalendar {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calendar() {
//        Event event1 = mock(Event.class);
//        Event event2 = mock(Event.class);
//        Event event3 = mock(Event.class);
//        Event event4 = mock(Event.class);
//        Event event5 = mock(Event.class);
//        Event event6 = mock(Event.class);
//        Event event7 = mock(Event.class);
//        Event event8 = mock(Event.class);
//        Event event9 = mock(Event.class);
//        Event event10 = mock(Event.class);
//        Event event11 = mock(Event.class);
//        Event event12 = mock(Event.class);
//        when(event1.getId()).thenReturn(1);
//        when(event1.equals(event1)).thenReturn(true);
//        when(event2.equals(event2)).thenReturn(true);
//        when(event3.equals(event3)).thenReturn(true);
//        when(event4.equals(event4)).thenReturn(true);
//        when(event5.equals(event5)).thenReturn(true);
//        when(event6.equals(event6)).thenReturn(true);
//        when(event7.equals(event7)).thenReturn(true);
//        when(event8.equals(event8)).thenReturn(true);
//        when(event9.equals(event9)).thenReturn(true);
//        when(event10.equals(event10)).thenReturn(true);
//        when(event11.equals(event11)).thenReturn(true);
//        when(event12.equals(event12)).thenReturn(true);
//        when(event2.getId()).thenReturn(2);
//        when(event3.getId()).thenReturn(3);
//        when(event4.getId()).thenReturn(4);
//        when(event5.getId()).thenReturn(5);
//        when(event6.getId()).thenReturn(6);
//        when(event7.getId()).thenReturn(7);
//        when(event8.getId()).thenReturn(8);
//        when(event9.getId()).thenReturn(9);
//        when(event10.getId()).thenReturn(10);
//        when(event11.getId()).thenReturn(11);
//        when(event12.getId()).thenReturn(12);
//        Calendar calendar = new Calendar();
//        // Add events out of order
//        calendar.addEvent(event7);
//        calendar.addEvent(event2);
//        calendar.addEvent(event9);
//        calendar.addEvent(event3);
//        calendar.addEvent(event12);
//        calendar.addEvent(event4);
//        calendar.addEvent(event5);
//        calendar.addEvent(event11);
//        calendar.addEvent(event6);
//        calendar.addEvent(event1);
//        calendar.addEvent(event8);
//        calendar.addEvent(event10);
//        List<Event> list = calendar.getEvents();
//        // Check to see if list is sorted correctly by end time
//        for(int i = 0; i < 12; i++) {
//            assertEquals(i+1, list.get(i).getId());
//        }
//        // Test if delEvent function workds
//        calendar.delEvent(event10);
//        assertEquals(11, calendar.getEvents().get(9).getId());

    }
}
