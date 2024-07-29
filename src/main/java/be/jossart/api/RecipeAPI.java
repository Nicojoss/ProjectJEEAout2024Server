package be.jossart.api;

import java.util.List;

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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{recherche}")
	public Response getRechercheRecipe(@PathParam("recherche") String recherche) {
		List<Recipe_Server> retour = null;
		System.out.println("rechercher : " +recherche);
		if (recherche.length()>=50|| recherche==null) {
			return Response.status(Status.BAD_REQUEST).build(); 
		}else {
			retour  = Recipe_Server.searchRecipe(recherche);
		}
		
		return Response.status(Status.OK).entity(retour).build();
	}
	
    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response CreateRecipeAndGetId(@FormParam("name") String name,
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
    /*@Path("/update")
    @PUT
    @FormParam(MediaType.APPLICATION_JSON)
    public Response UpdateRecipe(@FormParam("id") int id,
            @FormParam("name") String name,
            @FormParam("gender") String gender,
            @FormParam("idPerson") int idPerson) {
        try {
            if (name == null || gender == null) {
                return Response.status(Status.BAD_REQUEST).build();
            }

            RecipeGender recipeGender = RecipeGender.valueOf(gender);
            Person_Server person = new Person_Server(idPerson, null, null, null, null);
            Recipe_Server recipe = new Recipe_Server(id, name, person, recipeGender, null, null);

            if (!recipe.update()) {
                return Response.status(Status.NO_CONTENT)
                        .build();
            } else {
                return Response.status(Status.SERVICE_UNAVAILABLE)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Invalid recipe gender")
                    .build();
        }
    }*/

    @Path("/delete")
    @DELETE
    public Response DeleteRecipe(@FormParam("id") int id) {
    	Recipe_Server recipe = new Recipe_Server(id, null, null, null, null, null);
        if (!recipe.delete()) {
            return Response.status(Status.NO_CONTENT).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    
    
    
}
