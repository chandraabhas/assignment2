package com.mininet.exceptions;

/**
 * 
 * @author 
 *	- TooYoungException when trying to make friend with a young child.
 */
public class TooYoungException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public TooYoungException()
	{
		super("trying to make friend with a young child");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public TooYoungException(String string) {
		super(string);
	}
}
