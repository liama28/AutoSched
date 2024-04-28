package com.mockito.tests;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.GroupObj.GroupObjController;
import com.example.AutoSched.GroupObj.GroupObjService;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserController;
import com.example.AutoSched.User.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TestControllerQuattrone {

    @InjectMocks
    GroupObjController groupObjController;

    @Mock
    UserService userService;

    @Mock
    GroupObjService groupObjService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void getUserFromName() {
        User vince = mock(User.class);
        when(userService.findByUsername("vince")).thenReturn(vince);
        assertEquals(vince, userService.findByUsername("vince"));
    }

    @Test
    public void addAdminToGroup() {
        User u = mock(User.class);
        SessionId sId = mock(SessionId.class);
        when(sId.getUserid()).thenReturn(1);
        when(userService.findById(1)).thenReturn(u);
        when(u.isAdmin()).thenReturn(true);
        GroupObj g = mock(GroupObj.class);
        when(groupObjService.findById(2)).thenReturn(g);
        String str = groupObjController.addAdminToGroup(2,1,sId);
        assertEquals(true, u.isAdmin());
        assertEquals("{\"code\":0,\"message\":\"success\"}", str);

    }

}
