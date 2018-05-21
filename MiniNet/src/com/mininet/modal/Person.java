package com.mininet.modal;

import com.mininet.exceptions.NoSuchAgeException;

/**
 * 
 * @author 
 * Abstract class person
 */
public abstract class Person {
	
	/**
	 * name of the person
	 */
	private String name;
	
	/**
	 * image path of the Person
	 */
	private String image;
	
	/**
	 * status of the person
	 */
	private String status;
	
	/**
	 * age of the person
	 */
	private int age;
	
	/**
	 * state of the person
	 */
	private State state;
	
	/**
	 * sex of the person
	 */
	private Sex sex;
	
	/**
	 * Initialize person with passed data
	 * @param name name
	 * @param image image path
	 * @param status status
	 * @param sex sex
	 * @param age age
	 * @param state state
	 * @throws NoSuchAgeException if invalid age is entered
	 */
	public Person(String name, String image, String status,Sex sex, int age, State state) throws NoSuchAgeException {
		super();
		this.name = name;
		this.image = image;
		this.status = status;
		setAge(age);
		this.state = state;
		this.sex = sex;
	}
	
	/**
	 * String repreasentation of the person
	 */
	@Override
	public String toString() {
		return "Person [name=" + name + ", image=" + image + ", status=" + status + ", age=" + age + ", state=" + state
				+ ", sex=" + sex + "]";
	}
	
	/**
	 * 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set passed name to person
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return image
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}
	
	/**
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 
	 * @return age
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * 
	 * @param age 
	 * @throws NoSuchAgeException if invalid age is entered
	 */
	public void setAge(int age) throws NoSuchAgeException {
		if(age<0 || age >150)
			throw new NoSuchAgeException();
		this.age = age;
	}
	
	/**
	 * 
	 * @return state
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * 
	 * @param state
	 */
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * 
	 * @return Sex
	 */
	public Sex getSex() {
		return sex;
	}
	
	/**
	 * 
	 * @param sex
	 */
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
}
