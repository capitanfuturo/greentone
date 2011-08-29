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
@Service("jobService")
@Transactional(propagation = Propagation.REQUIRED)
public class JobService
{
	@Inject
	private JobDAO jobDAO;
	private final EventList<Job> allJobs = new BasicEventList<Job>();

	public void storeJob(final Job job)
	{
		jobDAO.storeJob(job);
	}

	public void addJob(Job job)
	{
		storeJob(job);
		allJobs.add(job);
	}

	public void deleteJob(final Job job)
	{
		jobDAO.deleteJob(job);
		allJobs.remove(job);
	}

	public EventList<Job> getAllJobs() throws DataAccessException
	{
		if(allJobs.isEmpty())
			allJobs.addAll(jobDAO.getAllJobs());
		return allJobs;
	}
}
