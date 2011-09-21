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
 * Documento relativo ad un incarico.
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

	/**
	 * Documento relativo ad un incarico.
	 */
	public Document()
	{
	}

	/**
	 * Restituisce l'identificativo del record.
	 * 
	 * @return l'identificativo del record
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Imposta l'identificativo del record.
	 * 
	 * @param id
	 *          l'identificativo del record
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Restituisce la descrizione del documento.
	 * 
	 * @return la descrizione del documento
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Imposta la descrizione del documento.
	 * 
	 * @param description
	 *          la descrizione del documento
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Restituisce il campo note del documento.
	 * 
	 * @return il campo note del documento
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * Imposta il campo note del documento.
	 * 
	 * @param notes
	 *          il campo note del documento
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/**
	 * Restituisce <code>true</code> se il documento è in formato digitale,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se il documento è in formato digitale,
	 *         <code>false</code> altrimenti
	 */
	public boolean getIsDigital()
	{
		return isDigital;
	}

	/**
	 * Indica se il documento è in formato digitale oppure no.
	 * 
	 * @param isDigital
	 *          <code>true</code> se il documento è in formato digitale,
	 *          <code>false</code> altrimenti
	 */
	public void setIsDigital(boolean isDigital)
	{
		this.isDigital = isDigital;
	}

	/**
	 * Restituisce il protocollo del documento.
	 * 
	 * @return il protocollo del documento
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * Imposta il protocollo del documento.
	 * 
	 * @param protocol
	 *          il protocollo del documento
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	/**
	 * Restituisce <code>true</code> se il documento è entrante nell'archivio
	 * dello studio, <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se il documento è entrante nell'archivio dello
	 *         studio, <code>false</code> altrimenti
	 */
	public boolean getIsIncoming()
	{
		return incoming;
	}

	/**
	 * Indica se il documento è entrante nell'archivio dello studio.
	 * 
	 * @param incoming
	 *          <code>true</code> se il documento è entrante nell'archivio dello
	 *          studio, <code>false</code> altrimenti
	 */
	public void setIsIncoming(boolean incoming)
	{
		this.incoming = incoming;
	}

	/**
	 * Restituisce la data di rilascio del documento.
	 * 
	 * @return la data di rilascio del documento
	 */
	public DateTime getReleaseDate()
	{
		return releaseDate;
	}

	/**
	 * Imposta la data di rilascio del documento.
	 * 
	 * @param releaseDate
	 *          la data di rilascio del documento
	 */
	public void setReleaseDate(DateTime releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	/**
	 * Restituisce il destinatario.
	 * 
	 * @return il destinatario
	 */
	public Person getRecipient()
	{
		return recipient;
	}

	/**
	 * Imposta il destinatario.
	 * 
	 * @param recipient
	 *          il destinatario
	 */
	public void setRecipient(Person recipient)
	{
		this.recipient = recipient;
	}

	/**
	 * Restituisce il pecorso al file.
	 * 
	 * @return il pecorso al file
	 */
	public String getUri()
	{
		return uri;
	}

	/**
	 * Imposta il pecorso al file.
	 * 
	 * @param uri
	 *          il pecorso al file
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}

	/**
	 * Restituisce l'incarico associato.
	 * 
	 * @return l'incarico associato
	 */
	public Job getJob()
	{
		return job;
	}

	/**
	 * Imposta l'incarico associato.
	 * 
	 * @param job
	 *          l'incarico associato
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}


}
