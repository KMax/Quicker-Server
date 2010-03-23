package Quicker.Server.rest;

import Quicker.Server.UserAuthorization;
import Quicker.Server.db.NoteDatabase;
import com.xhive.error.xquery.XhiveXQueryException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

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
		Response r =null;
		try{
			userAuth.Auth(hh,user);
			String s = notedb.getNoteList(user);
			r = Response.ok(s).build();
		}catch(SecurityException se){
			r = Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Basic").build();
		}catch(XhiveXQueryException xxe){
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return r;
	}
}
