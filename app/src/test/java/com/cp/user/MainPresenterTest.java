package com.cp.user;

import android.location.Location;

import com.cp.user.model.HistoryAddress;
import com.cp.user.mvp.MainView;
import com.mapbox.services.android.telemetry.location.LocationEngine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@SuppressWarnings({"MissingPermission"})
public class MainPresenterTest {

    @Mock
    private MainView mainView;

    @Mock
    private LocationEngine locationEngine;

    @Mock
    private Location location;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void initLocation_isOK() throws Exception {
        Mockito.when(locationEngine.isConnected()).thenReturn(true);
        assertEquals(true, locationEngine.isConnected());
    }

    @Test
    public void initLocation_isFailed() throws Exception {
        Mockito.when(locationEngine.isConnected()).thenReturn(false);
        assertEquals(false, locationEngine.isConnected());
    }

    @Test
    public void updateHistory() throws Exception {
        HistoryAddress historyAddress = new HistoryAddress();
        historyAddress.setAddress("test");
        historyAddress.setLatitude(0.0);
        historyAddress.setLongitude(0.0);

        Assert.assertNotNull(historyAddress);

        List<HistoryAddress> list = new ArrayList<>();
        list.add(historyAddress);

        mainView.onHistoryUpdated(list);
        Mockito.verify(mainView, Mockito.times(1)).onHistoryUpdated(list);
    }

}

