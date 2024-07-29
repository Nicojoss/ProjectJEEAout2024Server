package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.jossart.javabeans.Person_Server;
import be.jossart.javabeans.RecipeGender;
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
            try (ResultSet resultSet = (ResultSet) cs.getObject(4)) {
	            if (resultSet.next()) {
	            	recipe = new Recipe_Server();
	                recipeStep = new RecipeStep_Server();
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
    
    public RecipeStep_Server findId(RecipeStep_Server recipeStep) {
        String query = "{ call findRecipeStepId(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setString(1, recipeStep.getInstruction());
            cs.setInt(2, recipeStep.getRecipe().getIdRecipe());
            cs.registerOutParameter(3, OracleTypes.CURSOR);
            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	                recipeStep.setIdRecipeStep(resultSet.getInt("IdRecipeStep"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipeStep;
    }
    
    public List<Integer> findIds(int recipeId) {
        List<Integer> stepIds = new ArrayList<>();
        String query = "{ call getRecipeStepIdsByRecipe(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipeId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();

            try(ResultSet resultSet = (ResultSet) cs.getObject(2)){
            	while (resultSet.next()) {
                    int stepId = resultSet.getInt("IdRecipeStep");
                    stepIds.add(stepId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe step IDs: " + e.getMessage());
        }
        return stepIds;
    }
    public  List<RecipeStep_Server> GetRecipeStepsByRecipeId(int recipe_id) {
    	System.out.println("id recipe " + recipe_id);
		String query = "SELECT * FROM recipestep WHERE IDRECIPESTEP = ?";
		List<RecipeStep_Server> steps = new ArrayList<RecipeStep_Server>();
        try (PreparedStatement preparedStatement = this.connect.prepareStatement(query)) {
            preparedStatement.setInt(1, recipe_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	while (resultSet.next()) {
                    int idRecipeStep = resultSet.getInt("IDRECIPESTEP");
                    String INSTRUCTIONS = resultSet.getString("INSTRUCTIONS");
                    RecipeStep_Server step = new RecipeStep_Server(idRecipeStep,INSTRUCTIONS, null) ;
                    System.out.println(step);
                    steps.add(step);
                }
            } catch (SQLException e) {
    	        System.out.println("Error: " + e.getMessage());
    	    }

	} catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
		return steps;
}
    
}
