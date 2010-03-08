package Quicker.Server.db;

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

	public String getCode(String user){
		String query = "string(/info/user/code)";
		return executeXQuery(user, query);
	}
}
