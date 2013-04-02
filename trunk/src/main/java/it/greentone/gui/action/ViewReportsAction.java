package it.greentone.gui.action;

import it.greentone.report.ReportManager;
import it.greentone.report.ReportsCategoryInterface;

import javax.inject.Inject;

import org.jdesktop.application.Action;
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
 * Visualizza a seconda della categoria impostata da
 * {@link #setup(ReportsCategoryInterface)} la dialog di gestione di report e ne
 * gestisce la stampa.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewReportsAction
{
	@Inject
	private ReportManager reportManager;
	private ReportsCategoryInterface category;

	/**
	 * Imposta prima che venga eseguita l'azione la categoria di report da
	 * visualizzare.
	 * 
	 * @param category
	 *          la categoria di report da visualizzare
	 */
	public void setup(ReportsCategoryInterface category)
	{
		this.category = category;
	}

	/**
	 * Visualizza a seconda della categoria impostata da
	 * {@link #setup(ReportsCategoryInterface)} la dialog di gestione di report e
	 * ne gestisce la stampa.
	 */
	@Action
	public void viewReports()
	{
		reportManager.showDialog(category);
	}
}
