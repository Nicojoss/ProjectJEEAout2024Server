package be.jossart.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;

import be.jossart.javabeans.Person;
import be.jossart.javabeans.RecipeGender;
import be.jossart.javabeans.Recipe;

@Path("/recipe")
public class RecipeAPI {
	@GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipe(@PathParam("id") int id) {
        Recipe recipe = Recipe.find(id);
        if (recipe == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipe).build();
    }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search/{recherche}")
	public Response getRechercheRecipe(@PathParam("recherche") String recherche) {
		List<Recipe> retour = null;
		System.out.println("rechercher : " +recherche);
		if (recherche.length()>=50|| recherche==null) {
			return Response.status(Status.BAD_REQUEST).build(); 
		}else {
			retour  = Recipe.searchRecipe(recherche);
		}
		
		return Response.status(Status.OK).entity(retour).build();
	}
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRecipe(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);
            String name = json.getString("name");
            String gender = json.getString("gender");
            int idPerson = json.getInt("idPerson");

            if (name == null || gender == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }

            RecipeGender recipeGender = RecipeGender.valueOf(gender);
            Person person = new Person(idPerson, null, null, null, null);
            Recipe recipe = new Recipe(0, name, person, recipeGender, null, null);

            if (!recipe.create()) {
                return Response.status(Status.SERVICE_UNAVAILABLE).build();
            } else {
                return Response.status(Status.CREATED).entity(recipe).build();
            }
        } catch (JSONException ex) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRecipe(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);
            int idRecipe = json.getInt("idRecipe");
            String name = json.getString("name");
            RecipeGender recipeGender = RecipeGender.valueOf(json.getString("recipeGender"));
            int idPerson = json.getInt("idPerson");

            if (name == null || recipeGender == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }

            Recipe existingRecipe = Recipe.find(idRecipe);
            if (existingRecipe == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            Person person = new Person(idPerson, null, null, null, null);
            
            existingRecipe.setName(name);
            existingRecipe.setRecipeGender(recipeGender);
            existingRecipe.setPerson(person);

            if (!existingRecipe.update()) {
                return Response.status(Status.NO_CONTENT).build();
            } else {
                return Response.status(Status.OK).build();
            }
        } catch (JSONException ex) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid recipe gender").build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecipe(@PathParam("id") int id) {
    	Recipe recipe = new Recipe(id, null, null, null, null, null);
        if (!recipe.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
}
