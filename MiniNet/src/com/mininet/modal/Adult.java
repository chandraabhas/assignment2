package com.mininet.modal;



import com.mininet.exceptions.NoSuchAgeException;

/**
 * 
 * @author 
 * An adult
 */
public class Adult  extends Person{

	
	public Adult(String name, String image, String status, Sex sex, int age, State state) throws NoSuchAgeException {
		super(name, image, status, sex, age, state);

	}

	/**
	 * String representation of the Child
	 */
	@Override
	public String toString() {
		return "Adult | "+getAge()+" | "+getName();
	}
	
	
	
}
