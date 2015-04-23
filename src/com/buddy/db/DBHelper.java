/**
 * This File forms the DBAcccess layer of the application
 * Any method which requires DB access has to be defined here
 * to call the method first call DBHelper.getInstance (getApplicationContext())
 */


package com.buddy.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.buddy.BuddyTabs;
import com.buddy.Preferences;
import com.buddy.data.BuddyInfo;
import com.buddy.data.UserProfile;

public class DBHelper {
	
	
	private static Context con;
	private static DBHelper helper;
	private SQLiteDatabase db;
	private final static String TABLE_PROFILE = "TB_UserProfile"; 
	private final static String TABLE_BUDDY = "TB_Buddy1";
	private final static String TABLE_LOCATION = "loc";
	private final static String TABLE_MESSAGE = "message";

	private final static String COL_PROFILE_ID = "id";
	private final static String COL_PROFILE_NAME = "name";
	private final static String COL_PROFILE_PWD = "password";

	private final static String COL_BUDDY_NAME = "BuddyName";
	private final static String COL_BUDDY_ID = "_id";
	private final static String COL_BUDDY_REQ_STATUS = "ReqStatus";
	private final static String COL_BUDDY_ONLINE_STATUS = "OnlineStatus";
	private final static String COL_BUDDY_PHONE_NO = "PhoneNo";

	private final static String COL_LOCATION_ID = "_id";
	private final static String COL_LOCATION_LATITUDE = "latitude";
	private final static String COL_LOCATION_LONGITUDE = "longitude";
	private final static String COL_LOCATION_ADDRESS = "address";

	private final static String COL_MESSAGE_MSG = "Message";

	private DBHelper(Context context) {
		this.con = context;
		
		// Database and Table Creation  
		db = context.openOrCreateDatabase("BETA_TEST", Context.MODE_PRIVATE,
				null);
		
		// TABLE_PROFILE
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PROFILE + " ( "
				+ COL_PROFILE_ID + " INT(3), " + COL_PROFILE_NAME
				+ " VARCHAR(64), " + COL_PROFILE_PWD + " VARCHAR(12))");

		
		// TABLE_BUDDY
		db.execSQL("CREATE TABLE IF NOT EXISTS "
						+ TABLE_BUDDY
						+ " (_id INTEGER(3) PRIMARY KEY , PhoneNo CHARACTER(14),BuddyName VARCHAR(25),"
						+ "OnlineStatus INTEGER(1),ReqStatus INTEGER(1));");

