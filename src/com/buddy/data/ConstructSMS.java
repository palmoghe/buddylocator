// new file created -padma

// message former file, has pseudo code as shud be relevant to service

package com.buddy.data;


public class ConstructSMS
{
	public String formSMS(int type, int action)//also pass maybe a buddy object
	{
		String message = "BL:";
		String destNumber;
		switch(type)
		{
		case 0:
			// message += "ADD:1:" + buddyInfo.getName() + ":" + buddyInfo.getMessage();
			// destNumber = buddyInfo.getNumber();
			// add request to send
			break;
			
		case 1:
			// message += "ADD:0:";
			/*
			 * if (action ==0)
			 * 	message += "REJECT";
			 * else
			 * 	message += "ACCPET";
			 */
			// add ack/reject to send
			break;
			
		case 2:
			// message += "RMV:0";
			// remove request send
			break;
			
		case 3:			
			//remove done send sms
			//message += "RMV:1"
			break;
			
		case 4:
			//location update to send
			//message += "LOC:" + getlat + ":" + getlong;
			break;
						
		}
		
		return message;
		
	}
}