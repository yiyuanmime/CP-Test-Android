package com.cp.user.mvp;

import android.location.Location;

import com.cp.user.model.HistoryAddress;

import java.util.List;

/**
 * Created by yi on 08/07/2017.
 */

public interface MainView {

    void setCurrentLocation(Location currentLocation);

    void onLocationChanged(Location location);

    void noPermissionGranted();

    void onHistoryUpdated(List<HistoryAddress> list);

}
