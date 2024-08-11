package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.jossart.javabeans.RecipeStep;
import be.jossart.javabeans.Recipe;
import oracle.jdbc.OracleTypes;

public class RecipeStepDAO extends DAO<RecipeStep>{

	public RecipeStepDAO(Connection conn) {
		super(conn);
	}

	@Override
    public boolean create(RecipeStep obj) {
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
    public boolean delete(RecipeStep obj) {
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
    public boolean update(RecipeStep obj) {
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
    public RecipeStep find(int id) {
    	RecipeStep recipeStep = null;
    	Recipe recipe = null;
        String query = "{ call findRecipeStepById(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	            	recipe = new Recipe();
	                recipeStep = new RecipeStep();
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
    public RecipeStep findId(RecipeStep recipeStep) {
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
    
    public List<RecipeStep> GetRecipeStepsByRecipeId(int recipe_id) {
        System.out.println("id recipe " + recipe_id);
        List<RecipeStep> steps = new ArrayList<>();
        
        String query = "{call Get_Recipe_Steps_By_Id(?, ?)}";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipe_id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
                while (resultSet.next()) {
                    int idRecipeStep = resultSet.getInt("IdRecipeStep");
                    String instructions = resultSet.getString("Instructions");
                    RecipeStep step = new RecipeStep(idRecipeStep, instructions, null);
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
