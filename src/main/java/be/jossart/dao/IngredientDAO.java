package be.jossart.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient;
import oracle.jdbc.OracleTypes;

public class IngredientDAO extends DAO<Ingredient>{

	public IngredientDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Ingredient obj) {
		boolean success = false;

		String query = "{call Insert_Ingredient(?,?,?)}";
		try (CallableStatement cs = this.connect.prepareCall(query)) {

			cs.setString(1, obj.getName());
			cs.setString(2, obj.getType().toString());
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			
			cs.executeUpdate();
	        obj.setIdIngredient(cs.getInt(3));
			
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}


	@Override
    public boolean delete(Ingredient obj) {
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
    public boolean update(Ingredient obj) {
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
    public Ingredient find(int id) {
    	Ingredient ingredient = null;

    	String query = "{ call findIngredientById(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	        cs.setInt(1, id);
	        cs.registerOutParameter(2, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	                ingredient = new Ingredient();
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
    
    public HashMap<Double, Ingredient> GetRecipeIngredientsByRecipeId(int recipe_id) {
        HashMap<Double, Ingredient> ingredients = new HashMap<>();
        
        String query = "{call Get_Recipe_Ingredients(?, ?)}";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipe_id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();

            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
                while (resultSet.next()) {
                    double quantity = resultSet.getDouble("Quantity");
                    int ingredient_id = resultSet.getInt("IdIngredient");
                    String SGender = resultSet.getString("TypeIngredient");
                    
                    IngredientType gender;
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

                    String name = resultSet.getString("IngredientName");
                    Ingredient ingredient = new Ingredient(ingredient_id, name, gender, null);
                    ingredients.put(quantity, ingredient);
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return ingredients;
    }
}
