package Quicker.Server.rest;

import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 */

@Path("/{user}/notes/")
@Stateless
public class NoteListResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of NoteListResource */
    public NoteListResource() {
    }

	/**
	 * Retrieves representation of an instance of rest.NoteListResource
	 * @return an instance of java.lang.String
	 */
	@GET
    @Produces("application/xml")
	public String getNoteList() {
		return "<notes/>";
	}
}
