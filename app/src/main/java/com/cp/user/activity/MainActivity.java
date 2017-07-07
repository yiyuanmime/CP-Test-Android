package com.cp.user.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cp.user.R;
import com.cp.user.bus.ApplicationBus;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationSource;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yi on 07/07/2017.
 */

@SuppressWarnings({"MissingPermission"})
public class MainActivity extends AppCompatActivity implements PermissionsListener {

    private MarkerView userMarker;
    private MapboxMap map;

    private LocationEngine locationEngine;
    private LocationEngineListener locationListener;

    private PermissionsManager permissionsManager;

    @BindView(R.id.mapView)
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        locationEngine = new LocationSource(this);
        locationEngine.activate();

        mapView.getMapAsync(mapboxMap -> {
            map = mapboxMap;
            userMarker = mapboxMap.addMarker(
                    new MarkerViewOptions()
                            .position(new LatLng(0, 0))
                            .anchor(0.5f, 0.5f), markerView -> {
                        // Check if user has granted location permission
                        if (!PermissionsManager.areLocationPermissionsGranted(MainActivity.this)) {
                            permissionsManager = new PermissionsManager(MainActivity.this);
                            permissionsManager.requestLocationPermissions(MainActivity.this);
                        } else {
                            enableLocation();
                        }

                    });

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        if (locationEngine != null && locationListener != null) {
            locationEngine.activate();
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(locationListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        ApplicationBus.registerContext(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        ApplicationBus.unRegisterContext(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void enableLocation() {
        // If we have the last location of the user, we can move the camera to that position.
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));
            userMarker.setPosition(new LatLng(lastLocation));
        }

        locationListener = new LocationEngineListener() {
            @Override
            public void onConnected() {
                locationEngine.requestLocationUpdates();
            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
                    locationEngine.removeLocationEngineListener(this);
                    userMarker.setPosition(new LatLng(location));
                }
            }
        };
        locationEngine.addLocationEngineListener(locationListener);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted,
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
