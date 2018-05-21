package com.mininet.exceptions;

/**
 * 
 * @author 
 *	- when a child or young child has no parent or has only one parent, e.g.
 * adding a child to one adult, or to two adults who are not a couple. That also happens when trying
 * to delete an adult who has at least one dependent. (In this world a couple that have at least one kid
 * become inseparable and immortal.)
 */
public class NoParentException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572721681333819538L;
	/**
	 * default Constructor
	 */
	public NoParentException()
	{
		super("child or young child has no parent or has only one parent");
	}
	/**
	 * 
	 * @param string The message to be passed to the super class
	 */
	public NoParentException(String string)
	{
		super(string);
	}
}