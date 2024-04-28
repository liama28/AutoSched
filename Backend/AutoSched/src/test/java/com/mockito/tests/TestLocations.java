package com.mockito.tests;

import com.example.AutoSched.GroupObj.GroupObj;
import com.example.AutoSched.GroupObj.GroupObjController;
import com.example.AutoSched.GroupObj.GroupObjService;
import com.example.AutoSched.HttpBodies.SessionId;
import com.example.AutoSched.User.User;
import com.example.AutoSched.User.UserController;
import com.example.AutoSched.User.UserService;
import com.example.AutoSched.excel.ExcelController;
import com.example.AutoSched.excel.ExcelService;
import com.example.AutoSched.locations.Locations;
import com.example.AutoSched.locations.MainController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TestLocations {

    @InjectMocks
    MainController locController;

    @Mock
    Locations locations;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void addLocation() {
        locations = mock(Locations.class);
        locations.setId(2);
        locations.setLat(42.02844584772908);
        locations.setLon(-93.65068444792863);
        List<Locations> locationsList = new ArrayList<>();
        locationsList.add(locations);
        when(locations.getLat()).thenReturn(42.02844584772908);
        when(locations.getLon()).thenReturn(-93.65068444792863);

        assertEquals(1, locationsList.size());
        assertEquals(42.02844584772908, locations.getLat(), .0001);
        assertEquals(-93.65068444792863, locations.getLon(), .0001);
    }


}
