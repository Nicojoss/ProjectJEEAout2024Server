package be.jossart.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import be.jossart.javabeans.RecipeIngredient_Server;

@Path("/RecipeIngredient")
public class RecipeIngredientAPI {

	@Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecipeIngredientAndGetId(@FormParam("recipeId") int recipeId,
            @FormParam("ingredientId") int ingredientId,
            @FormParam("quantity") double quantity) {
    	 RecipeIngredient_Server recipeIngredient = new RecipeIngredient_Server(
         		recipeId, ingredientId, quantity, null, null);

         if (!recipeIngredient.create()) {
             return Response.status(Status.SERVICE_UNAVAILABLE).build();
         } else {
             return Response.status(Status.CREATED).entity(recipeIngredient).build();
         }
    }

    @Path("/update")
    @PUT
    @FormParam(MediaType.APPLICATION_JSON)
    public Response UpdateRecipeIngredient(@FormParam("recipeId") int recipeId,
            @FormParam("ingredientId") int ingredientId,
            @FormParam("quantity") double quantity) {
    	RecipeIngredient_Server recipeIngredient = new RecipeIngredient_Server(
        		recipeId, ingredientId, quantity, null, null);

        if (!recipeIngredient.update()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }

	@Path("/delete")
    @DELETE
    public Response DeleteRecipeIngredient(@FormParam("recipeId") int recipeId,
            @FormParam("ingredientId") int ingredientId) {
    	RecipeIngredient_Server recipeIngredient = new RecipeIngredient_Server(
        		recipeId, ingredientId, 0, null, null);
        if (!recipeIngredient.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }
}

