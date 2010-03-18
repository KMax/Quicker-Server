package Quicker.Server.db;

import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.error.xquery.XhiveXQueryException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;
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
		String query = "doc('note')/feed/entry[id="+id+"]";
		return executeXQuery(user, query).asNode().toXml();
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
				"<feed> " +
				"{for $i in doc('note')/feed/entry " +
				"return " +
				"<entry>" +
				"{" +
				"$i/id," +
				"$i/title" +
				"}" +
				"</entry>" +
				"} " +
				"</feed>";
		return executeXQuery(user, query).asNode().toXml();
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
		String query = "xhive:remove(doc('note')/feed/entry[id="+id+"])";
		executeXQueryUpdate(user, query);
	}

	/**
	 * Create a new note in database
	 * @param user
	 * @param doc
	 * @return created note
	 */
	public String addNote(String user, String doc){
		Integer newId = generateID(user);
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new ByteArrayInputStream(doc.getBytes())));
		} catch (SAXException ex) {
		} catch (IOException ex) {
		}
		Document document = parser.getDocument();
		document.getElementsByTagName("id").item(0)
				.setTextContent(newId.toString());
		
		DOMImplementationLS impl = null;
		try {
			impl = (DOMImplementationLS) DOMImplementationRegistry
					.newInstance().getDOMImplementation("LS");
		} catch (ClassNotFoundException ex) {
		} catch (InstantiationException ex) {
		} catch (IllegalAccessException ex) {
		} catch (ClassCastException ex) {
		}
		LSSerializer serializer = impl.createLSSerializer();
		serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
		serializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
		String output = serializer.writeToString(document);
		System.out.println("Destination:\n"+output);

		String query = "let $new-entry :="+output+
				"let $feed := doc('note')/feed " +
				"return " +
				"xhive:insert-into($feed, $new-entry) ";
		executeXQueryUpdate(user, query);
		System.out.println("Query done.");
		return getNoteById(user, newId);
	}


	public void changeNote(String user, int id, String doc){
		String deleteQuery = "";
		executeXQueryUpdate(user, deleteQuery);
		String addQuery = "";
		executeXQueryUpdate(user, addQuery);
	}

	private int generateID(String user){
		String query = "max(doc('note')/feed/entry/id)+1";
		return executeXQuery(user, query).asInt();
	}
	
}
