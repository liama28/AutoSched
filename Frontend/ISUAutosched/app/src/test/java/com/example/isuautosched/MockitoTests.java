package com.example.isuautosched;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockitoTests {

    @Test
    public void checkListElement() {
        List<String> list = new ArrayList<String>();
        List<String> spiedList = spy(list); // create a spy version of original list so we can mock any methods

         // return 42 when the spied list's get method is called with 99 value
        doReturn("42").when(spiedList).get(99);
        String value = (String) spiedList.get(99); // get the value from spied list

        assertEquals("42", value); // check the vale matches the one we expect
    }


    @Test
    public void checkListSize() {
        List<String> list = new ArrayList<String>();
        List<String> spiedList = spy(list); // create a spy version of original list so we can mock any methods

            // return 10 when the spied list's size method is called
        doReturn(10).when(spiedList).size();
        int  size =  spiedList.size(); // get the list size

        assertEquals(10, size); // check the value matches the one we expect
    }
}
