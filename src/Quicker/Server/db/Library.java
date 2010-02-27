package Quicker.Server.db;

import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.error.xquery.XhiveXQueryException;
import com.xhive.query.interfaces.XhiveQueryResultIf;
import java.util.Iterator;

/**
 *
 */
public class Library {

	XhiveLibraryIf library;

	public Library(XhiveLibraryIf library) {
		this.library = library;
	}

	public XhiveLibraryIf getLibrary(){
		return library;
	}

	public XhiveLibraryIf getChildLibrary(String lib){
		return (XhiveLibraryIf) library.get(lib);
	}

	/**
	 *
	 * @param query
	 * @return XML-document
	 * @throws XhiveXQueryException
	 */
	public Iterator executeXQueryQuery(String query) throws XhiveXQueryException {
		Iterator result = library.executeXQuery(query);
		return result;
	}

	/**
	 *
	 * @param query
	 * @return
	 */
	public XhiveQueryResultIf executeXPathQuery(String query){
		XhiveQueryResultIf result = library.executeXPathQuery(query);
		return result;
	}

	/**
	 *
	 * @param path - Path from root library
	 * @param doc - XML-document
	 */
	public void addDocument(String path,String doc){

	}
}
