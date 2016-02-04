package com.example.fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.thrifty.R;
import com.google.android.gms.maps.CameraUpdateFactory; 
import com.google.android.gms.maps.GoogleMap; 
import com.google.android.gms.maps.MapView; 
import com.google.android.gms.maps.MapsInitializer; 
import com.google.android.gms.maps.model.BitmapDescriptorFactory; 
import com.google.android.gms.maps.model.LatLng; 
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery; 
 
/** 
 * A fragment that launches other parts of the demo application. 
 */ 
public class MapFragment extends Fragment implements LocationListener {
 
    MapView mMapView; 
    private GoogleMap googleMap;
    private boolean locate;
    private ParseObject branches[];
    
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        // inflate and return the layout 
        View v = inflater.inflate(R.layout.activity_map_fragment, container,false); 
        mMapView = (MapView) v.findViewById(R.id.mapView); 
        mMapView.onCreate(savedInstanceState); 
        
        locate = true; 
        mMapView.onResume();// needed to get the map to display immediately 
 
        try { 
            MapsInitializer.initialize(getActivity().getApplicationContext()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
        googleMap = mMapView.getMap(); 
        
     // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        
        if(location!=null){
            //onLocationChanged(location);
            drawMarker(location);
            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));	
            
            Geocoder gcd = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
    		try {
    			addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
    	        if (addresses.size() > 0) {
    	        	saveBranches(addresses.get(0).getLocality());
    	        }    	            
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        
        
        return v; 
    } 
    
    @Override
    public void onLocationChanged(Location location) {
    	
    		    
    }
    
    private void drawMarker(Location location){
        // Remove any existing markers on the map
        googleMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.addMarker(new MarkerOptions()
        .position(currentPosition)
        .snippet("Ubicacion actual")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .title("Mi Ubicacion"));
    }
    
    private void saveBranches(String city){
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("city");
    	query.whereEqualTo("name", city);
    	query.getFirstInBackground(new GetCallback<ParseObject>() {
    	  public void done(ParseObject object, ParseException e) {
    	    if (object != null) {
    	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("branch_office");
    	    	query.whereEqualTo("city", object);
    	    	query.findInBackground(new FindCallback<ParseObject>() {
    	    	    public void done(List<ParseObject> offices, ParseException e) {
    	    	        if (e == null) {
    	    	        	int i = 0;
    	    	        	branches = new ParseObject[offices.size()];
    	    	            for(ParseObject branch:offices){
	    	      	      		branches[i] = branch;
    	    	            	ParseGeoPoint pgp = (ParseGeoPoint) branch.get("ubication");
    	    	      	      	if(pgp != null){
    	    	      	      		LatLng position = new LatLng(pgp.getLatitude(),pgp.getLongitude());
    	    	      	      		googleMap.addMarker(new MarkerOptions()
    	    	                        .position(position)
    	    	                        .title(branch.getString("Name"))
    	    	                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
    	    	      	      }	
    	    	            }   	            
    	    	        } 
    	    	        else {
    	    	            e.printStackTrace();
    	    	        }
    	    	    }
    	    	});
    	    } else {
    	      
    	    }
    	  }
    	});
    	/*
    	googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("Hello world"));*/
    }
    
    private void addMarkerBranch(ParseObject branch){
    	ParseGeoPoint pgp = (ParseGeoPoint) branch.get("ubication");
	      if(pgp != null){
	    	LatLng position = new LatLng(pgp.getLatitude(),pgp.getLongitude());
	    	googleMap.addMarker(new MarkerOptions()
                  .position(position)
                  .title("hola"));
	      }	
    }
 
    @Override 
    public void onResume() { 
        super.onResume(); 
        mMapView.onResume(); 
    } 
 
    @Override 
    public void onPause() { 
        super.onPause(); 
        mMapView.onPause(); 
    } 
 
    @Override 
    public void onDestroy() { 
        super.onDestroy(); 
        mMapView.onDestroy(); 
    } 
 
    @Override 
    public void onLowMemory() { 
        super.onLowMemory(); 
        mMapView.onLowMemory(); 
    }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	} 
} 