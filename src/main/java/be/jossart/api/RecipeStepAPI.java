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

import be.jossart.javabeans.RecipeStep_Server;
import be.jossart.javabeans.Recipe_Server;

@Path("/recipeStep")
public class RecipeStepAPI {
	@GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipeStep(@PathParam("id") int id) {
        RecipeStep_Server recipeStep = RecipeStep_Server.find(id);
        if(recipeStep == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipeStep).build();
    }
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRecipeStepAndGetId(String jsonData) {
	    try {
	        JSONObject json = new JSONObject(jsonData);
	        String instruction = json.getString("instruction");
	        int recipeId = json.getInt("recipeId");

	        if (instruction == null || recipeId <= 0) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }

	        Recipe_Server recipe = new Recipe_Server(recipeId, null, null);
	        RecipeStep_Server recipeStep = new RecipeStep_Server(instruction, recipe);

	        if (!recipeStep.create()) {
	            return Response.status(Status.SERVICE_UNAVAILABLE).build();
	        } else {
	            return Response.status(Status.CREATED).entity(recipeStep).build();
	        }
	    } catch (JSONException ex) {
	        return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
	    }
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRecipeStep(String jsonData) {
	    try {
	        JSONObject json = new JSONObject(jsonData);
	        int idRecipeStep = json.getInt("idRecipeStep");
	        String instruction = json.getString("instruction");
	        int recipeId = json.getInt("recipeId");

	        if (instruction == null || recipeId <= 0) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }

	        RecipeStep_Server existingRecipeStep = RecipeStep_Server.find(idRecipeStep);
	        if (existingRecipeStep == null) {
	            return Response.status(Status.NOT_FOUND).build();
	        }

	        existingRecipeStep.setInstruction(instruction);
	        existingRecipeStep.setRecipe(new Recipe_Server(recipeId, null, null));

	        if (!existingRecipeStep.update()) {
	            return Response.status(Status.NO_CONTENT).build();
	        } else {
	            return Response.status(Status.OK).entity(existingRecipeStep).build();
	        }
	    } catch (JSONException ex) {
	        return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
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