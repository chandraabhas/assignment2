package com.mininet.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.mininet.dao.MininetDAO;
import com.mininet.exceptions.NoAvailableException;
import com.mininet.exceptions.NoParentException;
import com.mininet.exceptions.NoSuchAgeException;
import com.mininet.exceptions.NotToBeClassmatesException;
import com.mininet.exceptions.NotToBeColleaguesException;
import com.mininet.exceptions.NotToBeCoupledException;
import com.mininet.exceptions.NotToBeFriendsException;
import com.mininet.exceptions.TooYoungException;
import com.mininet.helper.MiniNetHelper;
import com.mininet.modal.AccountType;
import com.mininet.modal.Adult;
import com.mininet.modal.Child;
import com.mininet.modal.Person;
import com.mininet.modal.Relation;
import com.mininet.modal.RelationType;
import com.mininet.modal.Sex;
import com.mininet.modal.State;
import com.mininet.modal.YoungChild;

/**
 * 
 * @author 
 * MiniNetController class to handle all the background operations
 */
public class MiniNetController {
	/**
	 * List of persons in the application
	 */
	private List<Person> persons;

	/**
	 * List of relations currently in current application
	 */
	private List<Relation> relations;

	/**
	 * DAO object to handle database operations
	 */
	private MininetDAO dao;

	/**
	 * Default Constructor
	 * read persons list and relations list
	 */
	public MiniNetController() 
	{
		try
		{
			/**
			 * read persons list
			 */
			persons=MiniNetHelper.getPersons();
			/**
			 * read relations list of persons list
			 */
			relations=MiniNetHelper.getRelations(persons);

			/**
			 * if connection object is not null
			 * Initialize DAO class
			 */
			if(MiniNetHelper.getConnection()!=null)
				dao=new MininetDAO(MiniNetHelper.getConnection());
			/**
			 * check for data validations
			 * and discard invalid persons
			 */
			discardInValidData();
		}
		catch(NoSuchAgeException e)
		{
			System.err.println(e.getMessage());
		}
	}

	/**
	 * discard invalid data, if any in the persons list
	 * according to the relations file read
	 */
	private void discardInValidData() {
		//list to store persons to be removed
		List<Person> re=new ArrayList<>();
		for(Person p:persons)
		{
			//if person is child or younger child
			if(p instanceof Child || p instanceof YoungChild)
			{
				List<Person>parents=new ArrayList<>();
				for(Relation r: relations)
				{
					if(r.getFrom().equals(p))
					{
						if(r.getRelation()==RelationType.Parent)
						{
							parents.add(r.getTo());
						}
					}
				}
				//if there are no parents of child
				if(parents.isEmpty() || parents.size()>2)
					re.add(p);
				else if(removeParentNotFound(parents,p))
					re.add(p);
			}
		}
		//remove the invalid data from persons list
		for(int i=0;i<re.size();i++)
			persons.remove(re.get(i));

	}

	/**
	 * 
	 * @return list of persons
	 */
	public List<Person> getPersons() {
		return persons;
	}

