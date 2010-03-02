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

	
	public String getNoteById(String id){
		return id;
	}

	public String getNoteList(String user){
		return "<notes/>";
	}
	
}
