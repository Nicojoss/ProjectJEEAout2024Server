package be.jossart.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import be.jossart.javabeans.Ingredient_Server;
import be.jossart.javabeans.RecipeIngredient_Server;
import be.jossart.javabeans.Recipe_Server;

@Path("/RecipeIngredient")
public class RecipeIngredientAPI {
	@GET
	@Path("/{recipeId}/{ingredientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetRecipeIngredient(@PathParam("recipeId") int recipeId,
    		@PathParam("ingredientId") int ingredientId) {
        RecipeIngredient_Server recipeIngredient = RecipeIngredient_Server
        		.findRecipeIngredient(recipeId,ingredientId);
        if (recipeIngredient == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipeIngredient).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecipeIngredientAndGetId(@FormParam("recipeId") int recipeId,
            @FormParam("ingredientId") int ingredientId,
            @FormParam("quantity") double quantity) {
    	 Recipe_Server existingRecipe = Recipe_Server.find(recipeId);
    	 Ingredient_Server existingIngredient = Ingredient_Server.find(ingredientId);
         if (existingRecipe == null || existingIngredient == null) {
             return Response.status(Status.NOT_FOUND).build();
         }
         RecipeIngredient_Server recipeIngredient = new RecipeIngredient_Server(quantity,
        		 existingIngredient, existingRecipe);
         if (!recipeIngredient.create()) {
             return Response.status(Status.SERVICE_UNAVAILABLE).build();
         } else {
             return Response.status(Status.CREATED).entity(recipeIngredient).build();
         }
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateRecipeIngredient(RecipeIngredient_Server recipeIngredient) {
    	RecipeIngredient_Server existingRecipeIngredient = RecipeIngredient_Server.
    			findRecipeIngredient(recipeIngredient.getRecipe().getIdRecipe(),
    			recipeIngredient.getIngredient().getIdIngredient());
        if (existingRecipeIngredient == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        existingRecipeIngredient.setQuantity(recipeIngredient.getQuantity());
        if (!existingRecipeIngredient.update()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response DeleteRecipeIngredient(RecipeIngredient_Server recipeIngredient) {
        if (!recipeIngredient.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
}

