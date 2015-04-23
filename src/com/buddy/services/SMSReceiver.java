//This class handles all incoming messages (it is the receiver class)
// format of messages for the application is 
// APP_CODE:MESSAGE_TYPE:REQUEST/RESPONSE:DATA;
// APP_CODE = BL
// MESSAGE_TYPE = ADD/RMV/LOC
// REQUEST = 0, RESPONSE = 1
// FOR MORE INFO REFER DOC GIVEN


package com.buddy.services;

import com.buddy.BuddyTabs;
import com.buddy.supportService.*;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

//BroadcastReveiver is a inbuilt class which listens to all incoming messages
public class SMSReceiver
	extends BroadcastReceiver
{
	private final String	ACTION	= "android.provider.Telephony.SMS_RECEIVED";
	public static String	lastMessage = null;
	//This method gets called whenever a message is received
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		 
		//Toast toast = Toast.makeText(context, "SMS Received from ", Toast.LENGTH_LONG);
		//toast.show();
		Log.d("SMSReceiver","#####Got Into On Recieve");
		System.out.println("#####Got Into On Recieve");
		System.out.println("-------------------------------------------------SMS received ..." + intent);
		
		if(intent.getAction().equals(ACTION))
		{
			Bundle bundle = intent.getExtras();		
			Object messages[] = (Object[])bundle.get("pdus");		// pdus is the format of message received
			SmsMessage smsMessage[] = new SmsMessage[messages.length];		// get length of the message
		
			for(int n = 0; n < messages.length; n++)
			{
				smsMessage[n] = SmsMessage.createFromPdu((byte[])messages[n]);		// converts from pdu format to normal text msg
			}
			
			Log.d("SMSReceiver-parse","#####Call to Parser");
						
			parse(context,smsMessage);		// call parsing function to interpret the message
			
			//toast = Toast.makeText(context, "SMS Received from "+ smsMessage, Toast.LENGTH_LONG);
			//toast.show();			
		}
	}
	
	private void parse(Context con,SmsMessage smsMessage[])
	{
		String AppCode= null;
		String SenderPhNum = smsMessage[0].getOriginatingAddress();
		String sms = smsMessage[0].getMessageBody();
		AppCode=sms.substring(0,2);
		String ServiceCode=sms.substring(3,6);
		String RqRes=sms.substring(7,8);
		String Info=null;
		
	
		String ReqInfo;
	//Concatenate Phone no. + Name + MSG
		
		if(SenderPhNum.charAt(0)=='+')
				SenderPhNum=SenderPhNum.substring(3);
		MessageOperation messageOperation=new 	MessageOperation(con);
	
		if(AppCode.equalsIgnoreCase("bl"))
		{
			if(ServiceCode.equalsIgnoreCase("add"))
			{					
				if(RqRes.equalsIgnoreCase("0")) 
				{
					Info=sms.substring(9);//Info has name:msg
					ReqInfo=SenderPhNum.concat(":"+Info);
					System.out.println("Info ph+name+msg"+ReqInfo);
					Log.d("SMSReceiver","#####Callin add method");
					Toast toast = Toast.makeText(con, "Info extracted "+ ReqInfo, Toast.LENGTH_LONG);
					toast.show();
					messageOperation.add(ReqInfo);
					//UPDATING LIST
					BuddyTabs.updateListBuddyList();
					System.out.println("####### UPDATE LIST CALLED !");
				}
				else if(RqRes.equalsIgnoreCase("1"))
				{
					Log.d("SMSReceiver","#####Callin add_response method");
					Info=sms.substring(9);//Info has ACCEPT/REJECT
					ReqInfo=SenderPhNum.concat(":"+Info);
					Toast toast = Toast.makeText(con, "add response "+ Info, Toast.LENGTH_LONG);
					toast.show();
					messageOperation.addResponse(ReqInfo);
					//UPDATING LIST
					BuddyTabs.updateListBuddyList();
				}
				
			}
			else if(ServiceCode.equalsIgnoreCase("rmv"))
			{	
				Log.d("SMSReceiver","#####Callin remove method");
				messageOperation.remove(SenderPhNum);
				//UPDATING LIST
				BuddyTabs.updateListBuddyList();

				// Call updateList to update the List of buddies on UI dynamically

				if(RqRes.equalsIgnoreCase("0")) 
				{
					//RqRes=0  >>SOMEONE HAS REMOVED ME FROM HIS/HER db
					//RqRes=1  >>THIS IS THE ACKNOWLEDGEMENT RECEIVED AFTER DELETION REQUEST
					SMSSender sender=new SMSSender(con);
					sender.setPhoneNumber(SenderPhNum);
					sender.SendRemoveAck();
					
				}								
	
			}
			else if(ServiceCode.equalsIgnoreCase("loc"))
			{				
				//FORMAT : BL:LOC:0:LAT:LONG
				Info=sms.substring(9);
				String LocInfo = SenderPhNum.concat(":"+Info);
				//LocInfo = PhNum:Lat:Long
				messageOperation.LocUpdate(LocInfo);
				//UPDATING LIST
				BuddyTabs.updateListBuddyList();

			}

			
		}
			
	}
	private static void writeString(Context context, String value)
	{
		if(value == null) return;
		SharedPreferences mySharedPreferences = context.getSharedPreferences("SMS_RECEIVER", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("LAST_SMS", value);
		editor.commit();
	}
	
	public static String readString(Context context)
	{
		String value = null;
		SharedPreferences mySharedPreferences = context.getSharedPreferences("SMS_RECEIVER", Activity.MODE_PRIVATE);
		value = mySharedPreferences.getString("LAST_SMS", value);
		return value;
	}
}
