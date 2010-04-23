package Quicker.Server.rest;

import javax.ws.rs.core.Response;

public class Resource {
	
	protected Resource(){}

	public Response unAuthorized(){
		return Response.status(Response.Status.UNAUTHORIZED).
				header("WWW-Authenticate", "Basic").build();
	}
}
