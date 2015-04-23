// this the entry point of the application.
// if a userprofile is detected in DB, login screen is opened.
// else screen to create profile is opened.


package com.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.buddy.data.UserProfile;
import com.buddy.db.DBHelper;
import com.buddy.services.LocationUpdateService;


public class StartupActivity extends android.app.Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		UserProfile userProfile = DBHelper.getInstance(this).getUserProfile();   // get the user profile from table
		System.out.println("USER PROFILE:" + userProfile);
		
		if(userProfile == null)		 // user profile has not been created, launch new Profile Activity
		{
			Profile.startActivity(this);
		}
		
		else						// user profile has been created, launch Log in Activity
		{
			Login.startActivity(this, userProfile);
		}
		
		finish();					// end this activity
	}
}
