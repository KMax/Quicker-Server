package Quicker.Server.db;

import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.error.xquery.XhiveXQueryException;
import com.xhive.query.interfaces.XhiveQueryResultIf;
import java.util.Iterator;
import javax.ejb.Stateful;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 *
 */
@Stateful
public class Library {

	XhiveLibraryIf library;

	public Library(XhiveLibraryIf library) {
		this.library = library;
	}

	public Library getChildLibrary(String lib){
		return new Library((XhiveLibraryIf) library.get(lib));
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
	 * @param doc - XML-document
	 */
	public void addDocument(String doc){
		LSParser builder = library.createLSParser();
		LSInput i = library.createLSInput();
		i.setStringData(doc);
		//FIXME
		//Обработать DOMException, LSException
		Document d = builder.parse(i);
	}
}
