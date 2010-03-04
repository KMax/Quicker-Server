package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.error.XhiveException;
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
	public static String userName = "Administrator";

	/**
	 * User password for connection to database
	 */
	public static String userPass = "123";
	
	/**
	 * Name of database
	 */
	public static String dbName = "Quicker";

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

	public XhiveDriverIf getDriver(){
		return driver;
	}
}
