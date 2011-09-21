package it.greentone.persistence;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

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
 * Classe di supporto per l'accesso e gestione di {@link Operation}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("operationService")
@Transactional(propagation = Propagation.REQUIRED)
public class OperationService
{
	@Inject
	private OperationDAO operationDAO;
	private final EventList<Operation> allOperationEventList =
	  new BasicEventList<Operation>();

	/**
	 * Aggiunge una nuova operazione.
	 * 
	 * @param operation
	 *          operazione da aggiungere
	 */
	public void addOperation(Operation operation)
	{
		storeOperation(operation);
		allOperationEventList.add(operation);
	}

	/**
	 * Rende persistente un oggetto nel database.
	 * 
	 * @param operation
	 *          oggetto da rendere persistente
	 */
	public void storeOperation(final Operation operation)
	{
		operationDAO.storeOperation(operation);
	}

	/**
	 * Elimina l'operazione.
	 * 
	 * @param operation
	 *          operazione da eliminare
	 */
	public void deleteOperation(final Operation operation)
	{
		operationDAO.deleteOperation(operation);
		allOperationEventList.remove(operation);
	}

	/**
	 * Restituisce la lista di tutte le operazioni.
	 * 
	 * @return la lista di tutte le operazioni
	 * @throws DataAccessException
	 */
	public EventList<Operation> getAllOperations() throws DataAccessException
	{
		if(allOperationEventList.isEmpty())
			allOperationEventList.addAll(operationDAO.getAllOperations());
		return allOperationEventList;
	}
}
