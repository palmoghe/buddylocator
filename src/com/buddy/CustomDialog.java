// this class creates custom dialog for changing the password

package com.buddy;

import com.buddy.data.UserProfile;
import com.buddy.db.DBHelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class CustomDialog extends Dialog implements OnClickListener		// must extend dialog and implement listener
{
	
	EditText oldPass, newPass, confirmPass;
	Button change;
	
	public CustomDialog(Context con)
	{
		super(con);
		setContentView(R.layout.password_dialog);		// set layout to defined xml file
		setTitle("Change Password"); 		
			
		oldPass = (EditText) findViewById(R.id.password_ET_old);
		newPass= (EditText) findViewById(R.id.password_ET_new);		// get the data from dialog
		confirmPass = (EditText) findViewById(R.id.password_ET_confirm);
	   
		change = (Button)findViewById(R.id.password_B_change);	// get the button
		change.setOnClickListener(this);								// set a listener for the button
				
	}
	
	public void onClick(View v) 
	{
		cancel();				// close the dialog
		
		UserProfile up = DBHelper.getInstance(v.getContext()).getUserProfile();		// create instance of DBHelper to get userprofile
		String pass = up.getPassword();				// get password from user profile object
				
		if(oldPass.length()==0 || newPass.length()==0 || confirmPass.length() ==0)	// check if fields entered
			Toast.makeText(v.getContext(), "Enter all fields!", Toast.LENGTH_LONG).show();
		
		else if((oldPass.getText().toString()).equals(pass))		// check if old password entry tallies with password
		{
			if((newPass.getText().toString()).equals(confirmPass.getText().toString()))		// check if new password match
			{	
				up.setPassword(newPass.getText().toString());				// set new password as the password						
				DBHelper.getInstance(v.getContext()).saveProfile(up);		// save the modified profile in database
				Toast.makeText(v.getContext(), "Password successfully changed!", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(v.getContext(), "Enter matching passwords!", Toast.LENGTH_LONG).show();
	    }
			
		else
			Toast.makeText(v.getContext(), "Enter correct Password!", Toast.LENGTH_LONG).show();			
	
	}

}