		// TABLE_LOCATION
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOCATION + "("
				+ COL_LOCATION_ID + " INTEGER(3) PRIMARY KEY,"
				+ COL_LOCATION_LATITUDE + " FLOAT(8)," + COL_LOCATION_LONGITUDE
				+ " FLOAT(8)," + COL_LOCATION_ADDRESS + " VARCHAR(100),"
				+ " FOREIGN KEY (" + COL_BUDDY_ID + ") REFERENCES "
				+ TABLE_BUDDY + "(" + COL_BUDDY_ID + ")"
				+ " ON DELETE CASCADE);");

		// TABLE_MESSAGE to STORE REQUEST's MESSAGE

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE
				+ " (_id INTEGER(3) PRIMARY KEY , Message VARCHAR(100),"
				+ "FOREIGN KEY (" + COL_BUDDY_ID + ") REFERENCES "
				+ TABLE_BUDDY + "(" + COL_BUDDY_ID + ")"
				+ " ON DELETE CASCADE);");
	}

	public static DBHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DBHelper(context);
		}
		return helper;
	}

	public UserProfile getUserProfile() {
		UserProfile profile = null;
		try {
			Cursor cursor = db.rawQuery("select * from " + TABLE_PROFILE, null);
			cursor.moveToFirst();
			int count = cursor.getCount();

			if (count > 0) {
				profile = new UserProfile();
				int id = cursor.getInt(cursor.getColumnIndex(COL_PROFILE_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(COL_PROFILE_NAME));
				String password = cursor.getString(cursor
						.getColumnIndex(COL_PROFILE_PWD));
				profile.setId(id);
				profile.setName(name);
				profile.setPassword(password);
			}
			cursor.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return profile;
	}

	public boolean saveProfile(UserProfile profile) {
		boolean rowUpdated = false;
		if (profile == null)
			return false;
		// check if profile exists ...
		UserProfile existingProfile = getUserProfile();
		try {
			if (existingProfile == null) {
				// insert a new profile
				
				db.execSQL("INSERT INTO " + TABLE_PROFILE + " VALUES (1,'"
						+ profile.getName() + "','" + profile.getPassword()
						+ "')");

			} else {
				// update existing profile
				
				db.execSQL("UPDATE " + TABLE_PROFILE + " SET "
						+ COL_PROFILE_NAME + "='" + profile.getName() + "',"
						+ COL_PROFILE_PWD + "='" + profile.getPassword()
						+ "' where " + COL_PROFILE_ID + "=" + profile.getId());
			}
			rowUpdated = true;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return rowUpdated;
	}

	public boolean saveBuddy(BuddyInfo buddyInfo) {
		boolean saved = false;
		return saved;
	}

	// Function returning Profile Name of user of app
	// used to send with his buddy request
	public String getProfileName() {

		Cursor cursor = db.rawQuery("select " + COL_PROFILE_NAME + " FROM "
				+ TABLE_PROFILE, null);
		cursor.moveToFirst();
		String profileName = cursor.getString(cursor
				.getColumnIndex(COL_PROFILE_NAME));
		cursor.close();
		return profileName;
	}
//--------------------------------All DB operations related to ADD/ACCEPT/REJECT/----------------------------------------------
	
	//Function to generate Buddy Id .. This is max(buddy id) + 1
	public int generatId() {

		Cursor cursor = db.rawQuery("SELECT MAX(" + COL_BUDDY_ID + ") FROM "
				+ TABLE_BUDDY, null);
		int genId = 0;

		Cursor countRecs = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BUDDY,
				null);
		countRecs.moveToFirst();

		if (countRecs.getInt(0) > 0) {

			cursor.moveToFirst();
			genId = cursor.getInt(0) + 1;

		} else {

			genId = 0;
		}
		cursor.close();
		countRecs.close();
		return (genId);
	}

	// This function is called when user receives add request from a Buddy
	public void addRequestToDB(String PhNum, String Name, String messageData) {

		int genId = generatId();

		System.out.println("###########3 generated ID + " + genId);

		db.execSQL("INSERT INTO " + TABLE_MESSAGE + " VALUES (" + genId + ",'"
				+ messageData + "');");
		db.execSQL("INSERT INTO " + TABLE_BUDDY + " VALUES (" + genId + ",'"
				+ PhNum + "','" + Name + "',0,1);");


	}

	// This function is called when user sends add request to a Buddy
	public void addRequest(String name, String phoneNo, String message) {

		int genId = 0;
		genId = generatId();
		db.execSQL("INSERT INTO " + TABLE_BUDDY + " VALUES (" + genId + ",'"
				+ phoneNo + "','" + name + "',0,2);");

	}


	//  Function deletes pending request when any request gets deleted
	// OR when user removes any buddy from list
	public void DeleteBuddyFromDB(String PhNum) {

		db.execSQL("DELETE FROM " + TABLE_BUDDY + " WHERE "
				+ COL_BUDDY_PHONE_NO + " = '" + PhNum + "'");
		
		/* Toast toast = Toast.makeText(con, "######Buddy DELETED",
				Toast.LENGTH_LONG);
		toast.show(); */

	}

	// Function to Create  record TABLE_LOCATION when Request accepted 
	public void InsertLocation(int id) {
		db.execSQL("INSERT INTO " + TABLE_LOCATION + " VALUES ( " + id
				+ " ,0,0,null );");
		Log.d("location", "###########Location inserted");
	}
	// Function to update Req Status to 3 ( When buddy removed but ack not received )s
	public void removeBuddy(int buddyId) {

		String query = " UPDATE " + TABLE_BUDDY + " SET "
				+ COL_BUDDY_REQ_STATUS + " = '3' WHERE " + COL_BUDDY_ID + " = "
				+ buddyId;

		db.execSQL(query);

	}
	
	// Function to accept buddy , update req status to " 0 "
	
	public void acceptBuddy(int buddyId) {

		Log.d("BuddyTab", "#####calling acceptBuddy()");
		String query = " UPDATE " + TABLE_BUDDY + " SET "
				+ COL_BUDDY_REQ_STATUS + " =0 WHERE " + COL_BUDDY_ID + " = "
				+ buddyId;

		db.execSQL(query);

	}

	// Function to reject request ( Remove record from buddy rable )
	public void rejectBuddy(int buddyId) {

		String query = " DELETE FROM " + TABLE_BUDDY + " WHERE " + COL_BUDDY_ID
				+ " = " + buddyId;

		db.execSQL(query);

	}
	
	
	//--------------------------Helper functions to retreive data from DB----------------
	
	// Function to return message corresponding to a request
	public String getMessageFromId(int BuddyId) {
		Cursor cursor = db.rawQuery("SELECT Message FROM " + TABLE_MESSAGE
				+ " WHERE " + COL_BUDDY_ID + "=" + BuddyId + ");", null);
		cursor.moveToFirst();
		String msg = cursor.getString(cursor.getColumnIndex(COL_MESSAGE_MSG));
		return msg;
	}
    // Function to update Request status to 0 (ie. make him buddy upon receinving approval )
	public void addResponseUpdateDB(String PhNum) {
		db.execSQL("UPDATE " + TABLE_BUDDY + " SET " + COL_BUDDY_REQ_STATUS
				+ "=0 WHERE " + COL_BUDDY_PHONE_NO + "='" + PhNum + "'");

		Toast toast = Toast.makeText(con, "######UPDATED ", Toast.LENGTH_LONG);
		toast.show();

	}
	//Function to return "request msg",to display in dialog 
	public String getReqMessage(int buddyId) {

		Log.d("DB", "#########ID retreived : " + buddyId);
		Cursor cursor = db.rawQuery("SELECT " + COL_MESSAGE_MSG + " FROM "
				+ TABLE_MESSAGE + " WHERE " + COL_BUDDY_ID + " = " + buddyId,
				null);
		cursor.moveToFirst();
		Log.d("DB", "#########cursor count : " + cursor.getCount());
		if (cursor != null && cursor.getCount() > 0) {
			String message = cursor.getString(cursor
					.getColumnIndex(COL_MESSAGE_MSG));
			cursor.close();
			return message;
		} else {
			Log.d("DB", "#########cursor count : " + cursor.getCount());
		}

		return (null);

	}

	public int GetIdFromPhoneNo(String PhoneNo) {
		Cursor cursor = db.rawQuery("select " + COL_BUDDY_ID + " FROM "
				+ TABLE_BUDDY + " where " + COL_BUDDY_PHONE_NO + " = '"
				+ PhoneNo + "'", null);
		
		Cursor cursorNoRecs = db.rawQuery("select COUNT(*) FROM "
				+ TABLE_BUDDY + " where " + COL_BUDDY_PHONE_NO + " = '"
				+ PhoneNo + "'", null);
		
		int BuddyID=0;
		cursor.moveToFirst();
		cursorNoRecs.moveToFirst();
		
		int noOfRecs = cursorNoRecs.getInt(0);

		cursorNoRecs.close();
		System.out.println("############ no of Recs " + noOfRecs);
		if(noOfRecs == 0)
		{
			return -1;         // Buddy Request for this phoneNo does not exist !
						  
		}
		else if (noOfRecs == 1)
		{
			BuddyID = cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID));

			cursor.close();
			return BuddyID;   // Return Buddy Id corresponding to Phone no
		}
			
		
		return(0);
		
		
		
	}
	/* public List<BuddyInfo> getAllBuddies() {
		List<BuddyInfo> buddies = new ArrayList<BuddyInfo>();
		// execute select * from TABLE_BUDDY
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDDY, null);

		// iterate over the cursor
		cursor.moveToFirst();
		int n = cursor.getCount();
		for (int i = 0; i < n; i++) {
			BuddyInfo buddyInfo = new BuddyInfo();
			// populate buddy info here

			buddyInfo.setId(cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID)));
			Log.d("DB", "######BUDDY ID in getAllBuddies : "
					+ buddyInfo.getId());
			buddyInfo.setName(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_NAME)));
			buddyInfo.setNumber(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_PHONE_NO)));
			buddyInfo.setOnline_status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_ONLINE_STATUS)));
			buddyInfo.setReq_Status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_REQ_STATUS)));

			System.out.println(buddyInfo.getName()
					+ "reqstatus-- "
					+ (cursor.getInt(cursor
							.getColumnIndex(COL_BUDDY_REQ_STATUS))));

			cursor.moveToNext();

			buddies.add(buddyInfo);
 
		}
		cursor.close();
		return buddies;
	} */



	//Function to return location of particular buddy 
	public String getLocation(int buddyId) {

		Cursor cursor = db.rawQuery("SELECT " + COL_LOCATION_ADDRESS + " FROM "
				+ TABLE_LOCATION + " WHERE " + COL_LOCATION_ID + " = "
				+ buddyId, null);
		cursor.moveToFirst();
		String location = cursor.getString(cursor
				.getColumnIndex(COL_LOCATION_ADDRESS));
		cursor.close();

		System.out.println("inside location%%%%%%%%%5");
		return location;

	}



	//---------------Function for list operations------------------------------

	


	// Function which returns all buddies in list form 
	public List<BuddyInfo> getBuddiesForList() {

		List<BuddyInfo> buddies = new ArrayList<BuddyInfo>();
		Cursor cursor = null;
		int req = 0;

		// execute select * from TABLE_BUDDY
		cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDDY + " where "
				+ COL_BUDDY_REQ_STATUS + " != '3' ORDER BY +"
				+ COL_BUDDY_REQ_STATUS, null);
		cursor.moveToFirst();
		int n = cursor.getCount();
		// iterate over the cursor
		for (int i = 0; i < n; i++) {
			BuddyInfo buddyInfo = new BuddyInfo();
			// populate buddy info here
			Log.d("BudyyInfo", "#####BuddyId from database : "
					+ cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID)));
			buddyInfo.setId(cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID)));
			Log.d("BudyyInfo", "#####BuddyInfo while setting : "
					+ buddyInfo.getId());
			buddyInfo.setName(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_NAME)));
			buddyInfo.setNumber(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_PHONE_NO)));
			buddyInfo.setOnline_status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_ONLINE_STATUS)));
			buddyInfo.setReq_Status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_REQ_STATUS)));
			System.out.println(buddyInfo.getName()
					+ "reqstatus-- "
					+ (cursor.getInt(cursor
							.getColumnIndex(COL_BUDDY_REQ_STATUS))));

			cursor.moveToNext();
			// append to List
			buddies.add(buddyInfo);

		}

		cursor.close();
		return buddies;

	}

	
	
	//--------------------Functions for Service operations----------------------------
	
	// Function to get phone nos of all records in TABLE_BUDDY 
	public List<BuddyInfo> getBuddiesForPhoneNos() {

		List<BuddyInfo> buddies = new ArrayList<BuddyInfo>();
		Cursor cursor = null;
		int req = 0;

		// execute select * from TABLE_BUDDY
		cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDDY + " where "
				+ COL_BUDDY_REQ_STATUS + " = '0'"
				, null);
		cursor.moveToFirst();
		int n = cursor.getCount();
		// iterate over the cursor
		for (int i = 0; i < n; i++) {
			BuddyInfo buddyInfo = new BuddyInfo();
			// populate buddy info here
			Log.d("BudyyInfo", "#####BuddyId from database : "
					+ cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID)));
			buddyInfo.setId(cursor.getInt(cursor.getColumnIndex(COL_BUDDY_ID)));
			Log.d("BudyyInfo", "#####BuddyInfo while setting : "
					+ buddyInfo.getId());
			buddyInfo.setName(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_NAME)));
			buddyInfo.setNumber(cursor.getString(cursor
					.getColumnIndex(COL_BUDDY_PHONE_NO)));
			buddyInfo.setOnline_status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_ONLINE_STATUS)));
			buddyInfo.setReq_Status(cursor.getInt(cursor
					.getColumnIndex(COL_BUDDY_REQ_STATUS)));
			System.out.println(buddyInfo.getName()
					+ "reqstatus-- "
					+ (cursor.getInt(cursor
							.getColumnIndex(COL_BUDDY_REQ_STATUS))));

			cursor.moveToNext();
			// append to List
			buddies.add(buddyInfo);

		}

		cursor.close();
		return buddies;

	}
	
	// Function to get phone nos of all BUDDIES ( Req status = 0 )
	// Function used by service to broadcast location updates to buddies
	public String[] getAllBuddyPhoneNos() {

		String PhoneNos[] = new String[50];
		int i = 0;
		List<BuddyInfo> buddies = getBuddiesForPhoneNos();

		System.out.println("############ LIST STATUS---" + buddies.isEmpty());

		while (!buddies.isEmpty()) {

			PhoneNos[i] = buddies.get(0).getNumber();
			buddies.remove(0);
			i++;
			System.out.println("###### phno" + PhoneNos[i]);
		}

		return PhoneNos;
	}


	//Function used by service to update buddy locations received
	public void UpdateBuddyLocation(int id, Float latitude, Float longitude,
			String address) {

		String query = " UPDATE " + TABLE_LOCATION + " SET "
				+ COL_LOCATION_LATITUDE + " = " + latitude + ","
				+ COL_LOCATION_LONGITUDE + " = " + longitude + ","
				+ COL_LOCATION_ADDRESS + " = '" + address + "'  WHERE "
				+ COL_BUDDY_ID + " = " + id;

		db.execSQL(query);
		

		Log.d("location", "###########Location finally Updated");
	}


	
