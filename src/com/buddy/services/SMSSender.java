
package com.buddy.services;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.buddy.Preferences;
import com.buddy.db.DBHelper;

public class SMSSender 
{
	String AppCode=new String("BL:");
	String MsgData=null;
	String phoneNumber;
	Context context;
	public SMSSender(Context con)
	{
		context=con;
	}
		
	public void setPhoneNumber(String num)
	{
		phoneNumber=num;
	}
	
	private void SendMessage()
	{
		SmsManager sm = SmsManager.getDefault();
		//sm.sendTextMessage(destinationAddress, null, "Hello world", null,
		sm.sendTextMessage(phoneNumber, 
                null, 
                MsgData, 
                null, null);
		Log.d("LocationUpdateService","########MEssage Sent");
	}
	public void SendLocationUpdatetoAll(float lat,float longitude)
	{
			
		Log.d("Sender","No call to preference");
		MsgData=AppCode.concat("LOC:"+Float.toString(lat)+":"+Float.toString(longitude));
/*		
 * -------------------Following Should be uncommented to braodcast message to all buddies--------	
 */
		String phNos[]=DBHelper.getInstance(context).getAllBuddyPhoneNos();
		for(int i=0;i<phNos.length;i++)
		{
			if(phNos[i]!=null)
			{
			phoneNumber=phNos[i];
			Log.d("SEnding all","#####Update sending to : "+phoneNumber);
			SendMessage();
			}
			
		}
		/*
----------------------------Till here------------------------------------------------
		*/
	//	phoneNumber="5554";
		//SendMessage();

		Log.d("SMSSENder","###############Location updated sent to all");
	}	
	public void SendLocationUpdate()
	{
		Preferences pf=new Preferences();
		float[] loc=pf.readLocation(context);
		MsgData=AppCode.concat("LOC:"+loc[0]+":"+loc[1]);
		SendMessage();
	}
	
	public void SendAddRequest(String message)
	{
		//string name should contain the user name extractd from the database
		
		String name= DBHelper.getInstance(context).getProfileName();
		System.out.println("#####UN "+name);
		MsgData=AppCode.concat("ADD:0:"+name+":"+message);
		Log.d("add request", "########"+MsgData);
		SendMessage();
	}
	
	public void SendAddAck(String response)
	{
		MsgData=AppCode.concat("ADD:1:"+response);
		SendMessage();
		
	}
	
	public void SendRemove()
	{
		MsgData=AppCode.concat("RMV:0");
		SendMessage();
	}
	public void SendRemoveAck()
	{
		{
			MsgData=AppCode.concat("RMV:1");
			SendMessage();
		}
	}
}
	
