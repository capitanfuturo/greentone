package it.greentone.persistence;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * Classe di supporto per l'accesso e gestione di {@link Metadata}.
 * 
 * @author Giuseppe Caliendo
 */
@Service("metadataService")
@Transactional(propagation = Propagation.REQUIRED)
public class MetadataService
{
	/** Property di sistema contenente l'ultimo protocollo dei documenti usato */
	public static final String DOCUMENT_LAST_PROTOCOL = "documentLastProtocol";

	@Inject
	private MetadataDAO metadataDAO;

	/**
	 * Restituisce il metadato con nome passato in ingresso.
	 * 
	 * @param name
	 *          nome del metadato
	 * @return il metadato con nome passato in ingresso
	 */
	public Metadata getMetadataFromName(String name)
	{
		return metadataDAO.getMetadataFromName(name);
	}

	/**
	 * Rende persistente l'oggetto passato come parametro.
	 * 
	 * @param metadata
	 *          l'oggetto da rendere persistente
	 * @throws DataAccessException
	 */
	public void storeMetadata(final Metadata metadata)
	{
		metadataDAO.storeMetadata(metadata);
	}

	/**
	 * Elimina l'oggetto passato in ingresso.
	 * 
	 * @param metadata
	 *          l'oggetto da eliminare
	 * @throws DataAccessException
	 */
	public void deleteMetadata(final JobCategory metadata)
	{
		metadataDAO.deleteMetadata(metadata);
	}
}
