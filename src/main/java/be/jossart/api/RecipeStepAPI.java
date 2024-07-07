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

import be.jossart.javabeans.RecipeStep_Server;
import be.jossart.javabeans.Recipe_Server;

@Path("/recipeStep")
public class RecipeStepAPI {
    
    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecipeStepAndGetId(@FormParam("instruction") String instruction,
                                             @FormParam("recipeId") int recipeId) {
    	if(instruction == null || recipeId <= 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        Recipe_Server recipe = new Recipe_Server(recipeId, null, null);
        RecipeStep_Server recipeStep = new RecipeStep_Server(instruction, recipe);
    
        if(!recipeStep.create()) {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
        else {
            return Response.status(Status.CREATED).entity(recipeStep).build();
        }
    }
    
    @Path("/update")
    @PUT
    @FormParam(MediaType.APPLICATION_JSON)
    public Response UpdateRecipeStep(@FormParam("id") int id,
                                     @FormParam("instruction") String instruction,
                                     @FormParam("recipeId") int recipeId) {
    	if (instruction == null || recipeId <= 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Recipe_Server recipe = new Recipe_Server(recipeId, null, null);
        RecipeStep_Server recipeStep = new RecipeStep_Server(id, instruction, recipe);

        if (!recipeStep.update()) {
            return Response.status(Status.NO_CONTENT).build();
        } 
        else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    @Path("/delete")
    @DELETE
    public Response deleteRecipeStep(@FormParam("id") int id) {
    	RecipeStep_Server recipeStep = new RecipeStep_Server(id, null, null);
        if (!recipeStep.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }
}

