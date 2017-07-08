package com.cp.user.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cp.user.R;
import com.cp.user.bus.ApplicationBus;
import com.cp.user.model.HistoryAddress;
import com.cp.user.mvp.MainPresenter;
import com.cp.user.mvp.MainPresenterImpl;
import com.cp.user.mvp.MainView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yi on 07/07/2017.
 */

@SuppressWarnings({"MissingPermission"})
public class MapBoxActivity extends BaseActivity implements MainView {

    private MarkerView userMarker;
    private MarkerView adrMarker;
    private MapboxMap map;

    private String[] COUNTRIES = {"FR"};

    private MainPresenter mainPresenter;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.query)
    GeocoderAutoCompleteView autocomplete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainPresenter = new MainPresenterImpl(this, this);

        mapView.onCreate(savedInstanceState);

        initMapProvider();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initMapProvider() {
        mapView.getMapAsync(mapboxMap -> {

            mapboxMap.setOnMapLongClickListener(point -> {

                Position position = Position.fromCoordinates(point.getLatitude(), point.getLongitude());

                MapboxGeocoding client = new MapboxGeocoding.Builder()
                        .setAccessToken(Mapbox.getAccessToken())
                        .setCoordinates(position)
                        .setCountries(COUNTRIES)
                        .setGeocodingType(GeocodingCriteria.TYPE_ADDRESS)
                        .build();
                client.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                        if (response.body() != null && response.body().getFeatures() != null && response.body().getFeatures().size() > 0) {
                            autocomplete.setText(response.body().getFeatures().get(0).getAddress());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable t) {

                    }
                });

                updateMap(point.getLatitude(), point.getLongitude());
                mainPresenter.updateHistory(autocomplete.getText().toString(), point.getLatitude(), point.getLongitude());
            });

            map = mapboxMap;
            userMarker = mapboxMap.addMarker(
                    new MarkerViewOptions()
                            .position(new LatLng(0, 0))
                            .anchor(0.5f, 0.5f), markerView -> {
                        mainPresenter.initLocation();
                    });

        });

        autocomplete.setAccessToken(Mapbox.getAccessToken());
        autocomplete.setCountries(COUNTRIES);
        autocomplete.setType(GeocodingCriteria.TYPE_ADDRESS);
        autocomplete.setOnFeatureListener(feature -> {
            hideOnScreenKeyboard();
            Position position = feature.asPosition();
            updateMap(position.getLatitude(), position.getLongitude());
            mainPresenter.updateHistory(autocomplete.getText().toString(), position.getLatitude(), position.getLongitude());
        });
    }

    @Override
    protected void updateMap(HistoryAddress historyAddress) {
        if (!android.text.TextUtils.isEmpty(historyAddress.getAddress())){
            autocomplete.setOnFeatureListener(null);
            autocomplete.setText(historyAddress.getAddress());
            autocomplete.setOnFeatureListener(feature -> {
                hideOnScreenKeyboard();
                Position position = feature.asPosition();
                updateMap(position.getLatitude(), position.getLongitude());
                mainPresenter.updateHistory(autocomplete.getText().toString(), position.getLatitude(), position.getLongitude());
            });
        }

        updateMap(historyAddress.getLatitude(), historyAddress.getLongitude());
        mainPresenter.updateHistory(historyAddress);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
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

    private void hideOnScreenKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void updateMap(double latitude, double longitude) {

        if (map != null & adrMarker != null)
            map.removeMarker(adrMarker);

        // Build marker
        adrMarker = map.addMarker(new MarkerViewOptions()
                .position(new LatLng(latitude, longitude))
                .title(getString(R.string.geocode_activity_marker_options_title)));

        // Animate camera to geocoder result location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }

    @Override
    public void setCurrentLocation(Location currentLocation) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation), 16));
        userMarker.setPosition(new LatLng(currentLocation));
    }

    @Override
    public void onLocationChanged(Location location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 16));
        userMarker.setPosition(new LatLng(location));
    }

    @Override
    public void noPermissionGranted() {
        Toast.makeText(this, R.string.user_location_permission_not_granted,
                Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onHistoryUpdated(List<HistoryAddress> list) {
        refreshMenu(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
