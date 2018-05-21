package com.mininet.exceptions;

/**
 * 
 * @author 
 *	when trying to make two adults a couple and at least one of them is
 *  already connected with another adult as a couple.
 */
public class NoAvailableException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NoAvailableException()
	{
		super("one of Person is\n" + 
				" *  already connected with another adult as a couple");
	}
	
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NoAvailableException(String string) {
		super(string);
	}
}