package it.greentone.gui.panel;

import it.greentone.ConfigurationProperties;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.Currency;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
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
 * Pannello delle configurazioni utente.
 * 
 * @author Giuseppe Caliendo
 */
@Component
@SuppressWarnings("serial")
public class OptionsPanel extends ContextualPanel<Void>
{
	@Inject
	private ConfigurationProperties properties;
	@Inject
	private ActionProvider actionProvider;
	private final String panelBundle;
	private JPanel systemPanel;
	private JCheckBox checkUpdateCheckBox;
	private JPanel appPanel;
	private JFormattedTextField vacazioneTextField;
	private JFormattedTextField vacazioneAiutanteTextField;
	private JCheckBox useYearInJobProtocolCheckBox;

	/**
	 * Pannello delle configurazioni utente.
	 */
	public OptionsPanel()
	{
		super();
		panelBundle = "viewOptions";
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}

	@Override
	protected JXTable createContentTable()
	{
		return null;
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JPanel headerPanel = new JPanel(new MigLayout("", "[100%]"));
		headerPanel.add(getSystemPanel(), "growx,wrap");
		headerPanel.add(getAppPanel(), "growx");
		return headerPanel;
	}

	@Override
	public void setup()
	{
		super.setup();

		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getSaveOptions());

		/* aggiorno il pannello */
		getCheckUpdateCheckBox().setSelected(properties.isCheckUpdateActivated());
		getVacazioneTextField().setValue(properties.getVacazionePrice());
		getVacazioneAiutanteTextField().setValue(
		  properties.getVacazioneHelperPrice());
		getUseYearInJobProtocolCheckBox().setSelected(
		  properties.getUseYearsInJobsProtocol());
	}

	private JPanel getSystemPanel()
	{
		if(systemPanel == null)
		{
			systemPanel = new JPanel(new MigLayout());
			systemPanel.setBorder(BorderFactory.createTitledBorder(getResourceMap()
			  .getString("viewOptions.Panel.systemTitle")));
			JLabel checkUpdateLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.checkUpdate"));
			systemPanel.add(checkUpdateLabel);
			systemPanel.add(getCheckUpdateCheckBox());
		}
		return systemPanel;
	}

	/**
	 * Restituisce il flag di abilitazione del controllo degli aggiornamenti.
	 * 
	 * @return il flag di abilitazione del controllo degli aggiornamenti
	 */
	public JCheckBox getCheckUpdateCheckBox()
	{
		if(checkUpdateCheckBox == null)
		{
			checkUpdateCheckBox = new JCheckBox();
		}
		return checkUpdateCheckBox;
	}

	private JPanel getAppPanel()
	{
		if(appPanel == null)
		{
			appPanel = new JPanel(new MigLayout("", "[][10%]"));
			appPanel.setBorder(BorderFactory.createTitledBorder(getResourceMap()
			  .getString("viewOptions.Panel.appTitle")));
			JLabel vacazioneLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.vacazione"));
			JLabel vacazioneAiutanteLabel =
			  new JLabel(getResourceMap().getString(
			    "viewOptions.Panel.vacazioneAiutante"));
			JLabel useYearInJobsProtocolLabel =
			  new JLabel(getResourceMap().getString(
			    "viewOptions.Panel.useYearInJobsProtocol"));

			appPanel.add(vacazioneLabel);
			appPanel.add(getVacazioneTextField(), "growx,wrap");
			appPanel.add(vacazioneAiutanteLabel);
			appPanel.add(getVacazioneAiutanteTextField(), "growx,wrap");
			appPanel.add(useYearInJobsProtocolLabel);
			appPanel.add(getUseYearInJobProtocolCheckBox(), "growx");
		}
		return appPanel;
	}

	/**
	 * Restituisce il campo dell'importo della vacazione del professionista.
	 * 
	 * @return il campo dell'importo della vacazione del professionista
	 */
	public JFormattedTextField getVacazioneTextField()
	{
		if(vacazioneTextField == null)
		{
			DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
			decimalFormat.setCurrency(Currency.getInstance("EUR"));
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setGroupingUsed(false);
			vacazioneTextField = new JFormattedTextField(decimalFormat);
			/* Issue 39: accettare anche il punto come punto separatore dei decimali */
			vacazioneTextField.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(FocusEvent e)
					{
						vacazioneTextField.setText(vacazioneTextField.getText().replace(
						  '.', ','));
					}
				});
		}
		return vacazioneTextField;
	}

	/**
	 * Restituisce il campo dell'importo della vacazione dell'aiutante.
	 * 
	 * @return il campo dell'importo della vacazione dell'aiutante
	 */
	public JFormattedTextField getVacazioneAiutanteTextField()
	{
		if(vacazioneAiutanteTextField == null)
		{
			DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
			decimalFormat.setCurrency(Currency.getInstance("EUR"));
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			decimalFormat.setGroupingUsed(false);
			vacazioneAiutanteTextField = new JFormattedTextField(decimalFormat);
			vacazioneAiutanteTextField.addFocusListener(new FocusAdapter()
				{
					@Override
					public void focusLost(FocusEvent e)
					{
						vacazioneAiutanteTextField.setText(vacazioneAiutanteTextField
						  .getText().replace('.', ','));
					}
				});
		}
		return vacazioneAiutanteTextField;
	}

	/**
	 * Restituisce il flag per l'uso dell'anno nella composizione del protocollo
	 * di un incarico.
	 * 
	 * @return il flag per l'uso dell'anno nella composizione del protocollo di un
	 *         incarico
	 */
	public JCheckBox getUseYearInJobProtocolCheckBox()
	{
		if(useYearInJobProtocolCheckBox == null)
		{
			useYearInJobProtocolCheckBox = new JCheckBox();
		}
		return useYearInJobProtocolCheckBox;
	}
}
