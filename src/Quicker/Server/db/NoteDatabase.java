package Quicker.Server.db;

import Quicker.Server.db.Database;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 */
@Stateless
public class NoteDatabase extends Database{

	public NoteDatabase(){
		super();
	}

	
	public String getNoteById(String user, int id){
		String query = "doc('note')/feed/entry[id="+id+"]";
		return executeXQuery(user, query);
	}

	/**
	 * 
	 * @param user - User login
	 * @return java.lang.String contains a xml-document
	 */
	public String getNoteList(String user){
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
		return executeXQuery(user, query);
	}

	/**
	 * 
	 * @param user
	 * @param id
	 */
	public void deleteNote(String user, int id){
		String query = "xhive:remove(doc('note')/feed/entry[id="+id+"])";
		executeXQueryUpdate(user, query);
	}
	
}
