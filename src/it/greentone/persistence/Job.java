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

	public Job()
	{
		// vuoto
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public DateTime getDueDate()
	{
		return dueDate;
	}

	public void setDueDate(DateTime dueDate)
	{
		this.dueDate = dueDate;
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public void setStartDate(DateTime startDate)
	{
		this.startDate = startDate;
	}

	public DateTime getFinishDate()
	{
		return finishDate;
	}

	public void setFinishDate(DateTime finishDate)
	{
		this.finishDate = finishDate;
	}

	public JobCategory getCategory()
	{
		return category;
	}

	public void setCategory(JobCategory category)
	{
		this.category = category;
	}

	public JobStatus getStatus()
	{
		return status;
	}

	public void setStatus(JobStatus status)
	{
		this.status = status;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Person getManager()
	{
		return manager;
	}

	public void setManager(Person manager)
	{
		this.manager = manager;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public Person getCustomer()
	{
		return customer;
	}

	public void setCustomer(Person customer)
	{
		this.customer = customer;
	}

	@Override
	public String toString()
	{
		return getProtocol() + " " + getDescription();
	}
}
