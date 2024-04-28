package com.example.isuautosched;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class AddFriendScreenTest {

    @Mock
    NetworkManager manager;

    @InjectMocks
    AddFriendScreen screen;

    @Captor
    ArgumentCaptor<NetworkManager.GetALLUserData> callbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getData() {
        Mockito.doNothing().when(manager).getAllData(callbackCaptor.capture());
        //screen.getData();
        Mockito.verify(manager).getAllData(callbackCaptor.getValue());
    }

    @Test
    public void showResults() {
    }
}