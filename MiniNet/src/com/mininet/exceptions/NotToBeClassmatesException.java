package com.mininet.exceptions;

/**
 * 
 * @author 
 *	when trying to connect a child in a colleague relation.
 */
public class NotToBeClassmatesException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NotToBeClassmatesException()
	{
		super("trying to connect a child in a colleague relation.");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NotToBeClassmatesException(String msg) {
		super(msg);
	}
}