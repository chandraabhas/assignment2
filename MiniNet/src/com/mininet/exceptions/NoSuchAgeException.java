package com.mininet.exceptions;

/**
 * 
 * @author 
 *	when trying to enter a person whose age is negative or over 150. (You
 * can treat age as integer)
 */
public class NoSuchAgeException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NoSuchAgeException()
	{
		super("Invalid Age Entered");
	}
}