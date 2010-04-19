/***************************************************************************
*
*	Copyright 2010 Quicker Team
*
*	Quicker Team is:
*		Kirdeev Andrey (kirduk@yandex.ru)
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
package Quicker.Server;

import Quicker.Server.db.UserDatabase;
import com.xhive.error.xquery.XhiveXQueryException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.HttpHeaders;

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
