package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryChildIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.error.XhiveException;
import com.xhive.query.interfaces.XhiveQueryResultIf;
import java.util.Iterator;
import javax.ejb.Stateless;

/**
 * This class encapsulate the low-level operations on xDB.
 */
@Stateless
public class Database{

	private XhiveDriverIf driver;
	private XhiveSessionIf session;
	private XhiveLibraryIf rootLibrary;

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
		rootLibrary = session.getDatabase().getRoot();
		session.commit();
	}

	private XhiveLibraryChildIf selectLibraryFromRoot(String lib){
		return rootLibrary.get(lib);
	}

	/**
	 * 
	 * @param path - Path from root library
	 * @param doc - XML-document
	 */
	public void addDocument(String path,String doc){
		
	}

	/**
	 * 
	 * @param query
	 * @return XML-document
	 */
	public Iterator executeXQueryQuery(String query){
		Iterator result = rootLibrary.executeXQuery(query);
		return result;
	}

	/**
	 *
	 * @param query
	 * @return
	 */
	public XhiveQueryResultIf executeXPathQuery(String query){
		XhiveQueryResultIf result = rootLibrary.executeXPathQuery(query);
		return result;
	}

}
