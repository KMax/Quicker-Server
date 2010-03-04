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

	public String getNoteList(String user){
		XhiveSessionIf session = db.getDriver().createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(true);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase().getRoot().get(user)
				.executeXQuery("doc('note')/entry");
		return i.next().toString();
	}
	
}
