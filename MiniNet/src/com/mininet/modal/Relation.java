package com.mininet.modal;

/**
 * 
 * @author 
 * Relation object
 */
public class Relation {

	/**
	 * String representation of the Relation
	 */
	@Override
	public String toString() {
		return "Relation [from=" + from + ", to=" + to + ", relation=" + relation + "]";
	}

	/**
	 * Person from which relation is made
	 */
	private Person from;

	/**
	 * Person to which the relation is referring
	 * 
	 * */
	private Person to;

	/**
	 * Type of relation
	 */
	private RelationType relation;


	public Relation(Person from, Person to, RelationType relation) {
		super();
		this.from = from;
		this.to = to;
		this.relation = relation;
	}

	/**
	 * 
	 * @return from
	 */
	public Person getFrom() {
		return from;
	}

	/**
	 * 
	 * @param from
	 */
	public void setFrom(Person from) {
		this.from = from;
	}

	/**
	 * 
	 * @return to
	 */
	public Person getTo() {
		return to;
	}

	/**
	 * 
	 * @param to
	 */
	public void setTo(Person to) {
		this.to = to;
	}

	/**
	 * 
	 * @return relation
	 */
	public RelationType getRelation() {
		return relation;
	}

	/**
	 * 
	 * @param relation
	 */
	public void setRelation(RelationType relation) {
		this.relation = relation;
	}

}
