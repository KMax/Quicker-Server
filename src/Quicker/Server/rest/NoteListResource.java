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
