package com.mockito.tests;

import static org.junit.Assert.assertEquals;

import com.example.AutoSched.User.User;
//import jdk.jfr.internal.Repository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.example.AutoSched.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

public class TestUserQuattrone {

    private User mockUser = Mockito.mock(User.class);

    private User vince = new User("vince", "password123");



    @Test
    public void testSetGetUsers() {
        Mockito.when(mockUser.getId()).thenReturn(1);
        assertEquals(1,mockUser.getId());
    }

    @Test
    public void testUsers() {

        Mockito.when(mockUser.getName()).thenReturn("vince");
        assertEquals(mockUser.getName(), "vince");
    }
    @Test
    public void testUser() {

        List<User> friends = new ArrayList<User>();
        friends.add(vince);

        Mockito.when(mockUser.getFriends()).thenReturn(friends);
        assertEquals(friends, mockUser.getFriends());

    }

}

