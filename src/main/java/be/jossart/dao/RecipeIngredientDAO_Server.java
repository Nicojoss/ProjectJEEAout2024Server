package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.jossart.javabeans.RecipeIngredient_Server;
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
			cs.setInt(1, obj.getIngredient().getIdIngredient());
            cs.setInt(2, obj.getRecipe().getIdRecipe());
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
    
    public RecipeIngredient_Server find(int idRecipe, int idIngredient) {
    	RecipeIngredient_Server recipeIngredient = null;
        String query = "{ call findRecipeIngredientById(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, idIngredient);
            cs.setInt(2, idRecipe); 
            cs.registerOutParameter(3, OracleTypes.CURSOR);

            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	                recipeIngredient = new RecipeIngredient_Server();
	                recipeIngredient.setIdRecipe(idRecipe);
	                recipeIngredient.setIdIngredient(idIngredient);
	                recipeIngredient.setQuantity(resultSet.getInt("Quantity"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipeIngredient;
    }
   

    public RecipeIngredient_Server findId(RecipeIngredient_Server recipeIngredient) {
        String query = "{ call findRecipeIngredientId(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setDouble(1, recipeIngredient.getQuantity());
            cs.setInt(2, recipeIngredient.getRecipe().getIdRecipe());
            cs.registerOutParameter(3, OracleTypes.CURSOR);
            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	            	recipeIngredient.setIdIngredient(resultSet.getInt("IdIngredient"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipeIngredient;
    }
    
    public List<Integer> findIds(int recipeId) {
        List<Integer> ingredientIds = new ArrayList<>();
        String query = "{ call getRecipeIngredientIdsByRecipe(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipeId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();

            try(ResultSet resultSet = (ResultSet) cs.getObject(2)){
            	while (resultSet.next()) {
                    int ingredientId = resultSet.getInt("IdIngredient");
                    ingredientIds.add(ingredientId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe ingredient IDs: " + e.getMessage());
        }
        return ingredientIds;
    }
}
