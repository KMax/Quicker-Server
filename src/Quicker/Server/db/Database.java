package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.error.XhiveException;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import javax.ejb.Stateless;

/**
 * This class encapsulate the low-level operations on xDB.
 */
@Stateless
public class Database{

	private XhiveDriverIf driver;
	
	/**
	 * User login for connection to database
	 */
	private static String userName = "Administrator";

	/**
	 * User password for connection to database
	 */
	private static String userPass = "123";
	
	/**
	 * Name of database
	 */
	private static String dbName = "Quicker";

	public Database (){
		try{
			driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");
		}catch(XhiveException xex){
			xex.printStackTrace();
		}
		if(!driver.isInitialized()){
			driver.init();
		}
	}

	protected String executeXQuery(String user, String query) {
		IterableIterator<? extends XhiveXQueryValueIf> i = null;
		XhiveSessionIf session = driver.createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(true);
		try {
			session.begin();
			i = session.getDatabase().getRoot().get(user).executeXQuery(query);
			//session.commit();
		} catch (Exception ex) {
			//session.rollback();
			ex.printStackTrace();
		}
		return i.next().toString();
	}

	protected void executeXQueryUpdate(String user, String query) {
		XhiveSessionIf session = driver.createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(false);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase().getRoot().get(user).executeXQuery(query);
		session.commit();
	}
}
