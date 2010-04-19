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
package Quicker.Server.db;

import com.xhive.error.xquery.XhiveXQueryException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

	
	public String getNoteById(String user, int id)
			throws NullPointerException,XhiveXQueryException{
		String query = "doc('note/"+id+"')/note";
		return executeXQuery(user, query);
	}

	public List<NoteBlob> getBLOBsByNoteId(String user, int id){
		return getAllBLOB(user,"note",id);
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
	 * Create a new note in database
	 * @param user
	 * @param doc
	 * @return created note
	 */
	public String addNote(String user, String doc){

		Integer newId = generateID(user);
		createNode(user, "note",newId.toString());
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new ByteArrayInputStream(doc.getBytes())));
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Document document = parser.getDocument();
		document.getElementsByTagName("id").item(0)
				.setTextContent(newId.toString());
		
		DOMImplementationLS impl = null;
		try {
			impl = (DOMImplementationLS) DOMImplementationRegistry
					.newInstance().getDOMImplementation("LS");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
		LSSerializer serializer = impl.createLSSerializer();
		serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
		serializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
		String output = serializer.writeToString(document);


		String query = "let $new-entry :="+output+
				"let $feed := doc('note')/feed " +
				"return " +
				"xhive:insert-into($feed, $new-entry) ";
		executeXQueryUpdate(user, query);
		return getNoteById(user, newId);
	}


	public void changeNote(String user, int id, String doc){
		deleteNote(user, id);
		String query = "let $new-entry :="+doc+
				"let $feed := doc('note')/feed " +
				"return " +
				"xhive:insert-into($feed, $new-entry) ";
		executeXQueryUpdate(user, query);
	}

	private int generateID(String user){
		String query = "max(doc('note')/feed/entry/id)+1";
		return Integer.parseInt(executeXQuery(user, query));
	}
	
}
