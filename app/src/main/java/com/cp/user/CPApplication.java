package com.cp.user;

import android.app.Application;
import android.content.Context;

import com.cp.user.preference.DevicePreference;
import com.cp.user.utils.GsonSerializer;
import com.mapbox.mapboxsdk.Mapbox;

import de.devland.esperandro.Esperandro;

/**
 * Created by yi on 07/07/2017.
 */

public class CPApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoieWl5dWFuIiwiYSI6ImNqNHR4Y3VxeTA5Z3Uyd29heW5oc2FqZXUifQ.h751wN1FiGA1sj1hchZ2fw");
    }

    /**
     * Device preferences access
     */

    private static DevicePreference devicePreference;

    public static DevicePreference getDevicePreference() {

        if (devicePreference == null) {
            Esperandro.setSerializer(new GsonSerializer());
            devicePreference = Esperandro.getPreferences(DevicePreference.class, CPApplication.getContext());
        }

        return devicePreference;
    }

    public static Context getContext() {
        return sContext;
    }

}
