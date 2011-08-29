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
 * 
 * @author Giuseppe Caliendo
 */
@Service("stakeholderService")
@Transactional(propagation = Propagation.REQUIRED)
public class StakeholderService
{
	@Inject
	private StakeholderDAO stakeholderDAO;
	private final EventList<Stakeholder> allStakeholdersEventList =
	  new BasicEventList<Stakeholder>();

	public void storeStakeholder(final Stakeholder stakeholder)
	{
		stakeholderDAO.storeStakeholder(stakeholder);
	}

	public void addStakeholder(final Stakeholder stakeholder)
	{
		storeStakeholder(stakeholder);
		allStakeholdersEventList.add(stakeholder);
	}

	public void deleteStakeholder(final Stakeholder stakeholder)
	{
		stakeholderDAO.deleteStakeholder(stakeholder);
		allStakeholdersEventList.remove(stakeholder);
	}

	public EventList<Stakeholder> getAllStakeholders()
	  throws DataAccessException
	{
		if(allStakeholdersEventList.isEmpty())
			allStakeholdersEventList.addAll(stakeholderDAO.getAllStakeholders());
		return allStakeholdersEventList;
	}
}
