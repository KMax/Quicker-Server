package rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 */

@Path("note/{id}")
public class NoteResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of NoteResource */
    public NoteResource() {
    }

	/**
	 * Retrieves representation of an instance of rest.NoteResource
	 * @return an instance of java.lang.String
	 */
	@GET
    @Produces("application/xml")
	public String getXml(@PathParam("id") int id) {
		return "<note id='"+id+"' />";
	}
}
