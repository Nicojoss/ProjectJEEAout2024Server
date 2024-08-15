package be.jossart.javabeans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Objects;
import be.jossart.dao.AbstractDAOFactory;
import be.jossart.dao.DAO;
import be.jossart.dao.PersonDAO;


public class Person implements Serializable {
	//Attributes
	private static final long serialVersionUID = -3448923763468846826L;
	private int idPerson;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private ArrayList<Recipe> recipeList;
	private static final AbstractDAOFactory adf = AbstractDAOFactory.getFactory();
	private static final DAO<Person> personDAO = adf.getPersonDAO();
	//CTOR
	public Person(int idPerson, String firstname, String lastname, String username, String password) {
		super();
		this.idPerson = idPerson;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
	}
	public Person(String firstname, String lastname, String username, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
	}
	public Person(int idPerson, String firstname, String lastname, String username) {
		this.idPerson = idPerson;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
	}
	public Person() {
	}
	public Person(String username, String password) {
		this.username = username;
		this.password = password;
	}
	//METHODS
	public boolean create() {
		return personDAO.create(this);
	}
	public static Person login(String username, String password) {
		PersonDAO dao = (PersonDAO) adf.getPersonDAO();
		return dao.login(username, password);
	}
	public static boolean updatePassword(int idPerson, String newPassword) {
		PersonDAO dao = (PersonDAO) adf.getPersonDAO();
		return dao.updatePassword(idPerson, newPassword);
	}
	public static Person find(int id) {
		PersonDAO dao = (PersonDAO) adf.getPersonDAO();
		return dao.find(id);
	}
	public static Person findId(Person person) {
		PersonDAO dao = (PersonDAO) adf.getPersonDAO();
		return dao.findId(person);
	}
	public static Person getPersonByPersonId(int person_id) {
		PersonDAO dao = (PersonDAO) adf.getPersonDAO();
		return dao.getPersonByPersonId(person_id);
	}
	//GETTERS SETTERS
	public int getIdPerson() {
		return idPerson;
	}
	
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Recipe> getRecipeList() {
		return recipeList;
	}
	public void setRecipeList(ArrayList<Recipe> recipeList) {
		this.recipeList = recipeList;
	}
	@Override
	public String toString() {
		return "Person [idPerson=" + idPerson + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", username=" + username + ", password=" + password + ", recipeList=" + recipeList + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(firstname, idPerson, lastname, password, recipeList, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(firstname, other.firstname) && idPerson == other.idPerson
				&& Objects.equals(lastname, other.lastname) && Objects.equals(password, other.password)
				&& Objects.equals(recipeList, other.recipeList) && Objects.equals(username, other.username);
	}
}
