package it.greentone.gui.action;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.panel.OptionsPanel;

import java.text.ParseException;

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
	private final ResourceMap resourceMap;

	/**
	 * Azione di salvataggio delle opzioni.
	 */
	public SaveOptionsAction()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}


	/**
	 * Azione di salvataggio delle opzioni.
	 */
	@Action
	public void saveOptions()
	{
		String name =
		  GreenToneUtilities.getText(optionsPanel.getVacazioneTextField());
		/* la ragione sociale è obbligatoria */
		if(name == null)
		{
			JOptionPane.showMessageDialog(optionsPanel,
			  resourceMap.getString("saveOptions.Action.priceNotNull"),
			  resourceMap.getString("ErrorDialog.title"), JOptionPane.ERROR_MESSAGE);
			optionsPanel.getVacazioneTextField().requestFocus();
		}
		else
		{
			try
			{
				/* recupero i dati dal pannello e li salvo */
				properties.setCheckUpdateActivated(optionsPanel
				  .getCheckUpdateCheckBox().isSelected());

				optionsPanel.getVacazioneTextField().commitEdit();
				Object value = optionsPanel.getVacazioneTextField().getValue();
				properties.setVacazionePrice(GreenToneUtilities
				  .roundTwoDecimals(new Double(value.toString())));

				value = optionsPanel.getVacazioneAiutanteTextField().getValue();
				properties.setVacazioneHelperPrice(GreenToneUtilities
				  .roundTwoDecimals(new Double(value.toString())));

				properties.useYearsInJobsProtocol(optionsPanel
				  .getUseYearInJobProtocolCheckBox().isSelected());

				/* salvo */
				properties.store();
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}
		}
	}
}
