package it.greentone.persistence;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenToneUtilities;

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

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
 * Classe di supporto per l'accesso e gestione di {@link Job}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("jobService")
@Transactional(propagation = Propagation.REQUIRED)
public class JobService
{
	@Inject
	private JobDAO jobDAO;
	@Inject
	private OperationService operationService;
	@Inject
	private DocumentService documentService;
	@Inject
	private ConfigurationProperties configurationProperties;
	private final EventList<Job> allJobs = new BasicEventList<Job>();

	/**
	 * Restitusice l'oggetto di identificativo passato in ingresso.
	 * 
	 * @param id
	 *          identificativo da caricare
	 * @return l'oggetto di identificativo passato in ingresso
	 */
	public Job loadJob(long id)
	{
		return jobDAO.loadJob(id);
	}

	/**
	 * Rende persistente un oggetto nel database.
	 * 
	 * @param job
	 *          oggetto da rendere persistente
	 */
	public void storeJob(final Job job)
	{
		jobDAO.storeJob(job);
	}

	/**
	 * Aggiunge un nuovo oggetto nel database
	 * 
	 * @param job
	 *          l'incarico da aggiungere
	 */
	public void addJob(Job job)
	{
		storeJob(job);
		allJobs.add(job);
	}

	/**
	 * Elimina un oggetto dal database. Quando si elimina un incarico, allora
	 * vengono eliminati anche:
	 * <ul>
	 * <li>tutti i documenti associati</li>
	 * <li>tutte le operationi associate</li>
	 * </ul>
	 * 
	 * @param job
	 *          l'incarico da eliminare
	 * @see Operation
	 * @see Document
	 */
	public void deleteJob(final Job job)
	{
		/* elimino le operazioni */
		Collection<Operation> operationsJob =
		  operationService.getOperationsJob(job);
		for(Operation operation : operationsJob)
		{
			operationService.deleteOperation(operation);
		}
		/* elimino i documenti */
		Collection<Document> documentsJob = documentService.getDocumentsJob(job);
		for(Document document : documentsJob)
		{
			documentService.deleteDocument(document);
		}
		jobDAO.deleteJob(job);
		allJobs.remove(job);
	}

	/**
	 * Restituisce la lista di tutti gli incarichi presenti in database.
	 * 
	 * @return la lista di tutti gli incarichi presenti in database
	 * @throws DataAccessException
	 */
	public EventList<Job> getAllJobs() throws DataAccessException
	{
		if(allJobs.isEmpty())
			allJobs.addAll(jobDAO.getAllJobs());
		return allJobs;
	}

	/**
	 * Restituisce la lista di incarichi per i quali la persona passata in
	 * ingresso sia il committente.
	 * 
	 * @param customer
	 *          committente dell'incarico
	 * @return la lista di incarichi per i quali la persona passata in ingresso
	 *         sia il committente
	 */
	public Collection<Job> getJobsAsCustomer(Person customer)
	{
		return jobDAO.getJobsAsCustomer(customer);
	}

	/**
	 * Restituisce la lista di incarichi per i quali la persona passata in
	 * ingresso sia il responsabile.
	 * 
	 * @param manager
	 *          responsabile dell'incarico
	 * @return la lista di incarichi per i quali la persona passata in ingresso
	 *         sia il responsabile
	 */
	public Collection<Job> getJobsAsManager(Person manager)
	{
		return jobDAO.getJobsAsManager(manager);
	}

	/**
	 * Restituisce l'insieme dei comuni di riferimento presenti nella tabella
	 * degli incarichi.
	 * 
	 * @return l'insieme dei comuni di riferimento presenti nella tabella degli
	 *         incarichi
	 */
	public Collection<String> getAllCities()
	{
		return jobDAO.getAllCities();
	}

