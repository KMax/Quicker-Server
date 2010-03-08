package Quicker.Server.db;

import com.xhive.error.xquery.XhiveXQueryException;
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
		String query = "xhive:remove(doc('note')/feed/entry[id="+id+"])";
		executeXQueryUpdate(user, query);
	}
	
}
