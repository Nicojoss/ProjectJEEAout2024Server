package be.jossart.javabeans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

import be.jossart.connection.DbConnection;
import be.jossart.dao.AbstractDAOFactory_Server;
import be.jossart.dao.DAO_Server;
import be.jossart.dao.RecipeDAO_Server;

public class Recipe_Server implements Serializable{

	//ATTRIBUTES
	private static final long serialVersionUID = -7287441285222249732L;
	private int idRecipe;
	private String name;
	private Person_Server person;
	private RecipeGender recipeGender;
	private HashMap<Integer, Ingredient_Server> recipeIngredientList;
	private ArrayList<RecipeStep_Server> recipeStepList;
	private static final AbstractDAOFactory_Server adf = AbstractDAOFactory_Server.getFactory();
	private static final DAO_Server<Recipe_Server> recipeDAO = adf.getRecipeDAO();
	
	//CTOR
	public Recipe_Server() {
		recipeIngredientList = new HashMap<Integer, Ingredient_Server>();
		recipeStepList = new ArrayList<>();
	}
	public Recipe_Server(int idRecipe, String name, Person_Server person, RecipeGender recipeGender,
			HashMap<Integer, Ingredient_Server> recipeIngredientList, ArrayList<RecipeStep_Server> recipeStepList) {
        super();
        this.idRecipe = idRecipe;
        this.name = name;
        this.person = person;
        this.recipeGender = recipeGender;
        this.recipeIngredientList = recipeIngredientList;
        this.recipeStepList = recipeStepList;
    }
	public Recipe_Server(String name, Person_Server person, RecipeGender recipeGender,
			HashMap<Integer, Ingredient_Server> recipeIngredientList, ArrayList<RecipeStep_Server> recipeStepList) {
        super();
        this.name = name;
        this.person = person;
        this.recipeGender = recipeGender;
        this.recipeIngredientList = recipeIngredientList;
        this.recipeStepList = recipeStepList;
    }
	public Recipe_Server(int idRecipe,HashMap<Integer, Ingredient_Server> recipeIngredientList,
			ArrayList<RecipeStep_Server> recipeStepList) {
		super();
		this.idRecipe = idRecipe;
		this.recipeIngredientList = recipeIngredientList;
		this.recipeStepList = recipeStepList;
	}
	//METHODS
	public boolean create() {
		return recipeDAO.create(this);
	}
	public boolean delete() {
		return recipeDAO.delete(this);
	}
	public boolean update() {
		return recipeDAO.update(this);
	}
	public static Recipe_Server find(int id) {
		RecipeDAO_Server dao = (RecipeDAO_Server) adf.getRecipeDAO();
		return dao.find(id);
	}
	public static Recipe_Server findId(Recipe_Server recipe) {
		RecipeDAO_Server dao = (RecipeDAO_Server) adf.getRecipeDAO();
		return dao.findId(recipe);
	}
	public static List<Integer> findIds(int id) {
		RecipeDAO_Server dao = (RecipeDAO_Server) adf.getRecipeDAO();
		return dao.findIds(id);
	}
	//GETTERS SETTERS
	public int getIdRecipe() {
		return idRecipe;
	}
	public void setIdRecipe(int idRecipe) {
		this.idRecipe = idRecipe;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Person_Server getPerson() {
		return person;
	}
	public void setPerson(Person_Server person) {
		this.person = person;
	}
	public RecipeGender getRecipeGender() {
		return recipeGender;
	}
	public void setRecipeGender(RecipeGender recipeGender) {
		this.recipeGender = recipeGender;
	}
	public HashMap<Integer, Ingredient_Server> getRecipeIngredientList() {
		return recipeIngredientList;
	}
	public void setIngredientList(HashMap<Integer, Ingredient_Server> recipeIngredientList) {
		this.recipeIngredientList = recipeIngredientList;
	}
	public ArrayList<RecipeStep_Server> getRecipeStepList() {
		return recipeStepList;
	}
	public void setRecipeStepList(ArrayList<RecipeStep_Server> recipeStepList) {
		this.recipeStepList = recipeStepList;
	}
	public static List<Recipe_Server> searchRecipe(String recherche){
		RecipeDAO_Server daoRecipe = new RecipeDAO_Server(DbConnection.getInstance());
		
		return daoRecipe.findRecipeByName(recherche);
	}
	@Override
	public String toString() {
		return "Recipe_Server [idRecipe=" + idRecipe + ", name=" + name + ", person=" + person + ", recipeGender="
				+ recipeGender + ", ingredientList=" + recipeIngredientList + ", recipeStepList=" + recipeStepList + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(idRecipe, recipeIngredientList, name, person, recipeGender, recipeStepList);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe_Server other = (Recipe_Server) obj;
		return idRecipe == other.idRecipe && Objects.equals(recipeIngredientList, other.recipeIngredientList)
				&& Objects.equals(name, other.name) && Objects.equals(person, other.person)
				&& recipeGender == other.recipeGender && Objects.equals(recipeStepList, other.recipeStepList);
	}
}
