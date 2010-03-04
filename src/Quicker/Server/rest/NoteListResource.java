package Quicker.Server.rest;

import Quicker.Server.db.NoteDatabase;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 */

@Path("/{user}/notes/")
@Stateless
public class NoteListResource {
    @Context
    private UriInfo context;

	@EJB
	private NoteDatabase ndb;

    /** Creates a new instance of NoteListResource */
    public NoteListResource() {
    }

	/**
	 * Retrieves representation of an instance of rest.NoteListResource
	 * @param user
	 * @return an instance of java.lang.String
	 */
	@GET
    @Produces("application/atom+xml")
	public String getNoteList(@PathParam("user") String user) {
		//FIXME Авторизация
		return ndb.getNoteList(user);
	}
}
