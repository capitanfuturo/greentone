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
@PersistenceCapable(table = "GT_OPERATION", detachable = "true")
public class Operation
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String description;
	@Persistent
	private Double amount;
	@Persistent(defaultFetchGroup = "true")
	private DateTime operationDate;
	@Persistent
	private boolean isVacazione;
	@Persistent
	private boolean isProfessionalVacazione;
	@Persistent(defaultFetchGroup = "true")
	private Job job;
	@Persistent(defaultFetchGroup = "true")
	private OperationType operationType;

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

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	public DateTime getOperationDate()
	{
		return operationDate;
	}

	public void setOperationDate(DateTime operationDate)
	{
		this.operationDate = operationDate;
	}

	public boolean getIsVacazione()
	{
		return isVacazione;
	}

	public void setIsVacazione(boolean isVacazione)
	{
		this.isVacazione = isVacazione;
	}

	public boolean getIsProfessionalVacazione()
	{
		return isProfessionalVacazione;
	}

	public void setIsProfessionalVacazione(boolean isProfessionalVacazione)
	{
		this.isProfessionalVacazione = isProfessionalVacazione;
	}

	public Job getJob()
	{
		return job;
	}

	public void setJob(Job job)
	{
		this.job = job;
	}

	public OperationType getOperationType()
	{
		return operationType;
	}

	public void setOperationType(OperationType operationType)
	{
		this.operationType = operationType;
	}
}
