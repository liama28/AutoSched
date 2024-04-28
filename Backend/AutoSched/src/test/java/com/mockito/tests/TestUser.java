package com.mockito.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.GroupObj.GroupObjService;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.User.UserController;
import com.example.AutoSched.User.UserService;
import com.example.AutoSched.User.User;
import com.mysql.cj.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TestUser {
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addFriend() throws NoSuchAlgorithmException {
        User user = new User(1, "liam", "liam_password");
        user.hashPassword(user.getPassword());
        Boolean b = user.addFriend(user);
        assertEquals(false, b);
        User userFriend1 = new User(2, "vincent", "vincent_password");
        userFriend1.hashPassword(user.getPassword());
        User userFriend2 = new User(3, "chase", "chase_password");
        userFriend2.hashPassword(user.getPassword());
        b = user.addFriend(userFriend2);
        assertEquals(true, b);
        b = user.addFriend(userFriend1);
        assertEquals(true , b);
        assertEquals(user.getFriends().get(0).getId(), userFriend1.getId());
        assertEquals(user.getFriends().get(1).getId(), userFriend2.getId());
        b = user.addFriend(userFriend1);
        assertEquals(false, b);
    }

    @Test
    public void addGroup() throws NoSuchAlgorithmException {
        User user = new User(1, "liam", "liam_password");
        user.hashPassword(user.getPassword());
        GroupObj group = mock(GroupObj.class);
        when(group.getId()).thenReturn(2);
        when(group.equals(group)).thenReturn(true);
        Boolean b = user.addGroup(group);
        assertEquals(true, b);
        assertEquals(group.getId(),user.getGroupobjs().get(0).getId());
        b = user.addGroup(group);
        assertEquals(false, b);
        assertEquals(1, user.getGroupobjs().size());
    }
}
