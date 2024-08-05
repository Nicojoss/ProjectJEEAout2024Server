package be.jossart.javabeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import be.jossart.dao.AbstractDAOFactory;
import be.jossart.dao.DAO;
import be.jossart.dao.IngredientDAO;

public class Ingredient implements Serializable{
	//ATTRIBUTES
	private static final long serialVersionUID = 634419802561898936L;
	private int idIngredient;
	private String name;
	private IngredientType type;
	private ArrayList<Recipe> recipeList;
	private static final AbstractDAOFactory adf = AbstractDAOFactory.getFactory();
	private static final DAO<Ingredient> ingredientDAO = adf.getIngredientDAO();
	//CTOR
	public Ingredient() {
	}
	public Ingredient(int idIngredient, String name, IngredientType type,
			ArrayList<Recipe> recipeList) {
		this.idIngredient = idIngredient;
		this.name = name;
		this.type = type;
		this.recipeList = recipeList;
	}
	public Ingredient(String name, IngredientType type,
			ArrayList<Recipe> recipeList) {
		this.name = name;
		this.type = type;
		this.recipeList = recipeList;
	}
	public Ingredient(String name, IngredientType ingredientType) {
		this.name = name;
		this.type = ingredientType;
	}
	//METHODS
	public boolean create() {
		return ingredientDAO.create(this);
	}
	public boolean delete() {
		return ingredientDAO.delete(this);
	}
	public boolean update() {
		return ingredientDAO.update(this);
	}
	public static Ingredient find(int id) {
		IngredientDAO dao = (IngredientDAO) adf.getIngredientDAO();
		return dao.find(id);
	}
	public static HashMap<Double, Ingredient> GetRecipeIngredientsByRecipeId(int recipe_id) {
		IngredientDAO dao = (IngredientDAO) adf.getIngredientDAO();
		return dao.GetRecipeIngredientsByRecipeId(recipe_id);
	}
	//GETTERS SETTERS
	public int getIdIngredient() {
		return idIngredient;
	}
	public void setIdIngredient(int idIngredient) {
		this.idIngredient = idIngredient;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IngredientType getType() {
		return type;
	}
	public void setType(IngredientType type) {
		this.type = type;
	}
	public ArrayList<Recipe> getRecipeIngredientList() {
		return recipeList;
	}
	public void setRecipeIngredient(
			ArrayList<Recipe> recipeIngredientList) {
		this.recipeList = recipeIngredientList;
	}
	@Override
	public String toString() {
		return "Ingredient [idIngredient=" + idIngredient + ", "
				+ "name=" + name + ", type=" + type + ", recipeIngredient="
				+ recipeList + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(idIngredient, name, recipeList, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return idIngredient == other.idIngredient && Objects.equals(name, other.name)
				&& Objects.equals(recipeList, other.recipeList) && type == other.type;
	}
}