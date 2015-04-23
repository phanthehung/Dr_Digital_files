package com.khoisang.drdigital.ui;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.khoisang.drdigital.R;
import com.khoisang.khoisanglibary.ui.ActionEvent;
import com.khoisang.khoisanglibary.ui.activity.BaseActivity;

public class ActivityGoogleMap extends BaseActivity implements
		OnMapReadyCallback, OnMyLocationChangeListener, OnMapLongClickListener,
		OnMapClickListener, OnCameraChangeListener, OnMapLoadedCallback {

	public static final String KEY = "location";

	private GoogleMap mGoogleMap = null;
	public static com.khoisang.drdigital.data.Location Location;

	@Override
	protected int getLayoutID() {
		return R.layout.activity_googlemap;
	}

	@Override
	protected void afterSetLayoutID(Bundle savedInstanceState) {
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_googlemap_map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void handleEvent(ActionEvent actionEvent) {
	}

	@Override
	protected void beforeSetLayoutID(Bundle savedInstanceState) {

	}

	@Override
	public void onMapReady(GoogleMap map) {
		if (mGoogleMap == null) {
			mGoogleMap = map;
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.setOnMyLocationChangeListener(this);
			mGoogleMap.setOnMapLongClickListener(this);
			mGoogleMap.setOnMapClickListener(this);
			mGoogleMap.setOnMapLoadedCallback(this);
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			mGoogleMap.setTrafficEnabled(true);
			mGoogleMap.setOnCameraChangeListener(this);

			// Setting
			// UiSettings mUiSettings = mGoogleMap.getUiSettings();
			// mUiSettings.setZoomGesturesEnabled(true);
			// mUiSettings.setZoomControlsEnabled(false);
			// mUiSettings.setCompassEnabled(false);
			// mUiSettings.setMyLocationButtonEnabled(false);
			// mUiSettings.setScrollGesturesEnabled(true);
			// mUiSettings.setTiltGesturesEnabled(false);
			// mUiSettings.setRotateGesturesEnabled(false);
		}
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
	}

	@Override
	public void onMapClick(LatLng arg0) {
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
	}

	@Override
	public void onMyLocationChange(Location arg0) {
	}

	@Override
	public void onMapLoaded() {
		if (Location != null) {
			LatLng latLng = new LatLng(Location.latitude, Location.longitude);
			mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(
					Location.locationName));
			int distance = 1000;
			LatLngBounds latLngBounds = new LatLngBounds.Builder()
					.include(SphericalUtil.computeOffset(latLng, distance, 0))
					.include(SphericalUtil.computeOffset(latLng, distance, 90))
					.include(SphericalUtil.computeOffset(latLng, distance, 180))
					.include(SphericalUtil.computeOffset(latLng, distance, 270))
					.build();
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
					latLngBounds, 10);
			mGoogleMap.moveCamera(cameraUpdate);
		}
	}

}
