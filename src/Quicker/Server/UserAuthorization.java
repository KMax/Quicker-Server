package Quicker.Server;

import Quicker.Server.db.UserDatabase;
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
	
	public boolean Auth(HttpHeaders hh, String user){
		String code = hh.getRequestHeader(hh.AUTHORIZATION).get(0).substring(6);
		if(!(userdb.getCode(user).equals(code))){
			throw new WebApplicationException(Response
					.status(Status.UNAUTHORIZED)
					.header(hh.WWW_AUTHENTICATE, "Basic").build());
		}
		return true;
	}
 
}
