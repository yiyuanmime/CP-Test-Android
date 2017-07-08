package com.cp.user.mvp;

import android.location.Location;

/**
 * Created by yi on 08/07/2017.
 */

public interface MainView {

    void setCurrentLocation(Location currentLocation);

    void updateMap(Location location);

    void onLocationChanged(Location location);

    void noPermissionGranted();

}
