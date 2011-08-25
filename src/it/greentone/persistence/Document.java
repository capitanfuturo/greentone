package it.greentone.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.joda.time.DateTime;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code>
 * <br>
 * <br>
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_DOCUMENT", detachable = "true")
public class Document
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	String description;
	@Persistent
	String notes;
	@Persistent
	boolean isDigital;
	@Persistent
	String protocol;
	@Persistent
	boolean incoming;
	@Persistent(defaultFetchGroup = "true")
	DateTime releaseDate;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	Person recipient;
	@Persistent
	String uri;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	Job job;

	public Document()
	{
		// TODO Auto-generated constructor stub
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public boolean getIsDigital()
	{
		return isDigital;
	}

	public void setIsDigital(boolean isDigital)
	{
		this.isDigital = isDigital;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public boolean getIsIncoming()
	{
		return incoming;
	}

	public void setIsIncoming(boolean incoming)
	{
		this.incoming = incoming;
	}

	public DateTime getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(DateTime releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public Person getRecipient()
	{
		return recipient;
	}

	public void setRecipient(Person recipient)
	{
		this.recipient = recipient;
	}

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public Job getJob()
	{
		return job;
	}

	public void setJob(Job job)
	{
		this.job = job;
	}


}
