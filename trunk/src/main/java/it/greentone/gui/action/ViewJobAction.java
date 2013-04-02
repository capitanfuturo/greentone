package it.greentone.gui.action;

import it.greentone.gui.MainPanel;
import it.greentone.gui.panel.JobPanel;
import it.greentone.persistence.Job;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2012 GreenTone Developer Team.<br>
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
 * Mostra un incarico e i suoi dettagli nel pannello principale
 * dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewJobAction extends AbstractBean
{
	@Inject
	private JobPanel jobPanel;
	private Job job;
	boolean viewJobActionEnabled = false;
	@Inject
	private MainPanel mainPanel;

	/**
	 * Imposta l'incarico da visualizzare.
	 * 
	 * @param job
	 *          l'incarico da visualizzare
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}

	/**
	 * Mostra gli incarichi nel pannello principale dell'applicazione.
	 */
	@Action(enabledProperty = "viewJobActionEnabled")
	public void viewJob()
	{
		jobPanel.setJob(job);
		jobPanel.setup();
		ContextualAction.addTab(mainPanel, jobPanel);
	}

	/**
	 * Restituisce <code>true</code> se è possibile abilitare l'azione,
	 * <code>false</code> altrimenti.
	 * 
	 * @return <code>true</code> se è possibile abilitare l'azione,
	 *         <code>false</code> altrimenti
	 */
	public boolean isViewJobActionEnabled()
	{
		return viewJobActionEnabled;
	}

	/**
	 * Imposta l'abilitazione dell'azione.
	 * 
	 * @param viewJobActionEnabled
	 *          <code>true</code> se si vuole abilitare l'azione,
	 *          <code>false</code> altrimenti
	 */
	public void setViewJobActionEnabled(boolean viewJobActionEnabled)
	{
		final boolean oldValue = this.viewJobActionEnabled;
		this.viewJobActionEnabled = viewJobActionEnabled;
		firePropertyChange("viewJobActionEnabled", oldValue, viewJobActionEnabled);
	}
}
