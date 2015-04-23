package com.buddy.services;

public class StoppableThread extends Thread
{
	protected boolean stop = false;
	
	public void stopThread()
	{
		stop = true;
		interrupt();		
	}	
}