//--------------------------------Map Helper function --------------------
	
	// Function to return HashMap corresponding BuddyCoordinates and address string 
	// This is used by Locate.class for plotting on google map
	public HashMap<String, Location> getHashMapCoordinates(int oneOrAll) {

		int i = 0, n = 0;
		double lat = 0, lon = 0;
		String address = null , name = null;
		Cursor cursor = null;
		Cursor cursorNames = null;
		Location loc = null;
		HashMap<String, Location> result = new HashMap<String, Location>();

		if (oneOrAll == 0) // For ALL Buddies ( LOCATE ALL OPTION )
		{
			cursor = db.rawQuery("select " + COL_LOCATION_LATITUDE + ","
					+ COL_LOCATION_LONGITUDE + " , " + COL_LOCATION_ADDRESS
					+ " FROM " + TABLE_LOCATION, null);
			cursorNames = db.rawQuery("select " + COL_BUDDY_NAME + " FROM " 
					+ TABLE_BUDDY + " WHERE " 
					+ COL_BUDDY_REQ_STATUS + "= '0'" , null);
		}
		else    // For particular Buddy
		{
			cursor = db.rawQuery("select " + COL_LOCATION_LATITUDE + ","
					+ COL_LOCATION_LONGITUDE + " , " + COL_LOCATION_ADDRESS
					+ " FROM " + TABLE_LOCATION + " WHERE " + COL_LOCATION_ID
					+ " = " + BuddyTabs.buddyId, null);
			cursorNames = db.rawQuery("select " + COL_BUDDY_NAME + " FROM " 
					+ TABLE_BUDDY + " WHERE " 
					 + COL_BUDDY_ID + "=" + BuddyTabs.buddyId , null);

		}
		
		n = cursor.getCount();
		cursor.moveToFirst();
		cursorNames.moveToFirst();
		for (i = 1; i <= n; i++) {
			lat = cursor
					.getDouble(cursor.getColumnIndex(COL_LOCATION_LATITUDE));
			lon = cursor.getDouble(cursor
					.getColumnIndex(COL_LOCATION_LONGITUDE));
			address = cursor.getString(cursor
					.getColumnIndex(COL_LOCATION_ADDRESS));
			
			name = cursorNames.getString(cursorNames.getColumnIndex(COL_BUDDY_NAME));
			System.out.println(" ###########name is " + name);
			if (!(lat == 0 && lon == 0)) {
				loc = new Location("");
				loc.setLatitude(lat);
				loc.setLongitude(lon);
				// Append name and location to HashMap here
				result.put(name + " :: " + address, loc);
				System.out.println("#########lat" + lat);

				System.out.println("#########lon" + lon);
				
				
			}

			cursor.moveToNext();
			cursorNames.moveToNext();

		}
		cursor.close();
		cursorNames.close();

		return result;
	}

}

