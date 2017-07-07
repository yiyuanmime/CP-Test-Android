package com.cp.user.preference;

import android.location.Location;

import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by yi on 07/07/2017.
 */

@SharedPreferences(name = "device_preferences", mode = SharedPreferenceMode.PRIVATE)
public interface DevicePreference extends SharedPreferenceActions {

    public Location lastKnownLocation();

    public void lastKnownLocation(Location location);
}
