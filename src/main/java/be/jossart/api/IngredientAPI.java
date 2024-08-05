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

import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient;

@Path("/ingredient")
public class IngredientAPI {
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIngredient(@PathParam("id") int id) {
		Ingredient ingredient = Ingredient.find(id);
		if(ingredient == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).entity(ingredient).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createIngredient(String jsonData) {
	    try {
	        JSONObject json = new JSONObject(jsonData);
	        String name = json.getString("name");
	        String type = json.getString("type");

	        if (name == null || type == null) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }

	        IngredientType ingredientType = IngredientType.valueOf(type);
	        Ingredient ingredient = new Ingredient(name, ingredientType);

	        if (!ingredient.create()) {
	            return Response.status(Status.SERVICE_UNAVAILABLE).build();
	        } else {
	            return Response.status(Status.CREATED).entity(ingredient).build();
	        }
	    } catch (JSONException ex) {
	        return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
	    }
	}
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateIngredient(String jsonData) {
	    try {
	        JSONObject json = new JSONObject(jsonData);
	        int idIngredient = json.getInt("idIngredient");
	        String name = json.getString("name");
	        String type = json.getString("type");

	        if (name == null || type == null) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }

	        Ingredient existingIngredient = Ingredient.find(idIngredient);
	        if (existingIngredient == null) {
	            return Response.status(Status.NOT_FOUND).build();
	        }

	        IngredientType ingredientType = IngredientType.valueOf(type);

	        existingIngredient.setName(name);
	        existingIngredient.setType(ingredientType);

	        if (!existingIngredient.update()) {
	            return Response.status(Status.NO_CONTENT).build();
	        } else {
	            return Response.status(Status.OK).build();
	        }
	    } catch (JSONException ex) {
	        return Response.status(Status.BAD_REQUEST).entity("Invalid JSON format").build();
	    } catch (IllegalArgumentException ex) {
	        return Response.status(Status.BAD_REQUEST).entity("Invalid ingredient type").build();
	    }
	}
	@DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response deleteIngredient(@PathParam("id") int id) {
		Ingredient ingredient = new Ingredient(id, null, null, null);
        if (!ingredient.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
	}
}
