package com.mininet.exceptions;

/**
 * 
 * @author 
 *	when trying to make a couple when at least one member is not an
 *  adult.
 */
public class NotToBeCoupledException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NotToBeCoupledException()
	{
		super("at least one member is not an\n" + 
				" *  adult.");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NotToBeCoupledException(String msg) {
		super(msg);
	}
}