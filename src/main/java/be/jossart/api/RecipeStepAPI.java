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

import be.jossart.javabeans.RecipeStep_Server;
import be.jossart.javabeans.Recipe_Server;

@Path("/recipeStep")
public class RecipeStepAPI {
	@GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetRecipeStep(@PathParam("id") int id) {
        RecipeStep_Server recipeStep = RecipeStep_Server.find(id);
        if(recipeStep == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipeStep).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
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
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateRecipeStep(@PathParam("id") int id,
    								 RecipeStep_Server recipeStep) {
    	if (recipeStep.getInstruction() == null || recipeStep.getRecipe().getIdRecipe() <= 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
    	RecipeStep_Server existingRecipeStep = RecipeStep_Server.find(id);
        if (existingRecipeStep == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        existingRecipeStep.setInstruction(recipeStep.getInstruction());
        if (!existingRecipeStep.update()) {
            return Response.status(Status.NO_CONTENT).build();
        } 
        else {
            return Response.status(Status.OK).build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRecipeStep(@PathParam("id") int id) {
    	RecipeStep_Server recipeStep = new RecipeStep_Server(id, null, null);
        if (!recipeStep.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
}

