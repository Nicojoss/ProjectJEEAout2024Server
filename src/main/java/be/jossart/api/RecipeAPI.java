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
import be.jossart.javabeans.Person_Server;
import be.jossart.javabeans.RecipeGender;
import be.jossart.javabeans.Recipe_Server;

@Path("/recipe")
public class RecipeAPI {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GetRecipe(@PathParam("id") int id) {
        Recipe_Server recipe = Recipe_Server.find(id);
        if (recipe == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(recipe).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecipe(@FormParam("name") String name,
            @FormParam("gender") String gender,
            @FormParam("idPerson") int idPerson) {
    	if (name == null || gender == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        RecipeGender recipeGender = RecipeGender.valueOf(gender);
        Person_Server person = new Person_Server(idPerson, null, null, null, null);
        Recipe_Server recipe = new Recipe_Server(0, name, person, recipeGender, null, null);
        if (!recipe.create()) {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        } else {
            return Response.status(Status.CREATED).entity(recipe).build();
        }
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateRecipe(@PathParam("id") int id,
            Recipe_Server recipe) {
        try {
            if (recipe.getName() == null || recipe.getRecipeGender() == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }
            Recipe_Server existingRecipe = Recipe_Server.find(id);
            if (existingRecipe == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            existingRecipe.setName(recipe.getName());
            existingRecipe.setRecipeGender(recipe.getRecipeGender());
            existingRecipe.setPerson(recipe.getPerson());
            if (!existingRecipe.update()) {
                return Response.status(Status.NO_CONTENT)
                        .build();
            } else {
                return Response.status(Status.OK)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Invalid recipe gender")
                    .build();
        }
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response DeleteRecipe(@PathParam("id") int id) {
    	Recipe_Server recipe = new Recipe_Server(id, null, null, null, null, null);
        if (!recipe.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.OK).build();
        }
    }
}
