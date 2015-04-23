package com.buddy.services;

import java.util.List;

import java.util.Locale;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;



import com.google.android.maps.GeoPoint;
import android.location.Address;


public class OurGeoCoder
{
	Context context;
	Geocoder geocoder;
	public OurGeoCoder(Context context)
	{
		this.context=context;
		geocoder=new Geocoder(context,Locale.getDefault());
	}
	
	public String getAddr(float lat,float longitude)
	{
        GeoPoint p = new GeoPoint((int) (lat*1E6),(int) (longitude*1E6));
        String add="";
        Log.d("GeoCoder","######lat:Long"+(int) (lat*1E6)+(int) (longitude*1E6));
        Log.d("GeoCoder","######Into the getAddr");
        try
        {
        	Log.d("GeoCoder","######Into the getAddr:try");
        	List<Address>address=geocoder.getFromLocation(p.getLatitudeE6()/1E6, p.getLongitudeE6()/1E6, 1);
        
			//String addr="";
        	Log.d("GeoCoder","######Into the getAddr:address from geocoder");
			if(address.size()>0)
			{
				for(int i=0;i<address.get(0).getMaxAddressLineIndex();i++)
					add+=address.get(0).getAddressLine(i)+"\n";
			}
			Log.d("OurGeocoder","########Location: "+add);
			
		
        }catch(Exception e){
        	e.printStackTrace();
        }
		return add;

	}

	
}
	
	

