//  this class helps user create a user profile for the application

package com.buddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buddy.data.UserProfile;
import com.buddy.db.DBHelper;

public class Profile extends Activity implements OnClickListener
{
	private EditText nameText, passwordText, confirmText;
	String message = "Please Enter:\n";
	boolean flag = false;
	
	public static void startActivity(Context context)
	{
		Intent intent = new Intent(context, Profile.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);		// open create profile screen
        
        nameText = (EditText)findViewById(R.id.profile_ET_name);
        passwordText = (EditText)findViewById(R.id.profile_ET_password);	// get refernces of the text boxes
        confirmText = (EditText)findViewById(R.id.profile_ET_confirmpwd);        
                
             
        Button login = (Button)findViewById(R.id.profile_B_save);
        
        login.setOnClickListener(new OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				if(nameText.length() == 0)
				{
					message += "Name\n";
					flag = true;				// if name not entered
					nameText.setTextColor(Color.RED);
				}
				
				if(passwordText.length() == 0)
				{
					message += "Password\n";
					flag = true;				// if password not entered
					passwordText.setTextColor(Color.RED);
				}
				
				if(confirmText.length() == 0)
				{
					message += "Password Confirmation\n";
					flag = true;				// if confirm password not entered
					confirmText.setTextColor(Color.RED);
				}
									
				// if the passwords dont match
				if(!(passwordText.getText().toString()).equals((confirmText.getText().toString())))
				{
					Toast.makeText(getApplicationContext(), "Passwords Don't Match!", Toast.LENGTH_LONG).show();
					flag = false;
				}		
				
				else if(flag == true)
					Toast.makeText(getApplicationContext(), (CharSequence)message, Toast.LENGTH_LONG).show();
				
								
				else
				{			
					UserProfile profile = saveProfile();		// if all right, save profile
				
					if(profile != null)
					{
						Login.startActivity(Profile.this, profile);		// if profile successfully created
					}
					else
					{
						
						Log.d("Error", "Profile coudnt be created!!");
					}
					finish();
				}

				message = "Please Enter :\n";
			}
		});
        
        Button cancel = (Button)findViewById(R.id.profile_B_cancel);
        
        cancel.setOnClickListener(new OnClickListener()
        {
        	
        	public void onClick(View v)
        	{        		
        		finish();			// close the application on exit
        	}
        });        
    }
    
    private UserProfile saveProfile()
    {
    	UserProfile profile = new UserProfile();
    	// call setter of profile
    	profile.setName(nameText.getText().toString());		// set name of the userprofile object
    	profile.setPassword(passwordText.getText().toString());		// set password of the userprofile object
    	
    	if(DBHelper.getInstance(this).saveProfile(profile))
    	{
    		return profile;				// return saved profile
    	}
    	return null;
    }

	public void onClick(View v) 
	{
			
	}	
}