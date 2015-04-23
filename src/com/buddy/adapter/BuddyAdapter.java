/**
 * This file has the BuddyAdapter Class ( For Custom List )
 * An instance of this class would be created
 * The getView method gets called for every list item appended to list of buddies
 * used in  BuddyTabs.java
 * 
 */



package com.buddy.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buddy.R;
import com.buddy.data.BuddyInfo;
import com.buddy.db.DBHelper;

public class BuddyAdapter
	extends ArrayAdapter<BuddyInfo>
{
	private int	itemViewResouceId;

	public BuddyAdapter(Context context, List<BuddyInfo> objects, int itemViewResouceId)
	{
		super(context, android.R.layout.activity_list_item, objects);
		this.itemViewResouceId = itemViewResouceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		if(position < getCount())
		{
			// File file = getItem(position);
			if(convertView == null)
			{
				LayoutInflater vi = (LayoutInflater)((Activity)getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(itemViewResouceId, null);
			}
			TextView buddyName = (TextView)convertView.findViewById(R.id.item_TV_name);
			ImageView statusIcon = (ImageView)convertView.findViewById(R.id.item_IV_icon);
			TextView buddyLocation = (TextView)convertView.findViewById(R.id.item_TV_location);
			BuddyInfo currentBuddy = (BuddyInfo)getItem(position);
			
			int reqStatus = currentBuddy.getReq_Status();
			int buddyId = currentBuddy.getId();
			int onlineStatus = currentBuddy.getOnline_Status();
			
			
			// Building list according to Request status
			
			
			if(reqStatus == 0 && onlineStatus == 1)
			{
				statusIcon.setImageResource(R.drawable.buddyon);
				
				String location = DBHelper.getInstance(getContext()).getLocation(buddyId);
				buddyLocation.setText(location);
			}
			else if(reqStatus == 0 && onlineStatus == 0)
			{
				statusIcon.setImageResource(R.drawable.buddyoff);
				String location = DBHelper.getInstance(getContext()).getLocation(buddyId);
		
			     buddyLocation.setText(location);
				
			}
			else if(reqStatus == 1)
			{
				statusIcon.setImageResource(R.drawable.buddygot);
				buddyLocation.setText("");
							}
			else if(reqStatus == 2)
			{
				statusIcon.setImageResource(R.drawable.buddysent);
				buddyLocation.setText("");
							}
			buddyName.setText("" + currentBuddy.getName());
			System.out.println("BUDDY ADAPTER EXITS ...");
			// Set location in textview for location in list item
		}
		return convertView;
	}
}
