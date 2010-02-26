package rest;

import java.net.URI;
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
import javax.ws.rs.core.Response;

/**
 * REST Веб-сервис
 */

@Path("/note/")
public class NoteResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of NoteResource */
    public NoteResource() {
    }

	/**
	 * Посылает заметку по заданному id
	 * @param id
	 * @return an instance of java.lang.String
	 */
	@Path("/{id}")
	@GET
    @Produces("application/xml")
	public String getNote(@PathParam("id") int id) {
		return "<note id='"+id+"' ><title>Пробная заметка</title>" +
				"<text>блая блая блая блая блая блая</text></note>";
	}

	/**
	 * Delete a note by id.
	 * @param id
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@Path("/{id}")
	@DELETE
	public Response deleteNote(@PathParam("id") int id){
		return Response.noContent().build();
	}

	/**
	 * Update a note by id.
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@Path("/{id}")
	@PUT
	@Consumes("application/xml")
	public Response updateNote(){
		return Response.noContent().build();
	}

	/**
	 * Add new note.
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@POST
	@Consumes("application/xml")
	public Response addNote(){
		return Response.noContent().build();
	}
}
