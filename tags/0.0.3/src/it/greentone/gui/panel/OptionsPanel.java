package it.greentone.gui.panel;

import it.greentone.ConfigurationProperties;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

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

	/**
	 * Pannello delle configurazioni utente.
	 */
	public OptionsPanel()
	{
		super();
		remove(getContextualToolBar());
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
		JPanel headerPanel = new JPanel(new MigLayout("flowy"));
		headerPanel.add(getSystemPanel());
		headerPanel.add(getAppPanel());
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
		getVacazioneTextField().setText("" + properties.getVacazionePrice());
		getVacazioneAiutanteTextField().setText(
		  "" + properties.getVacazioneHelperPrice());
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
			appPanel = new JPanel(new MigLayout());
			appPanel.setBorder(BorderFactory.createTitledBorder(getResourceMap()
			  .getString("viewOptions.Panel.appTitle")));
			JLabel vacazioneLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.vacazione"));
			JLabel vacazioneAiutanteLabel =
			  new JLabel(getResourceMap().getString(
			    "viewOptions.Panel.vacazioneAiutante"));

			appPanel.add(vacazioneLabel);
			appPanel.add(getVacazioneTextField(), "wrap");
			appPanel.add(vacazioneAiutanteLabel);
			appPanel.add(getVacazioneAiutanteTextField());
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
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			vacazioneTextField = new JFormattedTextField(decimalFormat);
			vacazioneTextField.setColumns(4);
			/* Issue 39: accettare anche il punto come punto separatore dei decimali */
			vacazioneTextField.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyReleased(KeyEvent e)
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
			decimalFormat.setMaximumFractionDigits(2);
			decimalFormat.setMinimumFractionDigits(2);
			vacazioneAiutanteTextField = new JFormattedTextField(decimalFormat);
			vacazioneAiutanteTextField.setColumns(4);
			vacazioneAiutanteTextField.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyReleased(KeyEvent e)
					{
						vacazioneAiutanteTextField.setText(vacazioneAiutanteTextField
						  .getText().replace('.', ','));
					}
				});
		}
		return vacazioneAiutanteTextField;
	}
}