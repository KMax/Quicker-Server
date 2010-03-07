package Quicker.Server.rest;

import Quicker.Server.UserAuthorization;
import Quicker.Server.db.NoteDatabase;
import Quicker.Server.db.UserDatabase;
import com.sun.jersey.api.core.HttpRequestContext;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 */

@Path("/{user}/notes/")
@Stateless
public class NoteListResource {
    @Context
    private UriInfo context;

	@Context
	private HttpHeaders hh;

	@EJB
	private NoteDatabase notedb;

	@EJB
	private UserAuthorization userAuth;


    /** Creates a new instance of NoteListResource */
    public NoteListResource() {
    }

	/**
	 * Retrieves representation of an instance of rest.NoteListResource
	 * @param user
	 * @return an instance of java.lang.String
	 */
	@GET
    @Produces("application/xml")
	public Response getNoteList(@PathParam("user") String user) {
		userAuth.Auth(hh);
		String s = notedb.getNoteList(user);
		Response r = Response.ok(s).build();
		return r;
	}
}