	/**
	 * 
	 * @param name name of person 
	 * @return person with the passed name
	 * null, if no one is found
	 */
	public Person getPerson(String name) {
		for(Person p:persons)
		{
			if(p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}

	/**
	 * 
	 * @param parents Parents found for the child
	 * @param p Persons(child)
	 * @return true if the person is to be removed
	 */
	private boolean removeParentNotFound(List<Person> parents, Person p) {
		//male parent of child
		Person mParent=null;
		//female parent of child
		Person fParent=null; 
		for(Person pa:parents)
		{
			if(pa.getSex()==Sex.Male)
				mParent=pa;
			else if(pa.getSex()==Sex.Female)
				fParent=pa;
		}
		int count=0;
		for(Relation r:relations)
		{
			if(r.getFrom().equals(mParent) && r.getTo().equals(fParent) && r.getRelation()==RelationType.Couple)
				count++;
			else if(r.getFrom().equals(fParent) && r.getTo().equals(mParent) && r.getRelation()==RelationType.Couple)
				count++;
		}
		// if there are two parents return false else, true
		return count==2?false:true;
	}

	/**
	 * add person to database
	 * @param sacType Account type
	 * @param sName name of person
	 * @param sSex sex
	 * @param sAge age
	 * @param sState state
	 * @param sImage image String
	 * @param status status
	 * @return output to be shown to person
	 * @throws NoSuchAgeException if invalid age is entered
	 */
	public String addPerson(AccountType sacType, String sName, Sex sSex, String sAge, State sState, String sImage,String status) throws NoSuchAgeException{
		int age=checkAge(sAge);
		/**
		 * checking for valid age of Adult
		 */
		if(sacType==AccountType.Adult && age <=16)
		{
			return "Adult should have age above 16 years";
		}
		/**
		 * if name string is empty
		 */
		if(sName.equals(""))
		{
			return "Name can't be empty";
		}
		/**
		 * if a person with passed name already exists
		 */
		if(getPerson(sName)!=null)
		{
			return "Person with this name already exists";
		}
		/**
		 * call addPersonToDb with the Adult object
		 */
		return addPersonToDb(new Adult(sName, sImage, status, sSex, age, sState));
	}

	/**
	 * 
	 * @param person person to be added to db
	 * @return response to be displayed to the USer
	 */
	private String addPersonToDb(Person person) {
		//add to current list of persons
		persons.add(person);

		/**
		 * if dao object is not null
		 * insert that person to the database
		 */
		if(dao!=null)
			return dao.insertPerson(person);
		else
			return "Person successfully added to list of persons";

	}

	/**
	 * 
	 * @param sAge String age of person
	 * @return integer age
	 * @throws NoSuchAgeException if invalid age is enetered
	 */
	private int checkAge(String sAge) throws NoSuchAgeException{
		int age=0;
		try
		{
			age=Integer.parseInt(sAge);
			if(age<0 || age>150)
				throw new NoSuchAgeException();
		}
		catch(Exception e)
		{
			throw new NoSuchAgeException();
		}
		return age;
	}

	/**
	 * CHeck for data validations,
	 * and add the person details to db
	 * @param sacType Account Type
	 * @param sName name 
	 * @param sSex sex
	 * @param sAge age
	 * @param sState state
	 * @param sImage image string
	 * @param parent1 1st parent of child
	 * @param parent2 2nd parent of child
	 * @param status status
	 * @return Response to be displayed to the user
	 * @throws NoSuchAgeException if invalid age is enetered
	 */
	public String addPerson(AccountType sacType, String sName, Sex sSex, String sAge, State sState, String sImage,
			Person parent1, Person parent2,String status) throws NoSuchAgeException {

		//check age validation
		int age=checkAge(sAge);

		// check account type and age 
		if(sacType==AccountType.YoungChild && age >2)
			return "Young Child should have age less then equal to 2 years.";
		else if(sacType==AccountType.Child && age <=2 || age>16)
			return "Child should have age less between 2-16 years";
		if(getPerson(sName)!=null)
			return "Person with this name already exists";
		int count=0;

		/**
		 * get parents of the child
		 */
		for(Relation r:relations)
		{
			if(r.getFrom().equals(parent1) && r.getTo().equals(parent2) && r.getRelation()==RelationType.Couple)
				count++;
			if(r.getFrom().equals(parent2) && r.getTo().equals(parent1) && r.getRelation()==RelationType.Couple)
				count++;
		}
		/**
		 * if parents are found, make their objects
		 */
		if(count==2)
		{
			Person p=null;
			if(sacType==AccountType.Child)
			{
				p=new Child(sName, sImage, status, sSex, age, sState);
				addParentRelation(p,parent1,parent2);
				return addPersonToDb(p);
			}
			else
			{
				p=new YoungChild(sName, sImage, status, sSex, age, sState);
				addParentRelation(p,parent1,parent2);
				return addPersonToDb(p);
			}


		}
		else
			//if parents are not couple
			return "Invalid Parents Selected as they are not Couple.";
	}

	/**
	 * 
	 * @param p Child
	 * @param parent1 1st parent
	 * @param parent2 2nd parent
	 * Add the relation among the student and parents
	 * to the list
	 */
	private void addParentRelation(Person p, Person parent1, Person parent2) {
		Relation r1=new Relation(p, parent1, RelationType.Parent);
		Relation r2=new Relation(p, parent2, RelationType.Parent);
		relations.add(r1);
		relations.add(r2);
	}

	/**
	 * save the relations file and people file 
	 */
	public void persistData() {
		if(dao==null)
			savePeopleData();
		saveRelationsData();

	}

	/**
	 * Save the changes to relations file 
	 */
	private void saveRelationsData() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(MiniNetHelper.RELATION_FILENAME))) {
			for(Relation r:relations)
			{
				String str=r.getFrom().getName()+" , "+r.getTo().getName()+" , "+r.getRelation().toString()+"\n";
				bw.write(str);
			}
			bw.close();
			System.out.println("Relations data saved to \""+MiniNetHelper.RELATION_FILENAME+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Save the changes to people file 
	 */
	private void savePeopleData() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(MiniNetHelper.PEOPLE_FILENAME))) {
			for(Person p:persons)
			{
				String str=p.getName()+" , \""+p.getImage()+"\", \""+p.getStatus()+"\" , "+
						(p.getSex()==Sex.Male?"M":"F")+" , "+p.getAge()+" , "+p.getState().toString()+"\n";
				bw.write(str);
			}
			bw.close();
			System.out.println("People data saved to \""+MiniNetHelper.PEOPLE_FILENAME+"\"");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * 
	 * @param p person to be deleted
	 * @return Response to be displayed to the user
	 * @throws NoParentException if deleting the person will leave orphan child
	 */
	public String deletePerson(Person p) throws NoParentException {
		for(Relation r:relations)
		{
			if(r.getTo().equals(p) && RelationType.Parent==r.getRelation())
				throw new NoParentException("NoParentException : Deleting a parent is not allowed");
		}
		//delete all relations to the person
		deleteAllRelationsToPerson(p);
		persons.remove(p);
		/**
		 * if dao is not null, delete it from db
		 */
		if(dao!=null)
			return dao.deletePerson(p);
		return "Person successfully deleted from list";
	}

	/**
	 * 
	 * @param p person whose relations are to be deleted
	 * Delete all relation to or from for this person
	 */
	private void deleteAllRelationsToPerson(Person p) {
		List<Relation> re=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) || r.getTo().equals(p))
				re.add(r);
		for(int i=0;i<re.size();i++)
			relations.remove(re.get(i));		
	}

	/**
	 * return all the children of the passed person
	 * @param p person whose child you want to find
	 * @return Response to be displayed to the user
	 */
	public String getChildren(Person p) {
		List<Person> childrens=new ArrayList<>();
		for(Relation r:relations)
			if(r.getTo().equals(p) && r.getRelation()==RelationType.Parent)
				childrens.add(r.getFrom());
		if(childrens.isEmpty())
			return "No Children to display.\n";
		StringBuilder sb=new StringBuilder("List of Children : ").append("\n");
		return listToString(sb,childrens);
	}

	/**
	 * return the parents of the passed person
	 * @param p person whose parents you want to find
	 * @return Response to be displayed to the user
	 */
	public String getParents(Person p) {
		List<Person> parents=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) && r.getRelation()==RelationType.Parent)
				parents.add(r.getTo());
		if(parents.isEmpty())
			return "No Parents to display.\n";
		StringBuilder sb=new StringBuilder(" Parents : ").append("\n");
		return listToString(sb,parents);
	}

	/**
	 * return the string representation of passed list
	 * @param sb stringbuilder to append data
	 * @param childrens list of persons
	 * @return String representation of the list
	 */
	private String listToString(StringBuilder sb, List<Person> childrens) {
		for(Person p:childrens)
		{
			if(p instanceof Adult)
				sb.append(AccountType.Adult).append(" | ");
			else if(p instanceof YoungChild)
				sb.append(AccountType.YoungChild).append(" | ");
			else if(p instanceof Child)
				sb.append(AccountType.Child).append(" | ");
			sb.append(p.getName()).append(" | ");
			sb.append(p.getAge()).append(" | ");
			sb.append(p.getSex()).append(" | ");
			sb.append(p.getState()).append("\n");
		}
		return sb.toString();
	}

	/**
	 * return all the relations of the passed person
	 * @param p person whose relations you want to find
	 * @return Response to be displayed to the user
	 */
	public String getRelations(Person p) {
		StringBuilder sb=new StringBuilder();
		sb.append(getParents(p)).append("\n");
		sb.append(getChildren(p)).append("\n");
		sb.append(getColleagues(p)).append("\n");
		sb.append(getClassmates(p)).append("\n");
		sb.append(getCouple(p)).append("\n");
		sb.append(getFriends(p)).append("\n");
		return sb.toString();
	}

	/**
	 * return all the friends of the passed person
	 * @param p person whose friend you want to find
	 * @return Response to be displayed to the user
	 */
	private String getFriends(Person p) {
		List<Person> friends=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) && r.getRelation()==RelationType.Friends)
				friends.add(r.getTo());
		if(friends.isEmpty())
			return "No Friends to display.\n";
		StringBuilder sb=new StringBuilder(" My Friends : ").append("\n");
		return listToString(sb,friends);
	}

	/**
	 * return the couple of the passed person
	 * @param p person whose couple you want to find
	 * @return Response to be displayed to the user
	 */
	private String getCouple(Person p) {
		List<Person> couple=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) && r.getRelation()==RelationType.Couple)
				couple.add(r.getTo());
		if(couple.isEmpty())
			return "I am single.\n";
		StringBuilder sb=new StringBuilder(" My Partner : ").append("\n");
		return listToString(sb,couple);
	}

	/**
	 * return all the classmates of the passed person
	 * @param p person whose classmates you want to find
	 * @return Response to be displayed to the user
	 */
	private String getClassmates(Person p) {
		List<Person> classmates=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) && r.getRelation()==RelationType.Classmate)
				classmates.add(r.getTo());
		if(classmates.isEmpty())
			return "No classmates to display.\n";
		StringBuilder sb=new StringBuilder(" Classmates : ").append("\n");
		return listToString(sb,classmates);
	}

	/**
	 * return all the colleagues of the passed person
	 * @param p person whose colleagues you want to find
	 * @return Response to be displayed to the user
	 */
	private String getColleagues(Person p) {
		List<Person> colleagues=new ArrayList<>();
		for(Relation r:relations)
			if(r.getFrom().equals(p) && r.getRelation()==RelationType.Colleague)
				colleagues.add(r.getTo());
		if(colleagues.isEmpty())
			return "No colleagues to display.\n";
		StringBuilder sb=new StringBuilder(" Classmates : ").append("\n");
		return listToString(sb,colleagues);
	}

	/**
	 * 
	 * @param p1 person whose colleague you want to set
	 * @param p2 person to be added as colleague
	 * @return Response to be displayed to the user
	 * @throws NotToBeColleaguesException Making child as Colleague
	 */
	public String setColleague(Person p1, Person p2) throws NotToBeColleaguesException {
		if(!(p2 instanceof Adult))
			throw new NotToBeColleaguesException("NotToBeColleaguesException : "+p2.getName()+" is not adult and can't be a Colleague.");
		if(!(p1 instanceof Adult))
			throw new NotToBeColleaguesException("NotToBeColleaguesException : "+p1.getName()+" is not adult and can't be a Colleague.");
		Relation r1=new Relation(p1,p2,RelationType.Colleague);
		relations.add(r1);
		return p1.getName()+" isnow Colleague of  "+p2.getName()+" .";
	}

	/**
	 * 
	 * @param p1 person whose parent you want to set
	 * @param p2  person to be added as parent
	 * @return  Response to be displayed to the user
	 */
	public String setParent(Person p1, Person p2) {
		if(!(p2 instanceof Adult))
			return p1.getName()+" is not adult, so can't be a parent";

		Person fParent=null;
		Person mParent=null;
		for(Relation r:relations)
		{
			if(r.getFrom().equals(p1) && r.getRelation()==RelationType.Parent )
			{
				return "Parents for this child already exists";
			}

		}
		if(p2.getSex()==Sex.Male)
		{
			mParent=p2;
			for(Relation r:relations)
			{
				if(r.getFrom().equals(mParent) && r.getRelation()==RelationType.Couple && r.getTo().getSex()==Sex.Female)
				{
					fParent=r.getTo();
					break;
				}

			}
		}
		if(p2.getSex()==Sex.Female)
		{
			fParent=p2;
			for(Relation r:relations)
			{
				if(r.getFrom().equals(fParent) && r.getRelation()==RelationType.Couple && r.getTo().getSex()==Sex.Male)
				{
					mParent=r.getTo();
					break;
				}

			}
		}		
		if(fParent == null || mParent ==null)
		{
			return "Can't find the other couple for this Parent";
		}
		Relation r1=new Relation(p1,fParent,RelationType.Parent);
		Relation r2=new Relation(p1,mParent,RelationType.Parent);
		relations.add(r1);
		relations.add(r2);
		return fParent.getName()+" and "+mParent.getName()+" are now parents of "+p1.getName()+".";
	}

	/**
	 * 
	 * @param p1 person whose friend you want to make
	 * @param p2 person to be made friend
	 * @return Response to be displayed to the user
	 * @throws TooYoungException Young Child Can't be friend to someone
	 * @throws NotToBeFriendsException  When trying to make child and Adult a friend
	 */
	public String setFriend(Person p1, Person p2) throws TooYoungException, NotToBeFriendsException {
		if(p1 instanceof YoungChild || p2 instanceof YoungChild)
			throw new TooYoungException("Young Child Can't be friend to someone");
		if(p1 instanceof Child && p2 instanceof Adult )
		{
			if(p1.getAge()==16 && p2.getAge()==17)
				return addFriend(p1,p2);
			else
				throw new NotToBeFriendsException("NotToBeFriendsException : A child and Adult can't be friends");
		}
		if(p1 instanceof Adult && p2 instanceof Child)
		{
			if(p2.getAge()==16 && p1.getAge()==17)
				return addFriend(p1,p2);
			else 
				throw new NotToBeFriendsException("NotToBeFriendsException : A child and Adult can't be friends");
		}
		if(p1 instanceof Child && p2 instanceof Child)
		{
			if(p1.getAge()-p2.getAge()<=3)
			{
				return addFriend(p1, p2);
			}
			else 
				throw new NotToBeFriendsException("NotToBeFriendsException : Difference in children's age is more then 3 years");
		}
		else 
			return addFriend(p1, p2);
	}
	
	/**
	 * add relations as friend between two persons
	 * @param p1 person 1
	 * @param p2 person 2
	 * @return Response
	 */
	private String addFriend(Person p1, Person p2) {
		Relation r1=new Relation(p1, p2, RelationType.Friends);
		Relation r2=new Relation(p2, p1, RelationType.Friends);
		relations.add(r1);
		relations.add(r2);
		return p1.getName()+" and "+p2.getName()+" are now friends.";
	}
	/**
	 * 
	 * @param p1 person 1
	 * @param p2 person2
	 * @return Response to be displayed
	 * @throws NotToBeCoupledException  if the person is not adult
	 * @throws NoAvailableException If the person is already couple with someone else
	 */
	public String setCouple(Person p1, Person p2) throws NotToBeCoupledException, NoAvailableException {
		if(!(p1 instanceof Adult))
			throw new NotToBeCoupledException("NotToBeCoupledException : "+p1.getName()+" is not an adult");
		if(!(p2 instanceof Adult))
			throw new NotToBeCoupledException("NotToBeCoupledException : "+p1.getName()+" is not an adult");
		for(Relation r:relations)
		{
			if(r.getFrom().equals(p1)&& r.getRelation()==RelationType.Couple)
				throw new NoAvailableException("NoAvailableException : "+p1.getName()+" is not "
						+ "single and cant be a couple with "+p2.getName());
			if(r.getFrom().equals(p2)&& r.getRelation()==RelationType.Couple)
				throw new NoAvailableException("NoAvailableException : "+p2.getName()+" is not "
						+ "single and cant be a couple with "+p1.getName());
		}
		Relation r1=new Relation(p1, p2, RelationType.Couple);
		Relation r2=new Relation(p2, p1, RelationType.Couple);
		relations.add(r1);
		relations.add(r2);
		return p1.getName()+" and "+p2.getName()+" are now couples.";
	}
	
	/**
	 * 
	 * @param p1 person 1
	 * @param p2 person 2
	 * @return Response to be displayed to the user
	 * @throws NotToBeClassmatesException Young child can't be a friend
	 */
	public String setClassmate(Person p1, Person p2) throws NotToBeClassmatesException {
		if((p2 instanceof YoungChild))
			throw new NotToBeClassmatesException("NotToBeClassmatesException : "+p2.getName()+" is a Young Child adult and can't be a Classmate.");
		if((p1 instanceof YoungChild))
			throw new NotToBeClassmatesException("NotToBeClassmatesException : "+p1.getName()+" is a Young Child and can't be a Classmate.");
		Relation r1=new Relation(p1, p2, RelationType.Classmate);
		relations.add(r1);
		return p1.getName()+" is now a classmate of  "+p2.getName()+" .";
	}


}
