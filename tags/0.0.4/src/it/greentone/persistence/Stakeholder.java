package it.greentone.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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
 * Interessati ad un incarico. Questa tabella di relazione serve per poter
 * definire delle liste di persone che possono aver accesso alle informazioni di
 * un certo incarico o che comunque ne sono coinvolte.
 * 
 * @author Giuseppe Caliendo
 */
@PersistenceCapable(table = "GT_STAKEHOLDER", detachable = "true")
public class Stakeholder
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	private Person person;
	@Persistent(defaultFetchGroup = "true", dependent = "false")
	private Job job;

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Stakeholder)
		{
			Stakeholder candidate = (Stakeholder) obj;
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
	 * Restituisce la persona in anagrafica.
	 * 
	 * @return la persona in anagrafica
	 */
	public Person getPerson()
	{
		return person;
	}

	/**
	 * Imposta la persona dell'anagrafica come interessata ad un incarico.
	 * 
	 * @param person
	 *          la persona dell'anagrafica come interessata ad un incarico
	 */
	public void setPerson(Person person)
	{
		this.person = person;
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
	 * Imposta l'incarico di interesse.
	 * 
	 * @param job
	 *          l'incarico
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}


}
