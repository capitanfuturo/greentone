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
	@Persistent
	private Integer numVacazioni;
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

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Operation)
		{
			Operation candidate = (Operation) obj;
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
	 * Restituisce l'importo.
	 * 
	 * @return l'importo
	 */
	public Double getAmount()
	{
		return amount;
	}

	/**
	 * Imposta l'importo
	 * 
	 * @param amount
	 *          l'importo
	 */
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	/**
	 * Restituisce la data di registrazione dell'operazione.
	 * 
	 * @return la data di registrazione dell'operazione
	 */
	public DateTime getOperationDate()
	{
		return operationDate;
	}

	/**
	 * Imposta la data di registrazione dell'operazione.
	 * 
	 * @param operationDate
	 *          la data di registrazione dell'operazione
	 */
	public void setOperationDate(DateTime operationDate)
	{
		this.operationDate = operationDate;
	}

	/**
	 * Restituisce <code>true</code> se l'operazione è di tipo vacazione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se l'operazione è di tipo vacazione,
	 *         <code>false</code> altrimenti
	 */
	public boolean getIsVacazione()
	{
		return isVacazione;
	}

	/**
	 * Imposta un'operazione è di tipo onorario a vacazione.
	 * 
	 * @param isVacazione
	 *          <code>true</code> se l'operazione è di tipo onorario a vacazione,
	 *          <code>false</code> altrimenti
	 */
	public void setIsVacazione(boolean isVacazione)
	{
		this.isVacazione = isVacazione;
	}

	/**
	 * Restituisce <code>true</code> se l'operazione è di tipo onorario a
	 * vacazione e la vacazione è da attribuirsi ad un aiutante,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se l'operazione è di tipo onorario a vacazione e
	 *         la vacazione è da attribuirsi ad un aiutante, <code>false</code>
	 *         altrimenti
	 */
	public boolean getIsProfessionalVacazione()
	{
		return isProfessionalVacazione;
	}

	/**
	 * Imposta un'operazione come di vacazione professionale.
	 * 
	 * @param isProfessionalVacazione
	 *          <code>true</code> se l'operazione è di tipo vacazione
	 *          professionale, <code>false</code> altrimenti
	 */
	public void setIsProfessionalVacazione(boolean isProfessionalVacazione)
	{
		this.isProfessionalVacazione = isProfessionalVacazione;
	}

	/**
	 * Restituisce l'incarico.
	 * 
	 * @return l'incarico
	 */
	public Job getJob()
	{
		return job;
	}

	/**
	 * Restituisce l'incarico.
	 * 
	 * @param job
	 *          l'incarico
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}

	/**
	 * Restituisce il tipo di operazione.
	 * 
	 * @return il tipo di operazione
	 */
	public OperationType getOperationType()
	{
		return operationType;
	}

	/**
	 * Imposta il tipo di operazione
	 * 
	 * @param operationType
	 *          il tipo di operazione
	 */
	public void setOperationType(OperationType operationType)
	{
		this.operationType = operationType;
	}

	/**
	 * Restituisce il numero di vacazioni per l'operazione.
	 * 
	 * @return il numero di vacazioni per l'operazione
	 */
	public Integer getNumVacazioni()
	{
		return numVacazioni;
	}

	/**
	 * Imposta il numero di vacazioni per l'operazione.
	 * 
	 * @param numVacazioni
	 *          il numero di vacazioni per l'operazione
	 */
	public void setNumVacazioni(Integer numVacazioni)
	{
		this.numVacazioni = numVacazioni;
	}
}
