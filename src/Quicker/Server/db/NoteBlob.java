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
package Quicker.Server.db;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author Quicker Team
 */
public class NoteBlob implements Serializable{
	public String name;
	public String title;
	public int noteId;
	public String mimeType;

	private ByteArrayInputStream stream;

	public NoteBlob(int noteId, ByteArrayInputStream in){
		this.stream = in;
	}

	public void setMetadata(Map<String,String> metadata){
		this.title = metadata.get("title");
		this.mimeType = metadata.get("mimeType");
	}

	public InputStream getInputStream(){
		return stream;
	}
}
