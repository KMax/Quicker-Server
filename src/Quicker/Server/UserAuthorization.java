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
	
	public boolean Auth(HttpHeaders hh, String user)
			throws WebApplicationException{
		WebApplicationException wae = new WebApplicationException(Response
						.status(Status.UNAUTHORIZED)
						.header(hh.WWW_AUTHENTICATE, "Basic").build());
		String code = hh.getRequestHeader(hh.AUTHORIZATION).get(0).substring(6);
		try{
			if(!(userdb.getCode(user).equals(code))){
				System.out.println("AuthWAE");
				throw wae;
			}
		}catch(NullPointerException npe){
			System.out.println("AuthNull");
			throw wae;
		}catch(XhiveXQueryException xxe){
			System.out.println("AuthXQuery");
			throw wae;
		}
		return true;
	}
 
}
