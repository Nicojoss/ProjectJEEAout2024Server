package be.jossart.javabeans;

import java.io.Serializable;


import java.util.Objects;

import be.jossart.dao.AbstractDAOFactory_Server;
import be.jossart.dao.DAO_Server;
import be.jossart.dao.RecipeIngredientDAO_Server;

public class RecipeIngredient_Server implements Serializable{

	//ATTRIBUTES
	private static final long serialVersionUID = 869701072763507309L;
	private double quantity;
	private Ingredient_Server ingredient;
	private Recipe_Server recipe;
	private static final AbstractDAOFactory_Server adf = AbstractDAOFactory_Server.getFactory();
	private static final DAO_Server<RecipeIngredient_Server> recipeIngredientDAO = adf.getRecipeIngredientDAO();
	//CTOR
	public RecipeIngredient_Server() {}
	public RecipeIngredient_Server(double quantity, Ingredient_Server ingredient, 
			Recipe_Server recipe) {
		super();
		this.quantity = quantity;
		this.ingredient = ingredient;
		this.recipe = recipe;
	}
	//METHODS
	public boolean create() {
		return recipeIngredientDAO.create(this);
	}
	public boolean delete() {
		return recipeIngredientDAO.delete(this);
	}
	public boolean update() {
		return recipeIngredientDAO.update(this);
	}
	public static RecipeIngredient_Server find(int id) {
		RecipeIngredientDAO_Server dao = (RecipeIngredientDAO_Server) adf.getRecipeIngredientDAO();
		return dao.find(id);
	}
	public static RecipeIngredient_Server findRecipeIngredient(int idRecipe, int idIngredient) {
		RecipeIngredientDAO_Server dao = (RecipeIngredientDAO_Server) adf.getRecipeIngredientDAO();
		return dao.findRecipeIngredient(idRecipe, idIngredient);
	}
	//GETTERS AND SETTERS
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public Ingredient_Server getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredient_Server ingredient) {
		this.ingredient = ingredient;
	}
	public Recipe_Server getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe_Server recipe) {
		this.recipe = recipe;
	}
	@Override
	public String toString() {
		return "RecipeIngredient_Server [quantity="+ quantity + ", ingredient=" 
				+ ingredient + ", recipe=" + recipe + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(ingredient, quantity, recipe);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecipeIngredient_Server other = (RecipeIngredient_Server) obj;
		return Objects.equals(ingredient, other.ingredient)
				&& Double.doubleToLongBits(quantity) == Double.doubleToLongBits(other.quantity)
				&& Objects.equals(recipe, other.recipe);
	}
}
