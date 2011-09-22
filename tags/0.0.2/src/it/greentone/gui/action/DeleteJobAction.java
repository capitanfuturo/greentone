package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.ContextualPanel.EStatus;
import it.greentone.gui.panel.DocumentsPanel;
import it.greentone.gui.panel.JobsPanel;
import it.greentone.gui.panel.OperationsPanel;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Elimina un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class DeleteJobAction extends AbstractBean
{
	@Inject
	JobsPanel jobsPanel;
	@Inject
	DocumentsPanel documentsPanel;
	@Inject
	OperationsPanel operationsPanel;
	@Inject
	JobService jobService;
	boolean deleteJobActionEnabled = false;
	private final ResourceMap resourceMap;

	/**
	 * Elimina un incarico.
	 */
	public DeleteJobAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Elimina un incarico.
	 */
	@Action(enabledProperty = "deleteJobActionEnabled")
	public void deleteJob()
	{
		int confirmDialog =
		  JOptionPane.showConfirmDialog(jobsPanel,
		    resourceMap.getString("deleteJob.Action.confirmMessage"));
		if(confirmDialog == JOptionPane.OK_OPTION)
		{
			Job job = jobsPanel.getSelectedItem();
			jobService.deleteJob(job);
			jobsPanel.clearForm();
			jobsPanel.setStatus(EStatus.NEW);
		}
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isDeleteJobActionEnabled()
	{
		return deleteJobActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param deleteJobActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setDeleteJobActionEnabled(boolean deleteJobActionEnabled)
	{
		final boolean oldValue = this.deleteJobActionEnabled;
		this.deleteJobActionEnabled = deleteJobActionEnabled;
		firePropertyChange("deleteJobActionEnabled", oldValue,
		  deleteJobActionEnabled);
	}
}
