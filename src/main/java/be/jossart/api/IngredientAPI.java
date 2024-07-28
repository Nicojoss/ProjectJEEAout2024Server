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

import be.jossart.javabeans.IngredientType;
import be.jossart.javabeans.Ingredient_Server;

@Path("/ingredient")
public class IngredientAPI {
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetIngredient(@PathParam("id") int id) {
		Ingredient_Server ingredient = Ingredient_Server.find(id);
		if(ingredient == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).entity(ingredient).build();
	}
	@POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
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
	@PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response UpdateIngredient(@PathParam("id") int id,
			Ingredient_Server ingredient) {
	    try {
	        if (ingredient.getName() == null || ingredient.getType() == null) {
	            return Response.status(Status.BAD_REQUEST).build();
	        }
	        Ingredient_Server existingIngredient = Ingredient_Server.find(id);
            if (existingIngredient == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            existingIngredient.setName(ingredient.getName());
            existingIngredient.setType(ingredient.getType());
	        if (!existingIngredient.update()) {
	            return Response.status(Status.NO_CONTENT)
	            		.build();
	        } 
	        else {
	            return Response.status(Status.OK)
	            		.build();
	        }
	    } 
	    catch (IllegalArgumentException e) {
	        return Response.status(Status.BAD_REQUEST)
	        		.entity("Invalid ingredient type")
	        		.build();
	    }
	}
	@DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response DeleteIngredient(@PathParam("id") int id) {
		Ingredient_Server ingredient = new Ingredient_Server(id, null, null, null);
        if (!ingredient.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
	}
}
