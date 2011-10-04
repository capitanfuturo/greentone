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
 * L'incarico è una prestazione che il geometra contrae con il committente.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_JOB", detachable = "true")
public class Job
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	String protocol;
	@Persistent(defaultFetchGroup = "true")
	DateTime dueDate;
	@Persistent(defaultFetchGroup = "true")
	DateTime startDate;
	@Persistent(defaultFetchGroup = "true")
	DateTime finishDate;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	JobCategory category;
	@Persistent
	JobStatus status;
	@Persistent
	String description;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	Person manager;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	Person customer;
	@Persistent
	String notes;
	@Persistent
	String city;

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Job)
		{
			Job candidate = (Job) obj;
			return id.equals(candidate.getId());
		}
		else
			return super.equals(obj);
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
	 * Restituisce il protocollo univoco dell'incarico.
	 * 
	 * @return il protocollo univoco dell'incarico
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * Imposta il protocollo univoco dell'incarico
	 * 
	 * @param protocol
	 *          il protocollo univoco dell'incarico
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	/**
	 * Restituisce la scadenza dell'incarico.
	 * 
	 * @return la scadenza dell'incarico
	 */
	public DateTime getDueDate()
	{
		return dueDate;
	}

	/**
	 * Imposta la scadenza dell'incarico.
	 * 
	 * @param dueDate
	 *          la scadenza dell'incarico
	 */
	public void setDueDate(DateTime dueDate)
	{
		this.dueDate = dueDate;
	}

	/**
	 * Restituisce la data di inizio.
	 * 
	 * @return la data di inizio
	 */
	public DateTime getStartDate()
	{
		return startDate;
	}

	/**
	 * Imposta la data di inizio.
	 * 
	 * @param startDate
	 *          la data di inizio
	 */
	public void setStartDate(DateTime startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * Restituisce la data di fine.
	 * 
	 * @return la data di fine
	 */
	public DateTime getFinishDate()
	{
		return finishDate;
	}

	/**
	 * Imposta la data di fine.
	 * 
	 * @param finishDate
	 *          la data di fine
	 */
	public void setFinishDate(DateTime finishDate)
	{
		this.finishDate = finishDate;
	}

	/**
	 * Restituisce la categoria dell'incarico.
	 * 
	 * @return la categoria dell'incarico
	 */
	public JobCategory getCategory()
	{
		return category;
	}

	/**
	 * Imposta la categoria dell'incarico
	 * 
	 * @param category
	 *          la categoria dell'incarico
	 */
	public void setCategory(JobCategory category)
	{
		this.category = category;
	}

	/**
	 * Restituisce lo stato dell'incarico.
	 * 
	 * @return lo stato dell'incarico
	 */
	public JobStatus getStatus()
	{
		return status;
	}

	/**
	 * Imposta lo stato dell'incarico.
	 * 
	 * @param status
	 *          lo stato dell'incarico
	 */
	public void setStatus(JobStatus status)
	{
		this.status = status;
	}

	/**
	 * Restituisce la descrizione.
	 * 
	 * @return la descrizione
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Imposta la descrizione.
	 * 
	 * @param description
	 *          la descrizione
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Restituisce il responsabile dell'incarico.
	 * 
	 * @return il responsabile dell'incarico
	 */
	public Person getManager()
	{
		return manager;
	}

	/**
	 * Imposta il responsabile dell'incarico.
	 * 
	 * @param manager
	 *          il responsabile dell'incarico
	 */
	public void setManager(Person manager)
	{
		this.manager = manager;
	}

	/**
	 * Restituisce il campo note.
	 * 
	 * @return il campo note
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * Imposta il campo note.
	 * 
	 * @param notes
	 *          il campo note
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/**
	 * Restituisce il committente.
	 * 
	 * @return il committente
	 */
	public Person getCustomer()
	{
		return customer;
	}

	/**
	 * Imposta il committente.
	 * 
	 * @param customer
	 *          il committente
	 */
	public void setCustomer(Person customer)
	{
		this.customer = customer;
	}

	/**
	 * Restituisce il comune di competenza dell'incarico.
	 * 
	 * @return il comune di competenza dell'incarico
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * Imposta il comune di competenza dell'incarico.
	 * 
	 * @param city
	 *          il comune di competenza dell'incarico
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	@Override
	public String toString()
	{
		String protocol = getProtocol();
		String description = getDescription() != null? getDescription(): "";
		return protocol + " " + description;
	}
}
