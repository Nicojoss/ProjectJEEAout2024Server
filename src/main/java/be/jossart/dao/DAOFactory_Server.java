package be.jossart.dao;

import java.sql.Connection;
import be.jossart.connection.DbConnection;
import be.jossart.javabeans.Ingredient_Server;
import be.jossart.javabeans.Person_Server;
import be.jossart.javabeans.RecipeIngredient_Server;
import be.jossart.javabeans.Recipe_Server;
import be.jossart.javabeans.RecipeStep_Server;

public class DAOFactory_Server extends AbstractDAOFactory_Server{

	protected static final Connection conn = DbConnection.getInstance();

	@Override
	public DAO_Server<Person_Server> getPersonDAO() {
		return new PersonDAO_Server(conn);
	}
	@Override
	public DAO_Server<Ingredient_Server> getIngredientDAO() {
		return new IngredientDAO_Server(conn);
	}
	@Override
	public DAO_Server<Recipe_Server> getRecipeDAO() {
		return new RecipeDAO_Server(conn);
	}
	@Override
	public DAO_Server<RecipeStep_Server> getRecipeStepDAO() {
		return new RecipeStepDAO_Server(conn);
	}
	@Override
	public DAO_Server<RecipeIngredient_Server> getRecipeIngredientDAO() {
		return new RecipeIngredientDAO_Server(conn);
	}
}
