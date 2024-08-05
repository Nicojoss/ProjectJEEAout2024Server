package be.jossart.dao;

import java.sql.Connection;
import be.jossart.connection.DbConnection;
import be.jossart.javabeans.Ingredient;
import be.jossart.javabeans.Person;
import be.jossart.javabeans.Recipe;
import be.jossart.javabeans.RecipeStep;

public class DAOFactory extends AbstractDAOFactory{

	protected static final Connection conn = DbConnection.getInstance();

	@Override
	public DAO<Person> getPersonDAO() {
		return new PersonDAO(conn);
	}
	@Override
	public DAO<Ingredient> getIngredientDAO() {
		return new IngredientDAO(conn);
	}
	@Override
	public DAO<Recipe> getRecipeDAO() {
		return new RecipeDAO(conn);
	}
	@Override
	public DAO<RecipeStep> getRecipeStepDAO() {
		return new RecipeStepDAO(conn);
	}
}
