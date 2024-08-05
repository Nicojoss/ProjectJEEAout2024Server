package be.jossart.dao;

import be.jossart.javabeans.Ingredient;
import be.jossart.javabeans.Person;
import be.jossart.javabeans.Recipe;
import be.jossart.javabeans.RecipeStep;

public abstract class AbstractDAOFactory {
	
	public abstract DAO<Person> getPersonDAO();
	public abstract DAO<Ingredient> getIngredientDAO();
	public abstract DAO<Recipe> getRecipeDAO();
	public abstract DAO<RecipeStep> getRecipeStepDAO();

	public static AbstractDAOFactory getFactory() {
		return new DAOFactory();

	}
}
