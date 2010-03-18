package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.error.XhiveException;
import com.xhive.error.xquery.XhiveXQueryException;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import javax.ejb.Stateless;

/**
 * This class encapsulate the low-level operations on xDB.
 */
@Stateless
public class Database{

	protected XhiveDriverIf driver;
	
	/**
	 * User login for connection to database
	 */
	protected static String userName = "Administrator";

	/**
	 * User password for connection to database
	 */
	protected static String userPass = "123";
	
	/**
	 * Name of database
	 */
	protected static String dbName = "Quicker";

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
	
	/**
	 *
	 * @param user
	 * @param query
	 * @return
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	protected XhiveXQueryValueIf executeXQuery(String user, String query)
			throws NullPointerException,XhiveXQueryException{
		IterableIterator<? extends XhiveXQueryValueIf> i = null;
		XhiveSessionIf session = driver.createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(true);
		session.begin();
		i = session.getDatabase().getRoot().get(user).executeXQuery(query);
		return i.next();
	}

	/**
	 *
	 * @param user
	 * @param query
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	protected void executeXQueryUpdate(String user, String query) 
			throws NullPointerException,XhiveXQueryException{
		XhiveSessionIf session = driver.createSession();
		session.connect(Database.userName, Database.userPass, Database.dbName);
		session.setReadOnlyMode(false);
		try{
			session.begin();
			IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase()
					.getRoot().get(user).executeXQuery(query);
			session.commit();
		}catch(Exception ex){
			session.rollback();
		}
	}
}
