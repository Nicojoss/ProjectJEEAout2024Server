package be.jossart.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import be.jossart.javabeans.Ingredient;
import be.jossart.javabeans.Recipe;

@Path("/recipeIngredient")
public class RecipeIngredientAPI {
	@GET
	@Path("/{recipeId}/{ingredientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeIngredient(@PathParam("recipeId") int recipeId,
    		@PathParam("ingredientId") int ingredientId) {
        Recipe recipe = Recipe
        		.findRecipeIngredient(recipeId,ingredientId);
        if (recipe == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipe).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRecipeIngredient(String jsonData) {
    	try {
	    	JSONObject json = new JSONObject(jsonData);
	    	int recipeId = json.getInt("recipeId");
	    	int ingredientId = json.getInt("ingredientId");
	        double quantity = json.getDouble("quantity");
	        
			Recipe existingRecipe = Recipe.find(recipeId);
			Ingredient existingIngredient = Ingredient
					.find(ingredientId);
	    	 
			if (existingRecipe == null || existingIngredient == null) {
			    return Response.status(Status.NOT_FOUND).build();
			}

			if (!Recipe.createRecipeIngredient(recipeId,ingredientId,quantity)) {
			    return Response.status(Status.SERVICE_UNAVAILABLE).build();
			} else {
			    return Response.status(Status.CREATED).build();
			}
    	} catch (JSONException ex) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRecipeIngredient(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);
            int recipeId = json.getInt("recipeId");
            int ingredientId = json.getInt("ingredientId");
            double quantity = json.getDouble("quantity");

            Recipe existingRecipeIngredient = Recipe
                    .findRecipeIngredient(recipeId, ingredientId);
            
            if (existingRecipeIngredient == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            if (!Recipe.updateRecipeIngredient(recipeId, ingredientId, quantity)) {
                return Response.status(Status.NO_CONTENT).build();
            } else {
                return Response.status(Status.OK).build();
            }
        } catch (JSONException ex) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }
    @DELETE
    @Path("/{recipeId}/{ingredientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecipeIngredient(@PathParam("recipeId") int recipeId,
    		@PathParam("ingredientId") int ingredientId) {
    	Recipe existingRecipe = Recipe.find(recipeId);
		Ingredient existingIngredient = Ingredient
				.find(ingredientId);
    	 
		if (existingRecipe == null || existingIngredient == null) {
		    return Response.status(Status.NOT_FOUND).build();
		}

        if (!Recipe.deleteRecipeIngredient(recipeId, ingredientId)) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
}

