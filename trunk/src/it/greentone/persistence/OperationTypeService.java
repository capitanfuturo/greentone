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
 * Classe di supporto per l'accesso e gestione di {@link OperationType}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("operationTypeService")
@Transactional(propagation = Propagation.REQUIRED)
public class OperationTypeService
{
	@Inject
	private OperationTypeDAO operationTypeDAO;
	private final EventList<OperationType> allOperationTypeEventList =
	  new BasicEventList<OperationType>();

	/**
	 * Rende persistente un oggetto nel database.
	 * 
	 * @param operationType
	 *          oggetto da rendere persistente
	 */
	public void storeOperationType(final OperationType operationType)
	{
		operationTypeDAO.storeOperationType(operationType);
	}

	/**
	 * Aggiunge un nuovo oggetto nell'insieme di quelli persistenti.
	 * 
	 * @param operationType
	 *          tipo di operazione
	 */
	public void addOperationType(OperationType operationType)
	{
		storeOperationType(operationType);
		allOperationTypeEventList.add(operationType);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param operationType
	 *          l'oggetto da eliminare
	 */
	public void deleteOperationType(final OperationType operationType)
	{
		operationTypeDAO.deleteOperationType(operationType);
		allOperationTypeEventList.remove(operationType);
	}

	/**
	 * Restituisce la lista di tutti gli oggetti presenti in database.
	 * 
	 * @return la lista di tutti gli oggetti presenti in database
	 * @throws DataAccessException
	 */
	public EventList<OperationType> getAllOperationTypes()
	  throws DataAccessException
	{
		if(allOperationTypeEventList.isEmpty())
			allOperationTypeEventList.addAll(operationTypeDAO.getAllOperationTypes());
		return allOperationTypeEventList;
	}
}
