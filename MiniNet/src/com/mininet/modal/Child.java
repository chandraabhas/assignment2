package com.mininet.modal;



import com.mininet.exceptions.NoSuchAgeException;

/**
 * 
 * @author 
 * A child
 */
public class Child  extends Person{

	public Child(String name, String image, String status, Sex sex, int age, State state) throws NoSuchAgeException {
		super(name, image, status, sex, age, state);

	}
	
	/**
	 * String representation of the Child
	 */
	@Override
	public String toString() {
		return "Child | "+getAge()+" | "+getName();
	}

	
	

}
