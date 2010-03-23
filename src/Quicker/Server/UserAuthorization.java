package Quicker.Server;

import Quicker.Server.db.UserDatabase;
import com.xhive.error.xquery.XhiveXQueryException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 */
@Stateless
public class UserAuthorization {

	@EJB
	private UserDatabase userdb;
	
	public UserAuthorization(){}
	
	public void Auth(HttpHeaders hh, String user)
			throws SecurityException,XhiveXQueryException{
		String code = hh.getRequestHeader(hh.AUTHORIZATION).get(0).substring(6);
		try{
		if(!(userdb.getCode(user).equals(code))){
			throw new SecurityException();
		}
		}catch(NullPointerException npe){
			throw new SecurityException();
		}
	}
 
}
