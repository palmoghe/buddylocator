package com.buddy.data;

import java.io.Serializable;

/**
 * @author Sharadendu.sinha
 * 
 *         This is a standard java value object that is used to pass values from
 *         one place to another Ensure creating VOS like this for all forms
 *         where you are storing more than one information. Eg Contacts can be
 *         another VO.
 * 
 */
public class UserProfile implements Serializable
{
	int	id;
	String	name;
	String	password;
	String	securityQuestion;
	String	securityAnswer;


	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getSecurityQuestion()
	{
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion)
	{
		this.securityQuestion = securityQuestion;
	}

	public String getSecurityAnswer()
	{
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer)
	{
		this.securityAnswer = securityAnswer;
	}
}
