package be.jossart.javabeans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Objects;
import be.jossart.dao.AbstractDAOFactory_Server;
import be.jossart.dao.DAO_Server;
import be.jossart.dao.PersonDAO_Server;


public class Person_Server implements Serializable {
	//Attributes
	private static final long serialVersionUID = -3448923763468846826L;
	private int idPerson;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private ArrayList<Recipe_Server> recipeList; // 0..* Donc c'est au moment ou la person va creer une recette qu'il faut l'initialiser
	private static final AbstractDAOFactory_Server adf = AbstractDAOFactory_Server.getFactory();
	private static final DAO_Server<Person_Server> personDAO = adf.getPersonDAO();
	//CTOR
	public Person_Server(int idPerson, String firstname, String lastname, String username, String password) {
		super();
		this.idPerson = idPerson;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
	}
	public Person_Server(String firstname, String lastname, String username, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
	}
	public Person_Server(int idPerson, String firstname, String lastname, String username) {
		this.idPerson = idPerson;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
	}
	public Person_Server() {
	}
	public Person_Server(String username, String password) {
		this.username = username;
		this.password = password;
	}
	//METHODS
	public boolean create() {
		return personDAO.create(this);
	}
	public static Person_Server login(String username, String password) {
		PersonDAO_Server dao = (PersonDAO_Server) adf.getPersonDAO();
		return dao.login(username, password);
	}
	public static boolean updatePassword(int idPerson, String newPassword) {
		PersonDAO_Server dao = (PersonDAO_Server) adf.getPersonDAO();
		return dao.updatePassword(idPerson, newPassword);
	}
	public static Person_Server find(int id) {
		PersonDAO_Server dao = (PersonDAO_Server) adf.getPersonDAO();
		return dao.find(id);
	}
	public static Person_Server findId(Person_Server person) {
		PersonDAO_Server dao = (PersonDAO_Server) adf.getPersonDAO();
		return dao.findId(person);
	}
	public static Person_Server getPersonByPersonId(int person_id) {
		PersonDAO_Server dao = (PersonDAO_Server) adf.getPersonDAO();
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
	public ArrayList<Recipe_Server> getRecipeList() {
		return recipeList;
	}
	public void setRecipeList(ArrayList<Recipe_Server> recipeList) {
		this.recipeList = recipeList;
	}
	@Override
	public String toString() {
		return "Person_Server [idPerson=" + idPerson + ", firstname=" + firstname + ", lastname=" + lastname
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
		Person_Server other = (Person_Server) obj;
		return Objects.equals(firstname, other.firstname) && idPerson == other.idPerson
				&& Objects.equals(lastname, other.lastname) && Objects.equals(password, other.password)
				&& Objects.equals(recipeList, other.recipeList) && Objects.equals(username, other.username);
	}
}
