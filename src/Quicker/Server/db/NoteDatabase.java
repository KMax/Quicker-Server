package Quicker.Server.db;

import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 */
@Stateless
public class NoteDatabase {
	@EJB
	private Database db;

	public NoteDatabase(){}

	
	public String getNoteById(String id){
		return id;
	}

	/**
	 * 
	 * @param user - User login
	 * @return java.lang.String contains a xml-document
	 */
	public String getNoteList(String user){
		String query = 
				"<feed xmlns='http://www.w3.org/2005/Atom'> " +
				"{for $i in doc('note')/* " +
				"return " +
				"<entry>" +
				"{" +
				"$i/*[1]," +
				"$i/*[2]" +
				"}" +
				"</entry>" +
				"} " +
				"</feed>";
		XhiveSessionIf session = db.getDriver().createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(true);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase().getRoot().get(user)
				.executeXQuery(query);
		return i.next().toString();
	}
	
}
