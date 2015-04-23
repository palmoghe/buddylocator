// this activity helps you add buddies from your contact list.

package com.buddy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class AddBuddy extends Activity	implements OnClickListener, OnItemClickListener
{
	Cursor				cursor			= null;
	List<ContactInfo>	listOfContacts	= new ArrayList<ContactInfo>();

	private class ContactInfo
	{
		String	name	= null;
		String	number	= null;
										// getter and setter methods
		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getNumber()
		{
			return number;
		}

		public void setNumber(String number)
		{
			this.number = number;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbuddy);
		// if enter phone no. selected, screen to accept name, no,message opens
		String[] columns = new String[]
		                              { Contacts.Phones._ID, People.NAME, Contacts.Phones.NUMBER };
		cursor = getContentResolver().query(Contacts.Phones.CONTENT_URI, columns, null, null, null);
		startManagingCursor(cursor);
		// the desired columns to be bound
		// the XML defined views which the data will be bound to
		int[] to = new int[]
		{ R.id.name_entry, R.id.number_entry };
		// create the adapter using the cursor pointing to the desired data as
		// well as the layout information
		String[] subColumns = { People.NAME, Contacts.Phones.NUMBER };
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.entry, cursor, subColumns, to);
		// set this adapter as your ListActivity's adapter
		ListView lv = (ListView)findViewById(R.id.addbuddy_lv);
		System.out.println("LIST VIEW :" + lv);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(this);
		
		int n = cursor.getCount();
		cursor.moveToFirst();
		
		while(n > 0)
		{
			ContactInfo contact = new ContactInfo();
			contact.setName(cursor.getString(cursor.getColumnIndex(People.NAME)).toString());
			System.out.println("contact NAME %%%%%%5" + contact.getName());
			System.out.println("contact NUMBER %%%%%%5" + cursor.getString(cursor.getColumnIndex(People.NUMBER)));
			contact.setNumber(cursor.getString(cursor.getColumnIndex(People.NUMBER)));
			listOfContacts.add(contact);
			cursor.moveToNext();
			n = n - 1;
			System.out.println("N########" + n);
		}
		cursor.moveToFirst();
		Button enter_phone = (Button)findViewById(R.id.addbuddy_B_enterphone);
		// Set onClickListener for enter_phone button
		
		enter_phone.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent i = new Intent(AddBuddy.this, EnterBuddyAdd.class);
				startActivity(i);
			}
		});
		Button back = (Button)findViewById(R.id.addbuddy_B_back);
		// Set onclickListner for Back
		back.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		// Set onClickListener for Send Request
		
	}

	public void onClick(View v)
	{}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		ListView lv = (ListView)findViewById(R.id.addbuddy_lv);
		ContactInfo contact = listOfContacts.get(position);
		Intent callIntent = new Intent(getApplicationContext() , EnterBuddyAdd.class);
		callIntent.putExtra(EnterBuddyAdd.NAME, contact.getName());
		callIntent.putExtra(EnterBuddyAdd.NUMBER, contact.getNumber());
		startActivity(callIntent);
		finish();
		
	}
}