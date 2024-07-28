package be.jossart.dao;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import be.jossart.javabeans.Ingredient_Server;
import be.jossart.javabeans.RecipeIngredient_Server;
import be.jossart.javabeans.Recipe_Server;
import oracle.jdbc.OracleTypes;

public class RecipeIngredientDAO_Server extends DAO_Server<RecipeIngredient_Server>{

	public RecipeIngredientDAO_Server(Connection conn) {
		super(conn);
	}

	@Override
    public boolean create(RecipeIngredient_Server obj) {
		boolean success = false;
		String query = "{ call Insert_RecipeIngredient(?, ?, ?) }";
		try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getRecipe().getIdRecipe());
			cs.setInt(2, obj.getIngredient().getIdIngredient());
            cs.setDouble(3, obj.getQuantity());
			
			cs.executeUpdate(); 
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}		

    @Override
    public boolean delete(RecipeIngredient_Server obj) {
    	boolean success = false;
    	String query = "{ call Delete_RecipeIngredient(?, ?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getRecipe().getIdRecipe());
            cs.setInt(2, obj.getIngredient().getIdIngredient());

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

        return success;
    }   	

    @Override
    public boolean update(RecipeIngredient_Server obj) {
    	boolean success = false;
    	String query = "{ call Update_RecipeIngredient(?, ?, ?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
        	cs.setInt(1, obj.getRecipe().getIdRecipe());
            cs.setInt(2, obj.getIngredient().getIdIngredient());
            cs.setDouble(3, obj.getQuantity());

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

        return success;
    }

    
    @Override
	public RecipeIngredient_Server find(int id) {
		// TODO Auto-generated method stub
		return null;
	}
    
    public RecipeIngredient_Server findRecipeIngredient(int idRecipe, int idIngredient) {
    	Recipe_Server recipe = null;
    	Ingredient_Server ingredient = null;
    	RecipeIngredient_Server recipeIngredient = null;
        String query = "{ call findRecipeIngredientById(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, idRecipe);
            cs.setInt(2, idIngredient); 
            cs.registerOutParameter(3, OracleTypes.CURSOR);

            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	            	recipe = new Recipe_Server();
	            	recipe.setIdRecipe(idRecipe);
	            	ingredient = new Ingredient_Server();
	            	ingredient.setIdIngredient(idIngredient);
	                recipeIngredient = new RecipeIngredient_Server();
	                recipeIngredient.setIngredient(ingredient);
	                recipeIngredient.setRecipe(recipe);
	                recipeIngredient.setQuantity(resultSet.getInt("quantity"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipeIngredient;
    }
}
