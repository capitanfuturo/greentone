package it.greentone.gui.action;

import it.greentone.GreenTone;

import javax.inject.Inject;
import javax.swing.Action;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.springframework.stereotype.Component;

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
@Component
public class ActionProvider
{
	private final ApplicationContext applicationContext;
	@Inject
	AddDocumentAction addDocument;
	@Inject
	AddJobAction addJob;
	@Inject
	AddPersonAction addPerson;
	@Inject
	DeleteDocumentAction deleteDocument;
	@Inject
	DeleteJobAction deleteJob;
	@Inject
	DeletePersonAction deletePerson;
	@Inject
	EditJobCategoryAction editJobCategory;
	@Inject
	EditJobStakeholderAction editJobStakeholder;
	@Inject
	EditUserAction editUser;
	@Inject
	ExitAction exitAction;
	@Inject
	SaveDocumentAction saveDocument;
	@Inject
	SaveJobAction saveJob;
	@Inject
	SavePersonAction savePerson;
	@Inject
	ViewDocumentsAction viewDocuments;
	@Inject
	ViewJobsAction viewJobs;
	@Inject
	ViewOperationsAction viewOperations;
	@Inject
	ViewPersonsAction viewPersons;
	@Inject
	ViewAboutAction viewAbout;

	public ActionProvider()
	{
		applicationContext = Application.getInstance(GreenTone.class).getContext();
	}

	public Action getAddPerson()
	{
		return applicationContext.getActionMap(addPerson).get("addPerson");
	}

	public Action getDeletePerson()
	{
		return applicationContext.getActionMap(deletePerson).get("deletePerson");
	}

	public Action getEditJobCategory()
	{
		return applicationContext.getActionMap(editJobCategory).get(
		  "editJobCategory");
	}

	public Action getEditJobStakeholder()
	{
		return applicationContext.getActionMap(editJobStakeholder).get(
		  "editJobStakeholder");
	}

	public Action getEditUser()
	{
		return applicationContext.getActionMap(editUser).get("editUser");
	}

	public Action getExit()
	{
		return applicationContext.getActionMap(exitAction).get("exit");
	}

	public Action getSavePerson()
	{
		return applicationContext.getActionMap(savePerson).get("savePerson");
	}

	public Action getViewAbout()
	{
		return applicationContext.getActionMap(viewAbout).get("viewAbout");
	}

	public Action getViewJobs()
	{
		return applicationContext.getActionMap(viewJobs).get("viewJobs");
	}

	public Action getViewPersons()
	{
		return applicationContext.getActionMap(viewPersons).get("viewPersons");
	}

	public Action getAddJob()
	{
		return applicationContext.getActionMap(addJob).get("addJob");
	}

	public Action getDeleteJob()
	{
		return applicationContext.getActionMap(deleteJob).get("deleteJob");
	}

	public Action getSaveJob()
	{
		return applicationContext.getActionMap(saveJob).get("saveJob");
	}

	public Action getViewDocuments()
	{
		return applicationContext.getActionMap(viewDocuments).get("viewDocuments");
	}

	public Action getAddDocument()
	{
		return applicationContext.getActionMap(addDocument).get("addDocument");
	}

	public Action getDeleteDocument()
	{
		return applicationContext.getActionMap(deleteDocument)
		  .get("deleteDocument");
	}

	public Action getSaveDocument()
	{
		return applicationContext.getActionMap(saveDocument).get("saveDocument");
	}

	public Action getViewOperations()
	{
		return applicationContext.getActionMap(viewOperations)
		  .get("viewOperations");
	}
}
