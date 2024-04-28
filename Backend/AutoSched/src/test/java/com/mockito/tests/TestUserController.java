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


public class TestUserController {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    GroupObjService groupObjService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllData() {
        List<User> list = new ArrayList<User>();
        list.add(mock(User.class));
        list.add(mock(User.class));
        list.add(mock(User.class));
        when(userService.findAll()).thenReturn(list);
        List<User> list1 = userController.getAllData();
        assertEquals(3, list1.size());
    }

    @Test
    public void delUser() throws NoSuchAlgorithmException {
        User user = mock(User.class);
        SessionId sessionId = mock(SessionId.class);
        when(sessionId.getUserid()).thenReturn(1);
        when(userService.findById(1)).thenReturn(user);
        when(user.equals(user)).thenReturn(true);
        when(user.isAdmin()).thenReturn(false);
        when(user.getFriends()).thenReturn(new ArrayList<>());
        when(user.getSentRequests()).thenReturn(new ArrayList<>());
        when(user.getGroupobjs()).thenReturn(new ArrayList<>());
        int i = 0;
        String str = userController.delUser(1,sessionId);
        assertEquals("{\"code\":0,\"message\":\"success\"}", str);
        verify(userService, times(2)).findById(1);
        verify(userService, times(1)).delete(user);
    }

    @Test
    public void addFriend() {
        User user = mock(User.class);
        User userFriend = mock(User.class);
        SessionId sessionId = mock(SessionId.class);
        when(sessionId.getUserid()).thenReturn(1);
        when(userService.findById(1)).thenReturn(user);
        when(userService.findById(2)).thenReturn(userFriend);
        when(user.acceptRequest(userFriend)).thenReturn(false);
        when(user.addSentRequest(userFriend)).thenReturn(true);
        when(userFriend.addFriendRequest(user)).thenReturn(true);
        String str = userController.addFriend(2, sessionId);
        verify(userService).save(user);
        verify(userService).save(userFriend);
        assertEquals("{\"code\":0,\"message\":\"success\"}", str);
    }

    @Test
    public void addToGroup() {
        User user = mock(User.class);
        SessionId sessionId = mock(SessionId.class);
        when(sessionId.getUserid()).thenReturn(1);
        when(userService.findById(1)).thenReturn(user);
        GroupObj group = mock(GroupObj.class);
        when(groupObjService.findById(2)).thenReturn(group);
        String str = userController.addToGroup(2, sessionId);
        assertEquals("{\"code\":0,\"message\":\"success\"}", str);
        verify(userService).save(user);
        verify(groupObjService).save(group);
        verify(group).addUser(user);
        verify(user).addGroup(group);
        verify(user).checkSession(sessionId);
    }


}
