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
package Quicker.Server.db;

import com.xhive.error.xquery.XhiveXQueryException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 */
@Stateless
public class NoteDatabase extends Database{

	public NoteDatabase(){
		super();
	}

	/**
	 * 
	 * @param user
	 * @param id
	 * @return
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	public String getNoteById(String user, int id)
			throws NullPointerException,XhiveXQueryException{
		String query = "doc('note/"+id+"')/note";
		return executeXQuery(user, query);
	}

	/**
	 *
	 * @param user
	 * @param noteId
	 * @param blobName
	 * @return
	 */
	public ByteArrayInputStream getBLOBStream(String user,int noteId, String blobName){
		ByteArrayInputStream tmp = null;
		try {
			tmp = getBLOBStream(user, "note", noteId, blobName);
		} catch (IOException ex){
			ex.printStackTrace();
		}
		return tmp;
	}

	/**
	 *
	 * @param user
	 * @param noteId
	 * @param blobName
	 * @return
	 */
	public String getBLOBInfo(String user,int noteId,String blobName){
		String query = "doc('note/"+noteId+"')" +
						"/note/content/entity[@name = "+blobName+"]";
		return executeXQuery(user, query);
	}

	public String getBLOBType(String user, int noteId, String blobName){
		String query= "string(doc('note/1')" +
				"/note/content/entity[@name = 1]/@type)";
		return executeXQuery(user, query);
	}

	/**
	 * 
	 * @param user - User login
	 * @return java.lang.String contains a xml-document
	 * @throws NullPointerException
	 * @throws XhiveXQueryException 
	 */
	public String getNoteList(String user)
			throws NullPointerException,XhiveXQueryException{
		String query = 
				"<notes> " +
				"{for $i in doc('note')/note " +
				"return " +
				"<note>" +
				"{" +
				"$i/id," +
				"$i/title,"+
				"$i/date,"+
				"<extractions>{substring($i/content/text/text(),0,10)}</extractions>"+
				"}" +
				"</note>" +
				"} " +
				"</notes>";
		return executeXQuery(user, query);
	}

	/**
	 * 
	 * @param user
	 * @param id
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	public void deleteNote(String user, int id)
			throws NullPointerException,XhiveXQueryException{
		deleteNode(user, "note/", String.valueOf(id));
	}

	/**
	 * FIXME
	 * Create a new note in database
	 * @param user
	 * @param doc
	 * @return created note
	 */
	public int addNote(String user, String doc){
		Integer newId = generateID(user);
		createLibrary(user, "note",newId.toString());
		createDocument(user, "note", newId, doc);
		return newId;
	}


	/**
	 * 
	 * @param user
	 * @param id
	 * @param doc
	 */
	public void changeNote(String user, int id, String doc){
		replaceDocument(user, "note", id, "note", doc);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	private int generateID(String user){
		String query = "max(doc('note')/note/id)+1";
		return Integer.parseInt(executeXQuery(user, query));
	}
}
