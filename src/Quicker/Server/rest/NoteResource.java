/***************************************************************************
*
*	Copyright 2010 Quicker Team
*
*	Quicker Team is:
*	Kirdeev Andrey (kirduk@yandex.ru)
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web-Service
 */

@Path("/{user}/note/")
@Stateless
public class NoteResource extends Resource{
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
	 * Get a note by id
	 * @param user - User login
	 * @param id - Id of note
	 * @return an instance of java.lang.String
	 */
	@Path("/{id}")
	@GET
    @Produces("application/xml")
	public Response getNote(@PathParam("user") String user, @PathParam("id") int id) {
		Response r =null;
		if(userAuth.Auth(hh, user)){
			r = Response.ok(ndb.getNoteById(user, id)).build();
		}else{
			r = unAuthorized();
		}
		return r;
	}


	/**
	 * Get a media content by note id and media name
	 * @param user  user name
	 * @param id  note id
	 * @param blobName  name of media
	 * @return
	 */
	@Path("/{id}/media/{blobName}")
	@GET
	//@Produces("image/jpeg")
	public Response getBLOB(@PathParam("user") String user, 
			@PathParam("id") int id, @PathParam("blobName") String blobName){
		Response r = null;
		if(userAuth.Auth(hh, user)){
			Multipart mp = new MimeMultipart();
			BodyPart bp = new MimeBodyPart();
			try {
				bp.setContent(ndb.getBLOBInfo(user, id, blobName),
						"application/xml");
				mp.addBodyPart(bp);
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}

			ByteArrayInputStream tmp = null;
			try{
				tmp = ndb.getBLOBStream(user, id, blobName);
				BodyPart stream = new MimeBodyPart();
				stream.setContent(tmp,ndb.getBLOBType(user, id, blobName));
				r = Response.ok(mp).build();
			}catch(MessagingException me){
				me.printStackTrace();
			}finally{
				try{
					tmp.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		}else{
			r = unAuthorized();
		}
		return r;
	}

	/**
	 * Delete a note by id.
	 * @param user 
	 * @param id
	 * @return 
	 */
	@Path("/{id}")
	@DELETE
	public Response deleteNote(@PathParam("user") String user, @PathParam("id") int id){
		Response r =null;
		if(userAuth.Auth(hh, user)){
			ndb.deleteNote(user, id);
			r = Response.ok().build();
		}else{
			r = unAuthorized();
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
	@Consumes("*/*")
	public Response updateNote(@PathParam("user") String user,
			@PathParam("id") int id, String data){
		Response r = null;
		if(userAuth.Auth(hh, user)){
			ndb.changeNote(user, id, data);
			r = Response.ok().build();
		}else{
			r = unAuthorized();
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
	@Consumes("*/*")
	public Response addNote(@PathParam("user") String user,
			@PathParam("id") int id, String data){
		Response r = null;
		if(userAuth.Auth(hh, user)){
			try {
				r = Response.ok().contentLocation(new URI(
						"/" + user + "/note/" + ndb.addNote(user, data))).build();
			} catch (URISyntaxException ex) {
				ex.printStackTrace();
			}
		}else{
			r = unAuthorized();
		}
		return r;
	}
}
