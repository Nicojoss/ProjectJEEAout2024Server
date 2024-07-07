package be.jossart.dao;

import java.sql.CallableStatement;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient_Server;
import oracle.jdbc.OracleTypes;

public class IngredientDAO_Server extends DAO_Server<Ingredient_Server>{

	public IngredientDAO_Server(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Ingredient_Server obj) {
			boolean success = false;

			String query = "{call Insert_Ingredient(?,?)}";
			try (CallableStatement cs = this.connect.prepareCall(query)) {

				cs.setString(1, obj.getName());
				cs.setString(2, obj.getType().toString());
				
				cs.executeUpdate(); 
				success = true;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			return success;
		}


	@Override
    public boolean delete(Ingredient_Server obj) {
		boolean success = false;

		String query = "{ call Delete_Ingredient(?) }";
		try (CallableStatement cs = this.connect.prepareCall(query)) {

			cs.setInt(1, obj.getIdIngredient());
			
			cs.executeUpdate(); 
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
    }

    @Override
    public boolean update(Ingredient_Server obj) {
    		boolean success = false;

    		String query = "{ call Update_Ingredient(?,?,?) }";
    		try (CallableStatement cs = this.connect.prepareCall(query)) {

    			cs.setInt(1, obj.getIdIngredient());
                cs.setString(2, obj.getName());
                cs.setString(3, obj.getType().name());
    			
    			cs.executeUpdate(); 
    			success = true;
    		} catch (SQLException e) {
    			System.out.println(e.getMessage());
    		}

    		return success;
     }

    @Override
    public Ingredient_Server find(int id) {
    	Ingredient_Server ingredient = null;

    	String query = "{ call findIngredientById(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	        cs.setInt(1, id);
	        cs.registerOutParameter(3, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	                ingredient = new Ingredient_Server();
	                ingredient.setIdIngredient(resultSet.getInt("IdIngredient"));
	                ingredient.setName(resultSet.getString("Name"));
	                ingredient.setType(IngredientType.valueOf(resultSet
	                		.getString("TypeIngredient")));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return ingredient;
    }
    
    public Ingredient_Server findId(Ingredient_Server ingredient) {
    	String query = "{ call findIngredientId(?, ?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setString(1, ingredient.getName());
            cs.setString(2, ingredient.getType().name());
	        cs.registerOutParameter(3, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	                ingredient = new Ingredient_Server();
	                ingredient.setIdIngredient(resultSet.getInt("IdIngredient"));
	                ingredient.setName(resultSet.getString("Name"));
	                ingredient.setType(IngredientType.valueOf(resultSet
	                		.getString("TypeIngredient")));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return ingredient;
    }
}
