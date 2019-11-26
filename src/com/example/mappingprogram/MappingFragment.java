package com.example.mappingprogram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MappingFragment extends SupportMapFragment {
	private GoogleMap googleMap;
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	private LocationTrackerManager mRunManager;
    private Location mLastLocation;
    private MarkerOptions myMarker;
    
    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        @Override
        protected void onLocationReceived(Context context, Location loc) {
            mLastLocation = loc;
            updateUI();
        }
        
        @Override
        protected void onProviderEnabledChanged(boolean enabled) {}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunManager = LocationTrackerManager.get(getActivity());
        mRunManager.startLocationUpdates();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//View view = inflater.inflate(R.layout.fragment_mapping, container, false);
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		googleMap = getMap();
		googleMap.setMyLocationEnabled(true);
		updateUI();
		
		
		return view;
	}
	
	public void updateUI()
	{
		if(mLastLocation == null) return;
		
		LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
		
		if(myMarker == null)
		{
			myMarker = new MarkerOptions().position(lastPosition).title("Me");
			googleMap.addMarker(myMarker);
		}
		
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 15));

	}
	
	@Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, 
                new IntentFilter(LocationTrackerManager.ACTION_LOCATION));
    }
    
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }
}