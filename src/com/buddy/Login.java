//# changed -padma

package com.buddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buddy.data.UserProfile;

public class Login extends Activity implements OnClickListener
{
	private static final String USER_PROFILE = "user_profile";
	private UserProfile userProfile; 	// user profile object, contains all the profile details of present user.
	
	public static void startActivity(Context context, UserProfile userProfile)
	{
		System.out.println("STARTUP LOGIN:" + userProfile);
		Intent intent = new Intent(context, Login.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(USER_PROFILE, userProfile);
		context.startActivity(intent);
	}
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);			// opens login screen
        
        userProfile = (UserProfile)getIntent().getSerializableExtra(USER_PROFILE);	// get the user profile object
        
        if(userProfile == null)
        {
        	// show error popup and exit app
        	finish();
        }
        
        else
        {
	        Button login=(Button)findViewById(R.id.login_B_login);
	        login.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) 
				{
					EditText pswd =(EditText)findViewById(R.id.login_ET_password);  // get password from the edit-text
					
					if(pswd.length() == 0)			// check if password entered
					{
						Toast.makeText(getApplicationContext(), "Please Enter a Password!", Toast.LENGTH_LONG).show();
						pswd.setTextColor(Color.RED);
					}
					
					else if((pswd.getText().toString()).equals(userProfile.getPassword()))		// check the password with profile
					{
						Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
						
						Intent i = new Intent(Login.this, BuddyTabs.class);
						startActivity(i);
						finish();
						
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Password Error!", Toast.LENGTH_LONG).show();
						System.out.println("%%%%%----- password not match");
						pswd.setTextColor(Color.RED);
					}
				}
			});
	        
	        Button exit = (Button)findViewById(R.id.login_B_exit);
	        exit.setOnClickListener(new OnClickListener(){
	        	
	        	public void onClick(View v)
	        	{        		
	        		finish();			// close the application on exit
	        	}
	        });
        }
    }

	public void onClick(View v) 
	{
			
	}	
}