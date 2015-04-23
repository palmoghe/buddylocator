package com.buddy.supportService;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.buddy.BuddyTabs;
import com.buddy.Preferences;
import com.buddy.db.DBHelper;
import com.buddy.services.OurGeoCoder;
import com.buddy.services.SMSSender;
public class MessageOperation
{
	
	//private static final String MODE_PRIVATE = null;
	
	private Context con;
	SQLiteDatabase myDB = null;
	String PhNum=null;
	//String TableName = "TB_Buddies";
	String messageData;
	String Name;
	public MessageOperation(Context con)
	{
		this.con=con;
				
		Log.d("MessageOperation","#####Constructor for MessageOperation");
		Log.d("MessageOperation","####Derived values: "+PhNum+":"+messageData);
		
	}
	
	public void add(String Info)
	{
	
		String content[]=Info.split(":");
		PhNum=content[0];
		Name=content[1];
		messageData = content[2];
		Toast toast = Toast.makeText(con, "add method "+ PhNum+Name+messageData, Toast.LENGTH_LONG);
		toast.show();
		try
		{
			/* Create a Database. */
			
			DBHelper.getInstance(con).addRequestToDB(PhNum,Name,messageData);
			//BuddyTabs bt = new BuddyTabs();
			//BuddyTabs.updateListBuddyList();
			Log.d("MessageOperation","#####try for AddRequest: ");
		}
		finally
		{
			//if(myDB != null) myDB.close();
			Log.d("MessageOperation","#####Finally Block");
		}
	}
	
	public void addResponse(String Info)
	{
		String response[]=Info.split(":");
		PhNum=response[0];
		//response[1] has ACCEPT/REJECT
		if(response[1].equalsIgnoreCase("accept"))
		{
			//change the request status in the buddy table
			Toast toast = Toast.makeText(con, "in add response method "+ PhNum, Toast.LENGTH_LONG);
			toast.show();
			
			//send own location update to the buddy
			DBHelper.getInstance(con).addResponseUpdateDB(PhNum);
			SMSSender sender = new SMSSender(con);
			sender.setPhoneNumber(PhNum);
			sender.SendLocationUpdate();
			
			
			//Call method to insert location into d location table 
			int id=DBHelper.getInstance(con).GetIdFromPhoneNo(PhNum);
			
			DBHelper.getInstance(con).InsertLocation(id);
			//*** Update ListView on Tab
			//BuddyTabs.callUpdateListFromService();
			
			
		}
		else if(response[1].equalsIgnoreCase("reject"))
		{
			//delete the send request from the buddy
			Toast toast = Toast.makeText(con, "#####IN RESPONSE REJECT", Toast.LENGTH_LONG);
			toast.show();
			DBHelper.getInstance(con).DeleteBuddyFromDB(PhNum);
		}
		
	}
	public void remove(String PhNum)
	{
		try
		{
			DBHelper.getInstance(con).DeleteBuddyFromDB(PhNum);

			Log.d("MessageOperation","#####Entry Removed");
		}
		catch(Exception e)
		{
			Log.e("Error", "Error", e);
			Log.d("MessageOperation","#####Catch for RemRequest");
		}
		finally
		{
			
			Log.d("MessageOperation","#####Finally Block");
		}
	}
	
	public void LocUpdate(String Info)
	{
		String content[]=Info.split(":");
		String PhNum = content[0];
		String latitude=content[1];
		String longitude=content[2];
		OurGeoCoder gc=new OurGeoCoder(con);
		
		String address=gc.getAddr(Float.parseFloat(latitude), Float.parseFloat(longitude));
		Log.d("GeoCoder","Location is : "+address);
		//---------------Use above address to update the database
		try
		{
			int BuddyID = DBHelper.getInstance(con).GetIdFromPhoneNo(PhNum);
			Log.d("MsgOp", "####In loc update, chk id from num -num,id : "+PhNum+BuddyID);
			DBHelper.getInstance(con).UpdateBuddyLocation(BuddyID,Float.parseFloat(latitude),Float.parseFloat(longitude),address);
			Log.d("MessageOperation","#####Location Updated");
			
		}
		catch(Exception e)
		{
			Log.e("Error", "Error", e);
			Log.d("MessageOperation","#####Catch for LocationUpdate");
		}
		finally
		{
			if(myDB != null) myDB.close();
			Log.d("MessageOperation","#####Finally Block");
		}
		
		
	}
			
}