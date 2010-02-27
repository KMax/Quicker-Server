package Quicker.Server.db;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 */
@Stateless
public class NoteDatabase {
	@EJB
	private Database database;

	public NoteDatabase(){}

	
}
