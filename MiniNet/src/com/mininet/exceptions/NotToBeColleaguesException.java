package com.mininet.exceptions;

/**
 * 
 * @author 
 *	when trying to make a young child in a classmate relation.
 */
public class NotToBeColleaguesException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NotToBeColleaguesException()
	{
		super("trying to make a young child in a classmate relation.");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NotToBeColleaguesException(String msg) {
		super(msg);
	}
}