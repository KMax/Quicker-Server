package Quicker.Server.rest;

import Quicker.Server.UserAuthorization;
import Quicker.Server.db.NoteDatabase;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * REST Web-Service
 */

@Path("/{user}/note/")
@Stateless
public class NoteResource {
    @Context
    private UriInfo context;

	@Context
	private HttpHeaders hh;

	@EJB
	private NoteDatabase ndb;

	@EJB
	private UserAuthorization userAuth;

    /** Creates a new instance of NoteResource */
    public NoteResource() {
    }

	/**
	 * Посылает заметку по заданному id
	 * @param user - User login
	 * @param id - Id of note
	 * @return an instance of java.lang.String
	 */
	@Path("/{id}")
	@GET
    @Produces("application/atom+xml")
	public Response getNote(@PathParam("user") String user, @PathParam("id") int id) {
		Response r =null;
		try{
			userAuth.Auth(hh, user);
			r = Response.ok(ndb.getNoteById(user, id)).build();
		}catch(WebApplicationException wae){
			r = wae.getResponse();
		}
		return r;
	}

	/**
	 * Delete a note by id.
	 * @param user 
	 * @param id
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@Path("/{id}")
	@DELETE
	public Response deleteNote(@PathParam("user") String user, @PathParam("id") int id){
		Response r =null;
		try{
			userAuth.Auth(hh, user);
			ndb.deleteNote(user, id);
		}catch(WebApplicationException wae){
			r = wae.getResponse();
		}
		return r;
	}

	/**
	 * Update a note by id.
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@Path("/{id}")
	@PUT
	@Consumes("application/xml")
	public Response updateNote(){
		//FIXME Authentication...
		return Response.noContent().build();
	}

	/**
	 * Add new note.
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@POST
	@Consumes("application/xml")
	public Response addNote(){
		//FIXME Authentication...
		Response r;
		r = Response.created(context.getAbsolutePathBuilder().path("1").build()).build();
		return r;
	}
}
