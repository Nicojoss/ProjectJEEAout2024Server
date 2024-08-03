package be.jossart.dao;

import java.sql.CallableStatement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient_Server;
import be.jossart.javabeans.RecipeGender;
import be.jossart.javabeans.RecipeStep_Server;
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
    public  HashMap<Integer, Ingredient_Server> GetRecipeIngredientsByRecipeId(int recipe_id) {
    	System.out.println("id recipe " + recipe_id);
		String query = "SELECT * FROM ingredient i INNER JOIN recipeingredient ri ON i.IDINGREDIENT = ri.IDINGREDIENT WHERE ri.IDRECIPE = ?";
		HashMap<Integer, Ingredient_Server> ingrdients = new HashMap<Integer, Ingredient_Server>();
        try (PreparedStatement preparedStatement = this.connect.prepareStatement(query)) {
            preparedStatement.setInt(1, recipe_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	while (resultSet.next()) {
                    int quantity = resultSet.getInt("QUANTITY");
                    int ingredient_id = resultSet.getInt("IDINGREDIENT");
                    String SGender= resultSet.getString("TYPEINGREDIENT");
                    IngredientType gender = null;
                    switch (SGender) {
                    case "Fruit":
                        gender = IngredientType.Fruit;
                        break;
                    case "Vegetable":
                        gender = IngredientType.Vegetable;
                        break;
                    case "vegetable":
                        gender = IngredientType.vegetable;
                        break;
                    case "Spicy":
                        gender = IngredientType.Spicy;
                        break;
                    default:
                        gender = IngredientType.Other;
                        break;
                }
                    String name = resultSet.getString("NAME");
                    Ingredient_Server ingredient = new Ingredient_Server(ingredient_id, name,gender, null) ;
                    ingrdients.put(quantity,ingredient);
                }
            } catch (SQLException e) {
    	        System.out.println("Error: " + e.getMessage());
    	    }

		} catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
		return ingrdients;
    }
}
