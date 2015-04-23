// gets called when the cell phone is booted/switched on

package com.buddy.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.buddy.Preferences;
 
public class StartAtBootServiceReceiver extends BroadcastReceiver 
{
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d("StartAtBooting","Booting Complete");
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))	// ACTION_BOOT_COMPLETED -  inbuilt action which gets fired on booting 
		{
			Preferences pref = new Preferences(); // create object of preferences
			Intent i = new Intent(context, LocationUpdateService.class);	// the intent obj tells the application which service to run next
			context.startService(i);
			
		}
	}
}
