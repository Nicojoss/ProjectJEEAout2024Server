package be.jossart.dao;

import java.util.ArrayList;
import java.util.List;
import be.jossart.javabeans.Person_Server;
import be.jossart.javabeans.RecipeGender;
import be.jossart.javabeans.RecipeStep_Server;
import be.jossart.javabeans.Recipe_Server;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;

import java.sql.*;


public class RecipeDAO_Server extends DAO_Server<Recipe_Server> {

    public RecipeDAO_Server(Connection conn) {
        super(conn);
    }
    @Override
	public  boolean create(Recipe_Server obj) {
		boolean success = false;

		String query = "{ call Insert_Recipe(?, ?, ?) }";
		try (CallableStatement cs = this.connect.prepareCall(query)) {

			cs.setString(1, obj.getName());
            cs.setString(2, obj.getRecipeGender().name());
            cs.setInt(3, obj.getPerson().getIdPerson());
			
			cs.executeUpdate();
			success = true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return success;
	}

    @Override
    public boolean delete(Recipe_Server obj) {
    	boolean success = false;
        String query = "{ call Delete_Recipe(?) }";

        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, obj.getIdRecipe());

            cs.executeUpdate();
            success = true;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

        return success;
    }

    @Override
    public boolean update(Recipe_Server obj) {
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
    public Recipe_Server find(int id) {
    	Person_Server person = null;
    	Recipe_Server recipe = null;
    	String query = "{ call findRecipeById(?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setInt(1, id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
	        cs.execute();
	        try (ResultSet resultSet = (ResultSet) cs.getObject(5)) {
	            if (resultSet.next()) {
	                person = new Person_Server();
	                person.setIdPerson(resultSet.getInt("IdPerson"));
	                recipe = new Recipe_Server();
	                recipe.setIdRecipe(resultSet.getInt("IdRecipe"));
	                recipe.setName(resultSet.getString("Name"));
	                recipe.setPerson(person);
	                recipe.setRecipeGender(RecipeGender.valueOf(resultSet
	                		.getString("RecipeGender")));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	    return recipe;
    }

    public Recipe_Server findId(Recipe_Server recipe) {
        String query = "{ call findRecipeId(?, ?, ?) }";
	    try (CallableStatement cs = this.connect.prepareCall(query)) {
	    	cs.setString(1, recipe.getName());
            cs.setString(2, recipe.getRecipeGender().toString());
            cs.setInt(3, recipe.getPerson().getIdPerson());
            cs.registerOutParameter(4, OracleTypes.CURSOR);

	        cs.execute();

	        try (ResultSet resultSet = (ResultSet) cs.getObject(2)) {
	            if (resultSet.next()) {
	                recipe.setIdRecipe(resultSet.getInt("IdRecipe"));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error: " + e.getMessage());
	    }

	    return recipe;
    }
    
    public List<Integer> findIds(int personId) {
        List<Integer> recipeIds = new ArrayList<>();
        String query = "{ call getRecipeIdsByPerson(?, ?) }";
        try (CallableStatement cs = this.connect.prepareCall(query)) {
            cs.setInt(1, personId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            cs.execute();

            try (ResultSet resultSet = (ResultSet) cs.getObject(2)){
            	while (resultSet.next()) {
                    int recipeId = resultSet.getInt("idRecipe");
                    recipeIds.add(recipeId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe IDs by person: " + e.getMessage());
        }
        return recipeIds;
    }
    
    public List<Recipe_Server> findRecipeByName(String recherche) {
        List<Recipe_Server> retour = new ArrayList<>();
        String callFunction = "SELECT * FROM recipe WHERE name = ?";

        System.out.println("rechercher : " + recherche);

        try (PreparedStatement preparedStatement = this.connect.prepareStatement(callFunction)) {
            preparedStatement.setString(1, recherche);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int idRecette = resultSet.getInt("IDRECIPE");
                    String nom = resultSet.getString("name");
                    String SGender = resultSet.getString("RECIPEGENDER");
                    int idPerson = resultSet.getInt("IDPERSON");
                    //System.out.println("ID Recette: " + idRecette + ", Titre: " + nom + ", Famille: " + SGender);
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
                    Person_Server person = Person_Server.getPersonByPersonId(idPerson);
                    ArrayList<RecipeStep_Server> steps = (ArrayList<RecipeStep_Server>) RecipeStep_Server.GetRecipeStepsByRecipeId(idRecette);
                    retour.add(new Recipe_Server(idRecette, nom, person, gender, null, steps));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retour;
    }

}
