package com.example.thrifty;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.os.Bundle;

public class StoreActivity extends Activity{

    MapView mMapView; 
    private GoogleMap googleMap; 
    LatLng latlng = new LatLng(10, 10);
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_layout);
	      
		mMapView = (MapView) findViewById(R.id.storeMap); 
        mMapView.onCreate(savedInstanceState); 
 
        mMapView.onResume();// needed to get the map to display immediately 

        googleMap = mMapView.getMap(); 
        try { 
            MapsInitializer.initialize(getApplicationContext()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 	 
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("branch_office");
		query.getInBackground(getIntent().getStringExtra("objectId"), new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (e == null) {
		      ParseGeoPoint pgp = (ParseGeoPoint) object.get("ubication");
		      if(pgp != null){
		    	  latlng = new LatLng(pgp.getLatitude(),pgp.getLongitude());
		      }		  
				googleMap.addMarker(new MarkerOptions()
				        .position(latlng)
				        .title("Hello world"));
				
				CameraPosition cameraPosition = new CameraPosition.Builder()
					    .target(latlng) 			// Center Set
						.zoom(15.0f)                // Zoom
						.bearing(90)                // Orientation of the camera to east
						.tilt(30)                   // Tilt of the camera to 30 degrees
						.build();                   // Creates a CameraPosition from the builder
				googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		        
		    } else {
		      // something went wrong
		    }
		  }
		});
	}
}
