package it.greentone.persistence;

import java.util.Collection;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("jobCategoryDAO")
public class JobCategoryDAO extends JdoDaoSupport
{
	@Inject
	public JobCategoryDAO(final PersistenceManagerFactory pmf)
	{
		setPersistenceManagerFactory(pmf);
	}

	public JobCategory loadJobCategory(final long id) throws DataAccessException
	{
		final JobCategory jobCategory =
		  getJdoTemplate().getObjectById(JobCategory.class, Long.valueOf(id));
		if(jobCategory == null)
			throw new RuntimeException("Job category " + id + " not found");
		return getPersistenceManager().detachCopy(jobCategory);
	}

	public void storeJobCategory(final JobCategory jobCategory)
	  throws DataAccessException
	{
		getJdoTemplate().makePersistent(jobCategory);
	}

	public void deleteJobCategory(final JobCategory jobCategory)
	  throws DataAccessException
	{
		if(jobCategory == null || jobCategory.getId() == null)
			throw new RuntimeException("Job category is not persistent");
		else
			getPersistenceManager().deletePersistent(jobCategory);
	}

	public Collection<JobCategory> getAllJobCategories()
	  throws DataAccessException
	{
		return getPersistenceManager().detachCopyAll(
		  getJdoTemplate().find(JobCategory.class));
	}
}
