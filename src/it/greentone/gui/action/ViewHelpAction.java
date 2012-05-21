package it.greentone.gui.action;

import it.greentone.GreenToneAppConfig;
import it.greentone.GreenToneUtilities;

import java.io.File;

import javax.inject.Inject;
import javax.swing.SwingWorker;

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
 * Lancia la visualizzazione del Visualizza la pagina iniziale del programma.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewHelpAction
{
	@Inject
	GreenToneUtilities utilities;

	/**
	 * Lancia la visualizzazione del Visualizza la pagina iniziale del programma.
	 */
	@Action
	public void viewHelp()
	{
		final File manualFile =
		  new File(GreenToneAppConfig.MANUAL_REPOSITORY
		    + GreenToneAppConfig.MANUAL_FILE_NAME);
		new SwingWorker<Void, Void>()
			{

				@Override
				protected Void doInBackground() throws Exception
				{
					utilities.open(manualFile);
					return null;
				}
			}.execute();
	}

}
