package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import be.jossart.javabeans.RecipeStep_Server;
import be.jossart.javabeans.Recipe_Server;
import oracle.jdbc.OracleTypes;

public class RecipeStepDAO_Server extends DAO_Server<RecipeStep_Server>{

	public RecipeStepDAO_Server(Connection conn) {
		super(conn);
	}

	@Override
    public boolean create(RecipeStep_Server obj) {
		boolean success = false;
        String query = "{ call Insert_RecipeStep(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setString(1, obj.getInstruction());
            cs.setInt(2, obj.getRecipe().getIdRecipe());
            cs.executeUpdate(); 
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
    }

    @Override
    public boolean delete(RecipeStep_Server obj) {
    	boolean success = false;
        String query = "{ call Delete_RecipeStep(?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getIdRecipeStep());
            cs.executeUpdate(); 
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
    }

    @Override
    public boolean update(RecipeStep_Server obj) {
    	boolean success = false;
        String query = "{ call Update_RecipeStep(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getIdRecipeStep());
            cs.setString(2, obj.getInstruction());
            cs.setInt(3, obj.getRecipe().getIdRecipe());
            
            cs.executeUpdate();
            success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return success;
    }

    @Override
    public RecipeStep_Server find(int id) {
    	RecipeStep_Server recipeStep = null;
    	Recipe_Server recipe = null;
        String query = "{ call findRecipeStepById(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	            	recipe = new Recipe_Server();
	                recipeStep = new RecipeStep_Server();
	                recipeStep.setIdRecipeStep(resultSet.getInt("IdRecipeStep"));
	                recipeStep.setInstruction(resultSet.getString("Instructions"));
	                recipe.setIdRecipe(resultSet.getInt("IdRecipe"));
	                recipeStep.setRecipe(recipe);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipeStep;
    }
}
