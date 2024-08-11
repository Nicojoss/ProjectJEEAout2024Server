package be.jossart.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import be.jossart.javabeans.Ingredient;
import be.jossart.javabeans.Person;
import be.jossart.javabeans.RecipeGender;
import be.jossart.javabeans.RecipeStep;
import be.jossart.javabeans.Recipe;
import oracle.jdbc.OracleTypes;
import java.sql.*;


public class RecipeDAO extends DAO<Recipe> {

    public RecipeDAO(Connection conn) {
        super(conn);
    }
    @Override
	public  boolean create(Recipe obj) {
		boolean success = false;

		String query = "{ call Insert_Recipe(?, ?, ?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {

	        cs.setString(1, obj.getName());
	        cs.setString(2, obj.getRecipeGender().name());
	        cs.setInt(3, obj.getPerson().getIdPerson());
	        cs.registerOutParameter(4, java.sql.Types.INTEGER);

	        cs.executeUpdate();
	        obj.setIdRecipe(cs.getInt(4));
	        
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}

    @Override
    public boolean delete(Recipe obj) {
    	boolean success = false;
        String query = "{ call Delete_Recipe(?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getIdRecipe());

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage()+ "test recipe");
        }

        return success;
    }

    @Override
    public boolean update(Recipe obj) {
    	boolean success = false;
        String query = "{ call Update_Recipe(?, ?, ?, ?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getIdRecipe());
            cs.setString(2, obj.getName());
            cs.setString(3, obj.getRecipeGender().name());
            cs.setInt(4, obj.getPerson().getIdPerson());

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

        return success;
    }

    @Override
    public Recipe find(int id) {
    	Person person = null;
    	Recipe recipe = null;
    	String query = "{ call findRecipeById(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setInt(1, id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
	        cs.execute();
	        try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	                person = new Person();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                recipe = new Recipe();
	                int idRecipe = resultSet.getInt("IdRecipe");
	                recipe.setIdRecipe(idRecipe);
	                recipe.setName(resultSet.getString("Name"));
	                recipe.setPerson(person);
	                recipe.setRecipeGender(RecipeGender.valueOf(resultSet
	                		.getString("RecipeGender")));
	                recipe.setRecipeIngredientList(Ingredient.GetRecipeIngredientsByRecipeId(idRecipe));
	                recipe.setRecipeStepList((ArrayList<RecipeStep>) RecipeStep.GetRecipeStepsByRecipeId(idRecipe));;
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipe;
    }
    
    public List<Recipe> findRecipeByName(String recherche) {
        List<Recipe> retour = new ArrayList<>();
        String query = "{call Get_Recipe_By_Name(?, ?, ?)}";

        System.out.println("rechercher : " + recherche);

        try (CallableStatement cs = this.connect.prepareCall(query)) {
        	cs.setString(1, recherche);
        	cs.setString(2, recherche);
        	cs.registerOutParameter(3, OracleTypes.CURSOR);

        	cs.execute();

            try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
                while (resultSet.next()) {
                    int idRecette = resultSet.getInt("IdRecipe");
                    String nom = resultSet.getString("RecipeName");
                    String SGender = resultSet.getString("RecipeGender");
                    int idPerson = resultSet.getInt("IdPerson");

                    RecipeGender gender = null;
                    switch (SGender) {
                        case "Entree":
                            gender = RecipeGender.Entree;
                            break;
                        case "Dish":
                            gender = RecipeGender.Dish;
                            break;
                        case "Desserts":
                            gender = RecipeGender.Desserts;
                            break;
                        case "Cocktails":
                            gender = RecipeGender.Cocktails;
                            break;
                        case "VegetarianDishes":
                            gender = RecipeGender.VegetarianDishes;
                            break;
                        default:
                            gender = RecipeGender.Dish;
                            break;
                    }

                    Person person = Person.getPersonByPersonId(idPerson);
                    HashMap<Double, Ingredient> ingredients = Ingredient.GetRecipeIngredientsByRecipeId(idRecette);
                    ArrayList<RecipeStep> steps = (ArrayList<RecipeStep>) RecipeStep.GetRecipeStepsByRecipeId(idRecette);

                    retour.add(new Recipe(idRecette, nom, person, gender, ingredients, steps));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retour;
    }
    
    public List<Recipe> findPersonRecipes(int person_id) {
        List<Recipe> retour = new ArrayList<>();
        String query = "{call Get_Person_Recipes(?, ?)}";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
        	cs.setInt(1, person_id);
        	cs.registerOutParameter(2, OracleTypes.CURSOR);

        	cs.execute();

            try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
                while (resultSet.next()) {
                    int idRecette = resultSet.getInt("IdRecipe");
                    String nom = resultSet.getString("RecipeName");
                    String SGender = resultSet.getString("RecipeGender");
                    int idPerson = resultSet.getInt("IdPerson");

                    RecipeGender gender = null;
                    switch (SGender) {
                        case "Entree":
                            gender = RecipeGender.Entree;
                            break;
                        case "Dish":
                            gender = RecipeGender.Dish;
                            break;
                        case "Desserts":
                            gender = RecipeGender.Desserts;
                            break;
                        case "Cocktails":
                            gender = RecipeGender.Cocktails;
                            break;
                        case "VegetarianDishes":
                            gender = RecipeGender.VegetarianDishes;
                            break;
                        default:
                            gender = RecipeGender.Dish;
                            break;
                    }

                    Person person = Person.getPersonByPersonId(idPerson);
                    HashMap<Double, Ingredient> ingredients = Ingredient.GetRecipeIngredientsByRecipeId(idRecette);
                    ArrayList<RecipeStep> steps = (ArrayList<RecipeStep>) RecipeStep.GetRecipeStepsByRecipeId(idRecette);

                    retour.add(new Recipe(idRecette, nom, person, gender, ingredients, steps));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retour;
    }
    
    public boolean createRecipeIngredient(int recipeId, int ingredientId, double quantity) {
		boolean success = false;
		String query = "{ call Insert_RecipeIngredient(?, ?, ?) }";
		try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipeId);
			cs.setInt(2, ingredientId);
            cs.setDouble(3, quantity);
			cs.executeUpdate(); 
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}

    public boolean deleteRecipeIngredient(int recipeId, int ingredientId) {
    	boolean success = false;
    	String query = "{ call Delete_RecipeIngredient(?, ?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, recipeId);
            cs.setInt(2, ingredientId);

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage() + " test");
        }

        return success;
    }   	

    public boolean updateRecipeIngredient(int recipeId, int ingredientId, double quantity) {
    	boolean success = false;
    	String query = "{ call Update_RecipeIngredient(?, ?, ?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
        	cs.setInt(1, recipeId);
            cs.setInt(2, ingredientId);
            cs.setDouble(3, quantity);

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

        return success;
    }
    
    public Recipe findRecipeIngredient(int idRecipe, int idIngredient) {
    	Ingredient ingredient = null;
    	Recipe recipe = null;
    	HashMap<Double, Ingredient> recipeIngredientList = 
    			new HashMap<Double, Ingredient>();
        String query = "{ call findRecipeIngredientById(?, ?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, idRecipe);
            cs.setInt(2, idIngredient); 
            cs.registerOutParameter(3, OracleTypes.CURSOR);
            
            cs.execute();
            try (ResultSet resultSet = (ResultSet) cs.getObject(3)) {
	            if (resultSet.next()) {
	            	recipe = new Recipe();
	            	recipe.setIdRecipe(idRecipe);
	            	ingredient = new Ingredient();
	            	ingredient.setIdIngredient(idIngredient);
	            	recipeIngredientList.put(resultSet.getDouble("quantity"),ingredient);
	            	recipe.setRecipeIngredientList(recipeIngredientList);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipe;
    }
}
