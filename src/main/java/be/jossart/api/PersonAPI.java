package be.jossart.api;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import be.jossart.javabeans.Person_Server;

@Path("/person")
public class PersonAPI {
	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPerson(@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,
			@FormParam("username") String username, @FormParam("password") String password) {
		try {
				if(firstname == null || lastname == null || username == null || password == null) {
					return Response.status(Status.BAD_REQUEST).build();
				}
			
				Person_Server person = new Person_Server(firstname, lastname, username, password);
			
				if(!person.create()) {
					return Response.status(Status.SERVICE_UNAVAILABLE).build();
				}
				else {
					return Response.status(Status.CREATED).entity(person).build();
				}
			} catch (Exception e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}
	}
	
	@Path("/login")
	@POST
	@FormParam(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
		try {
			if (username == null || password == null) {
				return Response.status(Status.BAD_REQUEST).build();
		        }

		    Person_Server person = Person_Server.login(username, password);

		    if (person != null) {
		    	return Response.status(Status.OK).entity(person).build();
		    }else {
		    	return Response.status(Status.UNAUTHORIZED).build();
		    }        
		} catch (Exception e) {
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	@Path("/updatePassword")
	@PUT
	@FormParam(MediaType.APPLICATION_JSON)
	public Response updatePassword(@FormParam("idPerson") int idPerson, @FormParam("newPassword") String newPassword) {
	    try {
	    	if (idPerson == 0 || newPassword == null) {
				return Response.status(Status.BAD_REQUEST).build();
		        }
	        if (Person_Server.updatePassword(idPerson, newPassword) == true) {
	            return Response.status(Status.OK).build();
	        } else {
	            return Response.status(Status.NOT_MODIFIED).build();
	        }
	    } catch (Exception e) {
	        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
	    }
	}
}
