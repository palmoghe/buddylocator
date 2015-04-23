package com.buddy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.util.Log;



public class Preferences extends PreferenceActivity{

	//public static int interval;			//GLOBAL VARIABLE
	//public int checkService=0;
	private static final int DEFAULT_TIME=5;
	private static final int DEFAULT_SERVICE_STATUS=0;
	private static final float DEFAULT_LATTITUDE=0;
	private static final float DEFAULT_LONGITUDE=0;
	

	
	public static int readInterval(Context context)
	{
		int value = DEFAULT_TIME;
		
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		value = mySharedPreferences.getInt("interval", value);
		return value;
		
	}

	public void writeInterval(Context context,int value)
	{
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("interval", value);
		editor.commit();
		Log.d("Preferences","########Value of the timer: "+value);
		value=readInterval(context);
		Log.d("Preferences","########Value after reading: "+value);
	}
	
	public int readServiceStatus(Context context)
	{
		int value = DEFAULT_SERVICE_STATUS;
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		value = mySharedPreferences.getInt("SerivceStatus", value);
		return value;
	}

	public void writeServiceStatus(Context context,int value)
	{
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt("ServiceStatus", value);
		editor.commit();
	}
	
/*	public int readInterval(Context context, String id)
	{
		int value = 0;
		if(id.equalsIgnoreCase("interval"))
			value = DEFAULT_TIME;
		else if(id.equalsIgnoreCase("checkService"))
			value=DEFAULT_CHECK_SERVICE;
		
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		value = mySharedPreferences.getInt(id, value);
		return value;
	}

	public void writeInterval(Context context, String id, int value)
	{
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putInt(id, value);
		editor.commit();
		Log.d("Preferences","########Value of the timer: "+id+value);
		value=readInterval(context,id);
		Log.d("Preferences","########Value after reading: "+value);
	}
	
*/
	
	public float[] readLocation(Context context)
	{
		
		float value[] ={DEFAULT_LATTITUDE,DEFAULT_LONGITUDE}; 
		//if(id.equalsIgnoreCase("interval"))
			//value = DEFAULT_TIME;
		//else if(id.equalsIgnoreCase("checkService"))
		//	value=DEFAULT_CHECK_SERVICE;
		
		//	value[0]=DEFAULT_LATTITUDE;
			//value[1]=DEFAULT_LONGITUDE;
	
		
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		value[0] = mySharedPreferences.getFloat("LATTITUDE", value[0]);
		value[1] = mySharedPreferences.getFloat("LONGITUDE", value[1]);
		
		return value;
	}

	public void writeLocation(Context context,float value1,float value2)
	{
		SharedPreferences mySharedPreferences = context.getSharedPreferences("BuddyLocator", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putFloat("LATTITUDE", value1);
		editor.putFloat("LONGITUDE", value2);
		editor.commit();
		float value[]= readLocation(context);
		Log.d("Preferences","########Values after reading of the lattitude: "+value[0]+":"+value[1]);
		
	}
}