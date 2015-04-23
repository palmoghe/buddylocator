// this service receives location updates of the user and saves it in preferences object

package com.buddy.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.buddy.Preferences;

//Extends to service because this class has to run in the background even if the application
//is closed. LocationListener enables location updates and store changed location
public class LocationUpdateService extends Service implements LocationListener
{
	private LocationManager lm;
	private Location location;
	Preferences pref = new Preferences();
	private LocationUpdateThread locationUpdateThread;
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		Log.d("LocationUpdateService","########Service Created!!!");
	}
	
	@Override
	public void onStart(Intent intent, int startid)
 
	{
		Preferences pref=new Preferences();
		
		Log.d("LocationUpadte","######Service Started:Status:"+pref.readServiceStatus(getApplicationContext()));
		pref.writeServiceStatus(getApplicationContext(), 1);
		pref.writeLocation(getApplicationContext(), 0, 0);
	
		//All the location updates are accessed through the instance of class LocationManager
		lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);	// initialize object of location manager
		
		float[] read=pref.readLocation(getApplicationContext());	// read user's own location from preferences
		Log.d("LocationUpdateService","########DefaultLocation"+ read[0]+" , "+read[1]);
		
		//Register for the device's location updates
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 0, this);		
		updateLocationInfo();

	}

		

	class LocationUpdateThread extends StoppableThread
	{
		protected boolean stop = false;
		
		public LocationUpdateThread()
		{
			//daemon thread implies this has to be background thread
			setDaemon(true);
		}
		
		public void stopThread()
		{
			stop = true;
			interrupt();		
		}
		
		@Override
		public void run()

		{
			while(!stop)
			{
				Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Log.d("LocationUpdateService","###########Entered while");
				
				//to Get last known location
				loc= lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(loc != null)
				{	
					Log.d("LocationUpdateService","###########LastKnownLocation not null!!");
					checkLocChange((float)loc.getLatitude(),(float)loc.getLongitude());
				}
				else
				{
					if(location!=null)
					{
						Log.d("LocationUpdateService","###########Location From notifier not null ");
						checkLocChange((float)location.getLatitude(),(float)location.getLongitude());
					}
				}
				try {
					//Setting the interval time between the thread wake up calls
					Log.d("LocationUpdateService","###########interval : "+Preferences.readInterval(getApplicationContext()));
					Thread.sleep(Preferences.readInterval(getApplicationContext())*60*1000);
					Log.d("LocationUpdateService","###########THREAD GOING OFF TO SLEEP");
					//Thread.sleep(60*1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public void updateLocationInfo()
	{
			Log.d("LocationUpdateService","###########Entered the updateLocationInfo method. Starting the servicve thread");
			
			locationUpdateThread = new LocationUpdateThread();
			//Start the thread that calls the run() method
			locationUpdateThread.start();
			
			
			
		}
	
		//Method to check if the new location should be broacdcasted or not
		private void checkLocChange(float latestLat, float latestLong)
 
		{
			Log.d("LocationUpdateService","###########In CheckLoc Change");
			SMSSender smsSender = new SMSSender(getApplicationContext());
			
			
			if(checkDifference(latestLat,latestLong)==1)
			{
				Log.d("LocationUpdateService","###########Loc Difference=1");
					pref.writeLocation(getBaseContext(), latestLat, latestLong);
					
					Log.d("LocationChanged","#######LocationChanged: "+latestLat+":"+latestLong);
					Log.d("LocationChanged","#######Calling sender");
					
					smsSender.SendLocationUpdatetoAll(latestLat,latestLong);
					
			}
		}
		
		//Method returns 1 if the new location lies out of the range of previous location
		private int checkDifference(float latestLat, float latestLong)

		{
			Log.d("LocationUpdateService","###########To check Difference");
			float prevLoc[]=pref.readLocation(getBaseContext());
			Log.d("LocationUpdateService","###########LAT Long"+prevLoc[0]+" , "+prevLoc[1]);
			if(latestLat<prevLoc[0]-10||latestLat>prevLoc[0]+10)
				return 1;
			else if(latestLong<prevLoc[1]-10||latestLong>prevLoc[1]+10)
				return 1;
			else
			{
				Log.d("LocationUpdateService","###########To check Difference:Returning 0");
				return 0;
			}
		}
		@Override		
		public void onDestroy() 

		{
			super.onDestroy();
			Toast.makeText(this, "Service destroyed...", Toast.LENGTH_LONG).show();
			Preferences pref = new Preferences();
			pref.writeServiceStatus(getApplicationContext(), 0);
			if(locationUpdateThread != null)
			{
				locationUpdateThread.stopThread();
			}
		}



		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			this.location=location;
		}
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
}