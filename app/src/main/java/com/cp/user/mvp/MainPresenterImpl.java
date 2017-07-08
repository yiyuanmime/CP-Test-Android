package com.cp.user.mvp;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;

import com.cp.user.CPApplication;
import com.cp.user.model.HistoryAddress;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yi on 08/07/2017.
 */

@SuppressWarnings({"MissingPermission"})
public class MainPresenterImpl implements MainPresenter, PermissionsListener {

    private Activity activity;
    private MainView mainView;

    private PermissionsManager permissionsManager;

    private LocationEngine locationEngine;
    private LocationEngineListener locationListener;


    public MainPresenterImpl(@NonNull Activity activity, @NonNull MainView mainView) {
        this.activity = activity;
        this.mainView = mainView;
        initLocationService();
    }

    @Override
    public void initLocation() {
        if (!PermissionsManager.areLocationPermissionsGranted(activity)) {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(activity);
        } else {
            enableLocation(locationEngine);
        }
    }

    @Override
    public void updateHistory(String address, double latitude, double longitude) {

        HistoryAddress historyAddress = new HistoryAddress();
        historyAddress.setAddress(address);
        historyAddress.setLatitude(latitude);
        historyAddress.setLongitude(longitude);

        List<HistoryAddress> list = CPApplication.getDevicePreference().historyAddress();

        if (list == null)
            list = new ArrayList<>();

        list.add(historyAddress);

        if (list.size() > 15)
            list.remove(0);

        CPApplication.getDevicePreference().historyAddress(list);

        mainView.onHistoryUpdated(list);
    }

    @Override
    public void updateHistory(HistoryAddress historyAddress) {

        List<HistoryAddress> list = CPApplication.getDevicePreference().historyAddress();

        if (list == null)
            list = new ArrayList<>();

        list.add(historyAddress);

        if (list.size() > 15)
            list.remove(0);

        CPApplication.getDevicePreference().historyAddress(list);

        mainView.onHistoryUpdated(list);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation(locationEngine);
        } else {
            mainView.noPermissionGranted();
        }
    }

    private void initLocationService() {
        locationEngine = new LocationSource(activity);
        locationEngine.activate();
        if (locationEngine != null && locationListener != null) {
            locationEngine.activate();
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(locationListener);
        }

    }

    private void enableLocation(LocationEngine locationEngine) {
        // If we have the last location of the user, we can move the camera to that position.
        Location lastLocation = locationEngine.getLastLocation();

        if (lastLocation != null)
            mainView.setCurrentLocation(lastLocation);

        locationListener = new LocationEngineListener() {
            @Override
            public void onConnected() {
                locationEngine.requestLocationUpdates();
            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    mainView.onLocationChanged(location);
                    locationEngine.removeLocationEngineListener(this);
                }
            }
        };
        locationEngine.addLocationEngineListener(locationListener);
    }

}
