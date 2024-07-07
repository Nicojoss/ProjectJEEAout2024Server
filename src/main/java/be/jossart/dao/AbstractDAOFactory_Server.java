package be.jossart.dao;

import be.jossart.javabeans.Ingredient_Server;
import be.jossart.javabeans.Person_Server;
import be.jossart.javabeans.RecipeIngredient_Server;
import be.jossart.javabeans.Recipe_Server;
import be.jossart.javabeans.RecipeStep_Server;

public abstract class AbstractDAOFactory_Server {
	
	public abstract DAO_Server<Person_Server> getPersonDAO();
	public abstract DAO_Server<Ingredient_Server> getIngredientDAO();
	public abstract DAO_Server<Recipe_Server> getRecipeDAO();
	public abstract DAO_Server<RecipeStep_Server> getRecipeStepDAO();
	public abstract DAO_Server<RecipeIngredient_Server> getRecipeIngredientDAO();

	public static AbstractDAOFactory_Server getFactory() {
		return new DAOFactory_Server();

	}
}
