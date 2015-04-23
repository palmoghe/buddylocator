package com.buddy.data;

import java.io.Serializable;

public class BuddyInfo implements Serializable
{
	String	name;
	String	number;
	String	message;
	int buddy_id;
	int req_status;
	int online_status;
	
	public int getReq_Status()
	{
		return req_status;
	}
	
	public int getId()
	{
		return buddy_id;
	}
	
	public void setReq_Status(int req_status)
	{
		this.req_status= req_status;
	
	}
	
	public void setOnline_status(int online_status)
	{
		 this.online_status=online_status;
	}
	
	public int getOnline_Status()
	{
		return online_status;
		
	}
	
	public void setId(int buddy_id)
	{
		 this.buddy_id=buddy_id;
		
	}
	
	
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

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
