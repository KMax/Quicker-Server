/***************************************************************************
*
*	Copyright 2010 Quicker Team
*
*	Quicker Team is:
*	Kirdeev Andrey (kirduk@yandex.ru)
* 	Koritniy Ilya (korizzz230@bk.ru)
* 	Kolchin Maxim	(kolchinmax@gmail.com)
*/
/****************************************************************************
*
*	This file is part of Quicker.
*
*	Quicker is free software: you can redistribute it and/or modify
*	it under the terms of the GNU Lesser General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	Quicker is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*	GNU Lesser General Public License for more details.
*
*	You should have received a copy of the GNU Lesser General Public License
*	along with Quicker. If not, see <http://www.gnu.org/licenses/>


****************************************************************************/
package Quicker.Server.db;

import com.xhive.XhiveDriverFactory;
import com.xhive.core.interfaces.XhiveDriverIf;
import com.xhive.core.interfaces.XhiveSessionIf;
import com.xhive.dom.interfaces.XhiveBlobNodeIf;
import com.xhive.dom.interfaces.XhiveDocumentIf;
import com.xhive.dom.interfaces.XhiveLSParserIf;
import com.xhive.dom.interfaces.XhiveLibraryIf;
import com.xhive.dom.interfaces.XhiveNodeIf;
import com.xhive.error.XhiveException;
import com.xhive.error.xquery.XhiveXQueryException;
import com.xhive.query.interfaces.XhiveXQueryValueIf;
import com.xhive.util.interfaces.IterableIterator;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.ejb.Stateless;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class encapsulate the low-level operations on xDB.
 */
@Stateless
public class Database {

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
	private static Stack<XhiveSessionIf> sessionStack;

	public Database() {
		try {
			driver = XhiveDriverFactory.getDriver("xhive://localhost:1235");
		} catch (XhiveException xex) {
			xex.printStackTrace();
		}
		if (!driver.isInitialized()) {
			driver.init();
		}
		sessionStack = new Stack<XhiveSessionIf>();
	}

	/**
	 * Execute XQuery query
	 * @param user
	 * @param query
	 * @return
	 * @throws NullPointerException
	 * @throws XhiveXQueryException
	 */
	protected String executeXQuery(String user, String query)
			throws NullPointerException, XhiveXQueryException {
		XhiveSessionIf session = this.getSession();
		session.join();
		session.setReadOnlyMode(true);
		session.begin();
		IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase().getRoot().get(user).executeXQuery(query);
		String result = i.next().toString();
		session.commit();
		if (session.isJoined()) {
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
			throws NullPointerException, XhiveXQueryException {
		XhiveSessionIf session = this.getSession();
		try {
			session.join();
			session.setReadOnlyMode(false);
			session.begin();
			IterableIterator<? extends XhiveXQueryValueIf> i = session.getDatabase().getRoot().get(user).executeXQuery(query);
			session.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.rollback();
		} finally {
			if (session.isJoined()) {
				session.leave();
			}
			this.returnSession(session);
		}
	}

	/**
	 * 
	 * @param user
	 * @param path
	 * @param id
	 * @param blobName 
	 * @return
	 * @throws IOException
	 */
	protected ByteArrayInputStream getBLOBStream(String user, String path,
			int id, String blobName)
			throws IOException{
		XhiveSessionIf session = getSession();
		session.join();
		session.setReadOnlyMode(true);
		session.begin();

		XhiveBlobNodeIf blob = (XhiveBlobNodeIf) session.getDatabase().getRoot().
				getByPath(user+"/"+path+"/"+id+"/"+blobName);
		BufferedInputStream in = new BufferedInputStream(blob.getContents());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int b;
		while((b = in.read())!=-1){
			out.write(b);
		}
		ByteArrayInputStream blobIn = new ByteArrayInputStream(out.toByteArray());
		out.close();
		in.close();
		session.leave();
		returnSession(session);
		return blobIn;
	}


	/**
	 *
	 * @param user
	 * @param path
	 * @param name
	 */
	protected void deleteNode(String user, String path, String name){
		XhiveSessionIf session = getSession();
		session.join();
		session.setReadOnlyMode(false);
		session.begin();

		XhiveNodeIf oldNode = session.getDatabase().getRoot().getByPath(user+"/"+path+"/"+name);
		session.getDatabase().getRoot().getByPath(user+"/"+path).removeChild(oldNode);

		session.commit();
		session.leave();
		returnSession(session);
	}

	/**
	 * 
	 * @param user
	 * @param path
	 * @param name
	 */
	protected void createLibrary(String user, String path ,String name){
		XhiveSessionIf session = getSession();
		session.join();
		session.setReadOnlyMode(false);
		session.begin();
		
		XhiveLibraryIf lif = (XhiveLibraryIf) session.getDatabase().getRoot().
				getByPath(user+"/"+path);
		XhiveLibraryIf nlib = lif.createLibrary();
		nlib.setName(name);
		lif.appendChild(nlib);

		session.commit();
		session.leave();
		returnSession(session);
	}

	/**
	 * 
	 * @param user
	 * @param path
	 * @param id
	 * @param content
	 */
	protected void createDocument(String user, String path, int id,String content){
		XhiveSessionIf session = getSession();
		session.join();
		session.setReadOnlyMode(false);
		session.begin();

		XhiveLibraryIf noteLib = (XhiveLibraryIf) session.getDatabase().
				getRoot().getByPath(user+"/"+path+"/"+id);
		XhiveLSParserIf parser = noteLib.createLSParser();
		LSInput lsinput = noteLib.createLSInput();
		lsinput.setStringData(content);
		XhiveDocumentIf doc = parser.parse(lsinput);
		doc.setName(doc.getDocumentElement().getNodeName());
		noteLib.appendChild(doc);
		
		session.commit();
		session.leave();
		returnSession(session);
	}

	protected void replaceDocument(String user, String path, int id,
			String name, String content){
		XhiveSessionIf session = getSession();
		session.join();
		session.setReadOnlyMode(false);
		session.begin();

		XhiveLibraryIf noteLib = (XhiveLibraryIf) session.getDatabase().
				getRoot().getByPath(user+"/"+path+"/"+id);
		XhiveLSParserIf parser = noteLib.createLSParser();
		LSInput lsinput = noteLib.createLSInput();
		lsinput.setStringData(content);
		XhiveDocumentIf doc = parser.parse(lsinput);
		doc.setName(name);
		noteLib.replaceChild(doc, noteLib.get(name));

		session.commit();
		session.leave();
		returnSession(session);
	}

	/**
	 * Get session from sessionStack.
	 * @return com.xhive.core.interfaces.XhiveSessionIf session.
	 */
	private XhiveSessionIf getSession() {
		if (!sessionStack.isEmpty()) {
			return sessionStack.pop();
		} else {
			XhiveSessionIf tmpSession = driver.createSession();
			tmpSession.connect(userName, userPass, dbName);
			if (tmpSession.isJoined()) {
				tmpSession.leave();
			}
			return tmpSession;
		}
	}

	/**
	 * Put back a session in sessionStack.
	 * @param session com.xhive.core.interfaces.XhiveSessionIf
	 */
	private void returnSession(XhiveSessionIf session) {
		sessionStack.push(session);
	}

	public static void main(String args[]){
		Database d = new Database();

	}
}
