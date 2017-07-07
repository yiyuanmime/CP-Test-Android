package com.cp.user;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

/**
 * Created by yi on 07/07/2017.
 */

public class CPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Mapbox Access token
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoieWl5dWFuIiwiYSI6ImNqNHR4Y3VxeTA5Z3Uyd29heW5oc2FqZXUifQ.h751wN1FiGA1sj1hchZ2fw");
    }

}
