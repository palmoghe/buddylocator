// this file has all the methods to do with the tabbed screen.
// and also all the options on them

package com.buddy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

import com.buddy.adapter.BuddyAdapter;
import com.buddy.data.BuddyInfo;
import com.buddy.db.DBHelper;
import com.buddy.locate.Locate;
import com.buddy.services.SMSSender;

public class BuddyTabs extends Activity implements OnItemClickListener,
		DialogInterface.OnClickListener {
	String[] menu = { "Add Buddy", "Edit Profile", "Locate All",
			"Set Update Timer", "Exit" };
	private List<BuddyInfo> buddies = new ArrayList<BuddyInfo>();
	private static int status = 0;
	public static int buddyId = 0;
	public static String buddyName = null;
	public static String buddyPhoneNo = null;
	private BuddyAdapter adapter;
	Preferences pref = new Preferences();

	private enum Dialog {
		BUDDYOPTIONS, ACCEPTREJECT
	};

	private Dialog whichDialog;
	private int buddyPosition;
	private SMSSender sender;
	private static BuddyTabs instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		setContentView(R.layout.buddytabs);
			
		
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost); // The activity
		// TabHost
		tabHost.setup();
		TabSpec spec; // Reusable TabSpec for each tab
		// set the array adapter to map the string array to listview widget
		ListView home_menu = (ListView) findViewById(R.id.buddytabs_LV_menu);
		ArrayAdapter<String> menu_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu);
		home_menu.setAdapter(menu_adapter);
		home_menu.setOnItemClickListener(menu_listener);

		ListView list = (ListView) findViewById(R.id.buddytabs_buddies);
		buddies = DBHelper.getInstance(this).getBuddiesForList();
		adapter = new BuddyAdapter(this, buddies, R.layout.item);
		// Set adapter to display buddy list
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		// registerForContextMenu(list); // enable context(floating menu)
		// Initialize a TabSpec for each tab and add it to the TabHost

		spec = tabHost.newTabSpec("tag1").setIndicator("Buddies").setContent(
				R.id.buddytabs_buddies);
		tabHost.addTab(spec);
		// Do the same for the other tabs
		spec = tabHost.newTabSpec("tag2").setIndicator("Home").setContent(
				R.id.buddytabs_LV_menu);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0); // set current tab to buddies tab
		// Initialize the SMSSender object*/
		sender = new SMSSender(this);
	}

	public static void updateListBuddyList() {
		
		if(instance!=null)
		instance.updateList();
	}

	private void updateList() {
		// Retrieve updated buddies List
		Log.d("UPDATE", "#########in update list function");
		List<BuddyInfo> latestData = DBHelper.getInstance(this)
				.getBuddiesForList();
		buddies.removeAll(buddies);
		buddies.addAll(latestData);
		adapter.notifyDataSetChanged();
		System.out.println("!!adapter set !!!!!!!!!!!1");
	}

	// For buddy tab
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long rowid) {
		// TODO Auto-generated method stub
		System.out.println("LIST CLICKED -------");
		int status = buddies.get(position).getReq_Status();
		// String buddyName = buddies.get(position).getName();
		buddyId = buddies.get(position).getId();
		System.out.println("THE BUDDY ID IS #####################" + buddyId);
		buddyPhoneNo = buddies.get(position).getNumber();
		buddyName = buddies.get(position).getName();
		buddyPosition = position;
		String buddyOptions[] = { "LOCATE", "CALL", "REMOVE" };
		String requestOptions[] = { "ACCEPT", "REJECT" };
		// Builder for displaying main pop up menu with options
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (status == 0) {
			whichDialog = Dialog.BUDDYOPTIONS;
			builder.setTitle("OPTIONS");
			builder.setItems(buddyOptions, this);
		} else if (status == 1) {
			whichDialog = Dialog.ACCEPTREJECT;
			/*
			 * builder.setTitle("OPTIONS");
			 * builder.setItems(requestOptions,this);
			 */
			builder.setMessage(DBHelper.getInstance(this)
					.getReqMessage(buddyId));
			builder.setTitle(buddyName);
			builder.setPositiveButton("Accept",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							sender.setPhoneNumber(BuddyTabs.buddyPhoneNo);
							sender.SendAddAck("ACCEPT");
							Log.d("BuddyTab",
									"#####BUDDY ID before accept buddy"
											+ buddyId);
							DBHelper.getInstance(getApplicationContext())
									.acceptBuddy(buddyId);
							DBHelper.getInstance(getApplicationContext())
									.InsertLocation(buddyId);
							sender.SendLocationUpdate();
							// Update List
							updateListBuddyList();
						}
					});
			builder.setNegativeButton("Reject",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Call SMSSender method to send "ADD ACK"
							// appmessage to
							// reject buddy
							sender.setPhoneNumber(BuddyTabs.buddyPhoneNo);
							sender.SendAddAck("REJECT");
							DBHelper.getInstance(getApplicationContext())
									.rejectBuddy(buddyId);
							// Update List
							updateList();
						}
					});
			builder.setNeutralButton("Back",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

		}
		builder.show();
	}

	// For home tab
	OnItemClickListener menu_listener = new OnItemClickListener() // on menu
	// list
	// click
	{
		public void onItemClick(AdapterView<?> adapterView, View v,
				int position, long id) {
			Intent next_screen;
			switch (position)
			// add buddy selected
			{
			case 0:
				// open add buddy
				// screen, if this
				// selected
				next_screen = new Intent(BuddyTabs.this, AddBuddy.class);
				startActivity(next_screen);
				break;
			case 1:
				// if edit profile
				// selected
				// createDialog(position,
				// v.getContext());

				CustomDialog change_pass = new CustomDialog(v.getContext());
				change_pass.show();

				break;
			case 2:
				// locate all code goes
				// here
				// opens the maps screen
				Locate.LOCATEALL = true;
				Intent i = new Intent(BuddyTabs.this, Locate.class);
				startActivity(i);

				break;
			case 3:
				// change update timer
				// dialog opens
				createDialog(v.getContext());
				break;
			case 4:
				// exit the activity and the application
				finish();
				break;
			}
		}
	};

	public void onClick(DialogInterface dialog, int which) {
		// Create Builder for confirming Remove request ( Yes/No )
		AlertDialog.Builder builderRemove = new AlertDialog.Builder(this);
		// If dialog clicked was Buddy Options dialog
		if (whichDialog.equals(Dialog.BUDDYOPTIONS)) {
			switch (which) {
			case 0:
				// Create new intent for starting ShowOnMap activity

				Locate.LOCATEALL = false;
				Intent i = new Intent(BuddyTabs.this, Locate.class);
				startActivity(i);
				break;
			case 1:
				callBuddy(buddyPosition);
				break;
			case 2:
				builderRemove.setTitle(buddyName);
				builderRemove.setMessage("Do you wish to remove buddy?");
				builderRemove.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Remove buddy function call
								DBHelper.getInstance(getApplicationContext())
										.removeBuddy(buddyId);
								// update ListView
								System.out.println("removed!!!!!!!!");
								updateList();
								// Call SMSSender method to send "Remove"
								// appmessage
								// to buudy removed
								sender.setPhoneNumber(BuddyTabs.buddyPhoneNo);
								sender.SendRemove();
							}
						});
				builderRemove.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				builderRemove.show();
			}
		}

	}

	public void createDialog(Context con) {
		final String[] timer = { "5", "15", "30", "45", "60" }; // create a
																// strind array
																// for update
																// timer dialog

		AlertDialog.Builder builder = new AlertDialog.Builder(this); // create
																		// an
																		// alert
																		// dialog
		builder.setTitle("Set Update Timer (in mins)");

		builder.setSingleChoiceItems(timer, -1,
				new DialogInterface.OnClickListener() // create the list dialog
				{
					public void onClick(DialogInterface dialog, int which) {
						// check time selected and save appropriate settings
						int value = Integer.parseInt(timer[which]); // get value
																	// of item
																	// selected
						Preferences pObject = new Preferences(); // create
																	// prefernce
																	// file
																	// object
						pObject.writeInterval(getApplicationContext(), value); // save
																				// option
																				// selected
																				// by
																				// user
																				// in
																				// file
					}
				});
		builder.show(); // show the dialog
	}

	public void callBuddy(int position) {
		String number = "tel:" + buddies.get(position).getNumber().trim(); // get
																			// the
																			// phone
																			// number
																			// of
																			// selected
																			// buddy,
																			// attach
																			// tel:
																			// to
																			// it
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); // create
																				// a
																				// call
																				// intent
		startActivity(callIntent); // make the call
	}
}