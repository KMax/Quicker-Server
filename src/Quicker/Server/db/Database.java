package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.error.XhiveException;
import com.xhive.error.xquery.XhiveXQueryException;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import java.util.Stack;
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

	private Stack<XhiveSessionIf> sessionStack;

	public Database (){
		try{
			driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");
		}catch(XhiveException xex){
			xex.printStackTrace();
		}
		if(!driver.isInitialized()){
			driver.init();
		}
		sessionStack = new Stack<XhiveSessionIf>();
	}
	
	/**
	 *
	 * @param user
	 * @param query
	 * @return
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	protected String executeXQuery(String user, String query)
			throws NullPointerException,XhiveXQueryException{
		XhiveSessionIf session = this.getSession();
		session.join();
		session.setReadOnlyMode(true);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session
				.getDatabase().getRoot().get(user).executeXQuery(query);
		String result = i.next().toString();
		session.commit();
		if(session.isJoined()){
			session.leave();
		}
		this.returnSession(session);
		return result;
	}

	/**
	 * By calling this method, you execute XQuery Update query.
	 * @param user java.lang.String
	 * @param query java.lang.String
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	protected void executeXQueryUpdate(String user, String query) 
			throws NullPointerException,XhiveXQueryException{
		XhiveSessionIf session = this.getSession();
		try{
			session.join();
			session.setReadOnlyMode(false);
			session.begin();
			IterableIterator<? extends XhiveXQueryValueIf> i = session
					.getDatabase().getRoot().get(user).executeXQuery(query);
			session.commit();
		}catch(Exception ex){
			ex.printStackTrace();
			session.rollback();
		}finally{
			if(session.isJoined()){
				session.leave();
			}
			this.returnSession(session);
		}
	}

	/**
	 * Get session from sessionStack.
	 * @return com.xhive.core.interfaces.XhiveSessionIf session.
	 */
	private XhiveSessionIf getSession(){
		if(!sessionStack.isEmpty()){
			return sessionStack.pop();
		}else{
			XhiveSessionIf tmpSession = driver.createSession();
			tmpSession.connect(userName, userPass, dbName);
			if(tmpSession.isJoined()){
				tmpSession.leave();
			}
			return tmpSession;
		}
	}

	/**
	 * Put back a session in sessionStack.
	 * @param session com.xhive.core.interfaces.XhiveSessionIf
	 */
	private void returnSession(XhiveSessionIf session){
		sessionStack.push(session);
	}
}
