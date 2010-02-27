package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveLibraryChildIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.error.XhiveException;
import javax.ejb.Stateless;

/**
 * 
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
	 * @param path - Путь от корневой библиотеки
	 * @param doc - XML-документ
	 */
	public void addDocument(String path,String doc){
		
	}

	public String executeQuery(String query){
		return query;
	}

}
