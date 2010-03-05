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

	
	public String getNoteById(String user, int id){
		String query = "doc('note')/feed/entry[id="+id+"]";
		return executeXQuery(user, query);
	}

	private String executeXQuery(String user, String query) {
		IterableIterator<? extends XhiveXQueryValueIf> i = null;
		XhiveSessionIf session = db.getDriver().createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(true);
		try{
		session.begin();
		i = session.getDatabase()
				.getRoot().get(user).executeXQuery(query);
		//session.commit();
		}catch(Exception ex){
			//session.rollback();
			ex.printStackTrace();
		}
		return i.next().toString();
	}

	private void executeXQueryUpdate(String user, String query){
		XhiveSessionIf session = db.getDriver().createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(false);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase()
				.getRoot().get(user).executeXQuery(query);
		session.commit();
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
