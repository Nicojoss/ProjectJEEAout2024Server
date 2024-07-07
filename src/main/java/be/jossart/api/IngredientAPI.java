package be.jossart.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient_Server;

@Path("/ingredient")
public class IngredientAPI {
	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response CreateIngredientAndGetId(@FormParam("name") String name, 
			@FormParam("type") String type) {
		if(name == null || type == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		IngredientType ingredientType = IngredientType.valueOf(type);
		Ingredient_Server ingredient = new Ingredient_Server(name, ingredientType);
	
		if(!ingredient.create()) {
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
		else {
			return Response.status(Status.CREATED).entity(ingredient).build();
		}
	}
	
	@Path("/update")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response UpdateIngredient(@FormParam("id") int id,
			@FormParam("name") String name,
	        @FormParam("type") String type) {
	    try {
	        if (name == null || type == null) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }
	        IngredientType ingredientType = IngredientType.valueOf(type);
	        Ingredient_Server ingredient = new Ingredient_Server(id, name, 
	        		ingredientType, null);

	        if (!ingredient.update()) {
	            return Response.status(Status.NO_CONTENT)
	            		.build();
	        } 
	        else {
	            return Response.status(Status.SERVICE_UNAVAILABLE)
	            		.build();
	        }
	    } 
	    catch (IllegalArgumentException e) {
	        return Response.status(Status.BAD_REQUEST)
	        		.entity("Invalid ingredient type")
	        		.build();
	    }
	}

	@Path("/delete")
	@DELETE
	public Response DeleteIngredient(@FormParam("id") int id) {
		Ingredient_Server ingredient = new Ingredient_Server(id, null, null, null);
        if (!ingredient.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
	}
}
