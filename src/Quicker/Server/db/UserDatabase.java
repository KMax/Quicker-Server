package Quicker.Server.db;

import com.xhive.error.xquery.XhiveXQueryException;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 */
@Stateless
public class UserDatabase extends Database{

	@EJB
	private Database db;

	public UserDatabase(){}

	public String getCode(String user)
			throws NullPointerException,XhiveXQueryException{
		String query = "string(/info/user/code)";
		return executeXQuery(user, query).asString();
	}
}
