package com.buddy.locate;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


public class tryItemizedOverlay extends ItemizedOverlay 
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	
	
	public tryItemizedOverlay(Drawable defaultMarker,Context context) 
	{
		super(boundCenterBottom(defaultMarker));
		  mContext=context;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected OverlayItem createItem(int i) 
	{

		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() 
	{
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);
	    populate();
	    
	}
	
	@Override
	public boolean onTap(int index) 
	{
		try
		{
	  OverlayItem item = mOverlays.get(index);
	 
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  
	  dialog.setTitle(item.getTitle());
	  
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  dialog.setCancelable(true);
	  	
		}
		catch(Exception e)
		{
			System.out.println("ERROR!!!!!!!!!!!");
			e.printStackTrace();
		}
	  return true;
	}

}



