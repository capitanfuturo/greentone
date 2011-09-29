package it.greentone.gui.action;

import it.greentone.ConfigurationProperties;
import it.greentone.gui.panel.OptionsPanel;

import java.text.ParseException;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
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
 * Azione di salvataggio delle opzioni.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveOptionsAction extends AbstractBean
{
	@Inject
	private OptionsPanel optionsPanel;
	@Inject
	private ConfigurationProperties properties;

	/**
	 * Azione di salvataggio delle opzioni.
	 */
	@Action
	public void saveOptions()
	{
		try
		{
			/* recupero i dati dal pannello e li salvo */
			properties.setCheckUpdateActivated(optionsPanel.getCheckUpdateCheckBox()
			  .isSelected());
			optionsPanel.getVacazioneTextField().commitEdit();
			Double value = new Double(optionsPanel.getVacazioneTextField().getText());
			properties.setVacazionePrice(value);

			/* salvo */
			properties.store();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
	}
}
