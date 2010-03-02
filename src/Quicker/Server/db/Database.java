package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.error.XhiveException;
import javax.ejb.Stateless;

/**
 * This class encapsulate the low-level operations on xDB.
 */
@Stateless
public class Database{

	private XhiveDriverIf driver;
	private XhiveSessionIf session;
	public Library rootLibrary;

	public Database (){
		try{
			driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");
		}catch(XhiveException xex){
			xex.printStackTrace();
		}
		if(!driver.isInitialized()){
			driver.init();
		}
		session = driver.createSession();
		session.connect("Administrator", "123", "Quicker");
		if(!session.isConnected()){
			//FIXME
			//Exception
		}
		session.begin();
		rootLibrary = new Library(session.getDatabase().getRoot());
		session.commit();
	}

	public Library getRootLibrary(String lib){
		return rootLibrary.getChildLibrary(lib);
	}

}