	/**
	 * Restituisce il prossimo protocollo utile da utilizzare.
	 * 
	 * @return il prossimo protocollo utile da utilizzare
	 */
	public String getNextProtocol()
	{
		/* calcolo l'ultimo protocollo usato */
		String protocol = "";
		for(Job job : getAllJobs())
		{
			if(protocol.compareToIgnoreCase(job.getProtocol()) < 0)
			{
				protocol = job.getProtocol();
			}
		}
		/*
		 * costruisco il nuovo protocollo, se è usato l'anno allora lo elimino dal
		 * calcolo
		 */
		boolean yearsEnabled = configurationProperties.getUseYearsInJobsProtocol();
		if(!protocol.isEmpty())
		{
			if(yearsEnabled)
			{
				protocol = protocol.substring(5);
			}
			/* tolgo il padding iniziale di 0 */
			int i = 0;
			while(protocol.charAt(i) == ConfigurationProperties.PROTOCOL_PADDING_CHAR)
			{
				i++;
			}
			protocol = protocol.substring(i);
			protocol = (Integer.valueOf(protocol) + 1) + "";
		}
		else
		{
			protocol = "1";
		}
		/* ora procedo con il padding e l'eventuale anno */
		protocol =
		  GreenToneUtilities.leftPadding(protocol,
		    ConfigurationProperties.PROTOCOL_PADDING_CHAR,
		    ConfigurationProperties.JOB_PROTOCOL_NUMERIC_LENGTH);
		if(yearsEnabled)
		{
			int year = Calendar.getInstance().get(Calendar.YEAR);
			protocol =
			  year + ConfigurationProperties.JOB_PROTOCOL_SEPARATOR + protocol;
		}
		return protocol;
	}

	/**
	 * Aggiunge a tutti i protocolli come prefisso l'anno
	 */
	public void addYearToAllJobProtocols()
	{
		if(getAllJobs().size() == 0)
		{
			return;
		}
		/* ordino per protocollo tutti gli incarichi in ordine crescente */
		SortedList<Job> sortedJobs =
		  new SortedList<Job>(getAllJobs(), new Comparator<Job>()
			  {
				  @Override
				  public int compare(Job o1, Job o2)
				  {
					  return o1.getProtocol().compareToIgnoreCase(o2.getProtocol());
				  }
			  });
		/*
		 * Considera il caso [0001,0002] con 0001 iniziato nel 2011 e 0002 nel 2012
		 * che deve diventare [2011-0001,2012-0001]
		 */
		int year = 0;
		int counter = 1;
		for(Job job : sortedJobs)
		{
			if(year != job.getStartDate().getYear())
			{
				year = job.getStartDate().getYear();
				counter = 1;
			}
			else
			{
				counter++;
			}
			String protocol =
			  year
			    + ConfigurationProperties.JOB_PROTOCOL_SEPARATOR
			    + GreenToneUtilities.leftPadding("" + counter,
			      ConfigurationProperties.PROTOCOL_PADDING_CHAR,
			      ConfigurationProperties.JOB_PROTOCOL_NUMERIC_LENGTH);
			job.setProtocol(protocol);
			storeJob(job);
		}
	}

	/**
	 * Rimuove a tutti i protocolli come prefisso l'anno
	 * 
	 * @throws IndexOutOfBoundsException
	 *           eccezione in caso in cui si venga a superare il limite di
	 *           incarichi gestibili
	 */
	public void removeYearToAllJobProtocols() throws IndexOutOfBoundsException
	{
		if(getAllJobs().size() == 0)
		{
			return;
		}
		/* calcolo il limite numerico di protocolli gestiti */
		int limit =
		  (int) Math.pow(10d, new Integer(
		    ConfigurationProperties.JOB_PROTOCOL_NUMERIC_LENGTH).doubleValue());
		if(getAllJobs().size() > limit)
		{
			throw new IndexOutOfBoundsException("Too many job protocols to migrate!");
		}
		/* ordino per protocollo tutti gli incarichi in ordine crescente */
		SortedList<Job> sortedJobs =
		  new SortedList<Job>(getAllJobs(), new Comparator<Job>()
			  {
				  @Override
				  public int compare(Job o1, Job o2)
				  {
					  return o1.getProtocol().compareToIgnoreCase(o2.getProtocol());
				  }
			  });
		/*
		 * rinumero tutti gli incarichi in questo modo il caso [2011-0001,
		 * 2012-0001] in [0001,0002]
		 */
		for(int i = 0; i < sortedJobs.size(); i++)
		{
			Job job = sortedJobs.get(i);
			int protocolNumber = i + 1;
			/* eseguo il padding */
			String protocol = protocolNumber + "";
			protocol =
			  GreenToneUtilities.leftPadding(protocol,
			    ConfigurationProperties.PROTOCOL_PADDING_CHAR,
			    ConfigurationProperties.JOB_PROTOCOL_NUMERIC_LENGTH);
			job.setProtocol("" + protocol);
			storeJob(job);
		}
	}
}
