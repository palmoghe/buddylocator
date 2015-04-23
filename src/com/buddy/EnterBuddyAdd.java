package com.buddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.buddy.db.DBHelper;
import com.buddy.services.SMSSender;



public class EnterBuddyAdd extends Activity 
{
	public final static String NAME = "name";
	public final static String NUMBER = "number";
	private String name = null;
	private String number = null;
	
	private EditText nameText, numberText;
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enteraddbuddy);
		
		nameText = (EditText) findViewById(R.id.enteraddbuddy_ET_name);
		numberText = (EditText) findViewById(R.id.enteraddbuddy_ET_phone);
		
		name = getIntent().getStringExtra(NAME);
		number = getIntent().getStringExtra(NUMBER);
		nameText.setText(name);
		numberText.setText(number);
		Button send = (Button) findViewById(R.id.enteraddbuddy_B_add);
		send.setOnClickListener(new OnClickListener()		// on back button click
        {
			
			public void onClick(View v) 
			{
				//finish();			// done go back to add buddy screen
				sendBuddyRequest();
				Intent i = new Intent(EnterBuddyAdd.this, BuddyTabs.class);
				startActivity(i);
				
			}        	
        });
				
		Button back = (Button) findViewById(R.id.enteraddbuddy_B_back);
		back.setOnClickListener(new OnClickListener()		// on back button click
        {
			
			public void onClick(View v) 
			{
				finish();			// done go back to add buddy screen
			}        	
        });
		
		/** Default message displayed in message box 
		 *  using name of user from preferences
		 */
		
		
	}

	public void onClick(View v) 
	{
		sendBuddyRequest();
	}
	
	private void sendBuddyRequest()
	{ 
		/** Validations for Phone No. entered left
		 * 
		 */
		
		//Extract name phone no. and message from EditText of message
		
		String message;
		message = ((EditText)findViewById(R.id.enteraddbuddy_ET_message)).getText().toString();
		String phoneNo;
		phoneNo = ((EditText)findViewById(R.id.enteraddbuddy_ET_phone)).getText().toString();
		String name;
		name = ((EditText)findViewById(R.id.enteraddbuddy_ET_name)).getText().toString();
		
		//Validation for buddy request
		
		
		int BuddyID = DBHelper.getInstance(getApplicationContext()).GetIdFromPhoneNo(phoneNo);
		if(BuddyID==-1)
		{
			System.out.println("#########Buddy Does Not Exist");
			// Send Request by calling SMSSender Method
			SMSSender sender = new SMSSender(this);
			sender.setPhoneNumber(phoneNo);
			sender.SendAddRequest(message);
		
			// Add Request into apps DB
		
			DBHelper.getInstance(this).addRequest(name, phoneNo, message);	
		}
		else
		{
			System.out.println("#########Buddy Already Exists");
			Toast toast = Toast.makeText(getApplicationContext(), "Already A Buddy!!! ", Toast.LENGTH_LONG);
			toast.show(); 
		}
		finish();
						
	}
}
