/***************************************************************************
*
*	Copyright 2010 Quicker Team
*
*	Quicker Team is:
*		Kirdeev Andrey (kirduk@yandex.ru)
* 	Koritniy Ilya (korizzz230@bk.ru)
* 	Kolchin Maxim	(kolchinmax@gmail.com)
*/
/****************************************************************************
*
*	This file is part of Quicker.
*
*	Quicker is free software: you can redistribute it and/or modify
*	it under the terms of the GNU Lesser General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	Quicker is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*	GNU Lesser General Public License for more details.
*
*	You should have received a copy of the GNU Lesser General Public License
*	along with Quicker. If not, see <http://www.gnu.org/licenses/>


****************************************************************************/
package Quicker.Server.rest;

import Quicker.Server.UserAuthorization;
import Quicker.Server.db.NoteDatabase;
import com.xhive.error.xquery.XhiveXQueryException;
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
    @Produces("application/xml")
	public Response getNote(@PathParam("user") String user, @PathParam("id") int id) {
		Response r =null;
		try{
			userAuth.Auth(hh, user);
			r = Response.ok(ndb.getNoteById(user, id)).build();
		}catch(SecurityException se){
			r = Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Basic").build();
		}catch(XhiveXQueryException xxe){
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return r;
	}

//	@Path("/{id}/{blobName}")
//	@GET
//	@Produces("*/*")
//	public Response

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
			r = Response.ok().build();
		}catch(SecurityException se){
			r = Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Basic").build();
		}catch(XhiveXQueryException xxe){
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return r;
	}

	/**
	 * Update a note by id.
	 * @param user 
	 * @param id
	 * @param data 
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@Path("/{id}")
	@PUT
	@Consumes("application/xml")
	public Response updateNote(@PathParam("user") String user,
			@PathParam("id") int id, String data){
		Response r = null;
		try{
			userAuth.Auth(hh, user);
			ndb.changeNote(user, id, data);
			r = Response.ok().build();
		}catch(SecurityException se){
			r = Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Basic").build();
		}catch(XhiveXQueryException xxe){
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return r;
	}

	/**
	 * Add new note.
	 * @param user
	 * @param id 
	 * @param data
	 * @return an instance of javax.ws.rs.core.Response
	 */
	@POST
	@Consumes("application/xml")
	public Response addNote(@PathParam("user") String user,
			@PathParam("id") int id, String data){
		Response r = null;
		try{
			userAuth.Auth(hh, user);
			String tmp = null;
			tmp = ndb.addNote(user, data);
			r = Response.ok(tmp).build();
		}catch(SecurityException se){
			r = Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Basic").build();
		}catch(XhiveXQueryException xxe){
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return r;
	}
}
