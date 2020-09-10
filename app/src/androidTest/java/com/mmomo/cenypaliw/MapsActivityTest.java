package com.mmomo.cenypaliw;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MapsActivityTest {

    private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseAccess databaseTest;
    List<GasStation> testList;
    @Before
    public void setUp() throws Exception {
        databaseTest=DatabaseAccess.getInstance(context);
        testList=databaseTest.getStationList();
    }

    @Test
    public void getStationListTest(){
        Log.i("Tested station: ","Name: "+testList.get(4852).getName()+" ID: "+testList.get(4852).getID()+" Lat: "+testList.get(4852).getLat() + " Lng: " + testList.get(4852).getLng());
        assertNotNull(testList.get(8000).getLng());
    }

    @After
    public void tearDown() throws Exception {
        databaseTest.closeConnection();
        databaseTest=null;
        testList=null;
    }
}