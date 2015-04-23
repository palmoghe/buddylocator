package com.buddy.locate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import com.buddy.BuddyTabs;
import com.buddy.db.DBHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.buddy.R;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

//MapActivity is subclass of Activity and used to display maps
public class Locate extends MapActivity
{
	
	public static boolean LOCATEALL ;
	
	OverlayItem overlayitem;
	//HashMap is an implementation of Map
	HashMap<String,Location>result=new HashMap<String,Location>();
	String address;
	double lat,lon;
	Location loc;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        //MapView gets the map from google and fits it on the mobile screen
        //Refer map.xml for MapView parameters
        MapView mapView = (MapView) findViewById(R.id.mapView);
        
        //Displaying Zoom control over the map
        mapView.setBuiltInZoomControls(true);
        
        //Displaying Satellite and street view of map
        mapView.setSatellite(false);
        mapView.setStreetView(true);
        
        
        if(LOCATEALL)   // If locate ALL option has called this activity
        	result= DBHelper.getInstance(getApplicationContext()).getHashMapCoordinates(0);
        else			// If locate ONE option has called this activity
        	result= DBHelper.getInstance(getApplicationContext()).getHashMapCoordinates(1);
        
        //mapOverlays is a list of Overlays to be displayed over the map
        //One overlay corresponds to one buddy on map
        //Function getOverlays() gets all the overlays on your map
        List<Overlay> mapOverlays = mapView.getOverlays();
        //Display image for buddy is greenpin.jpg, stored in drawable-hdpi in resources(res)
        Drawable drawable = this.getResources().getDrawable(R.drawable.greenpin);
        //Creating instance of tryItemizedOverlay class (Refer tryItemizedOverlay.java) 
        tryItemizedOverlay itemizedoverlay=new tryItemizedOverlay(drawable,mapView.getContext());
        
        
        
        //Checking size of HashMap result
        if(result.size()>0)
		{
        	//Setting Iterator e to the HashMap (similar to pointer)
			Iterator<String>e=result.keySet().iterator();
			
			do
			{
				//Getting string address and location (loc-latitude and longitude)
				address=e.next();
				loc=result.get(address);
				
				lat=loc.getLatitude();
				lon=loc.getLongitude();
				
								
				GeoPoint point = new GeoPoint((int)(lat*1E6),(int)(lon*1E6));
				overlayitem = new OverlayItem(point, address,"Hello!!");
				//Creating overlay item for that location
				itemizedoverlay.addOverlay(overlayitem);
				  
			}while(e.hasNext());
		
		}
      //Adding all points to overlay list
        mapOverlays.add(itemizedoverlay);
    }
    
    
    @Override
    protected boolean isRouteDisplayed() 
    {
        return false;
    }
    
}