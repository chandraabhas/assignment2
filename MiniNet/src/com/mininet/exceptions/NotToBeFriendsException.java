package com.mininet.exceptions;

/**
 * 
 * @author 
 *	- when trying to make an adult and a child friend or connect two
 *  children with an age gap larger than 3.
 */
public class NotToBeFriendsException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NotToBeFriendsException()
	{
		super("trying to make an adult and a child friend or connect two\n" + 
				" children with an age gap larger than 3.");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NotToBeFriendsException(String msg) {
		super(msg);
	}
}