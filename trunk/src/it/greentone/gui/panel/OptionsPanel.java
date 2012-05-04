package it.greentone.gui.panel;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenToneLogProvider;
import it.greentone.gui.action.ActionProvider;
import it.greentone.persistence.Office;
import it.greentone.persistence.OfficeService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Currency;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	@Inject
	private GreenToneLogProvider logProvider;
	@Inject
	private OfficeService officeService;
	private final String panelBundle;
	private JPanel systemPanel;
	private JCheckBox checkUpdateCheckBox;
	private JPanel appPanel;
	private JFormattedTextField vacazioneTextField;
	private JFormattedTextField vacazioneAiutanteTextField;
	private JCheckBox useYearInJobProtocolCheckBox;
	private JButton deleteLogsButton;
	private JButton deleteLogoButton;
	private JTextField nameTextField;
	private JTextField addressTextField;
	private JTextField cityTextField;
	private JTextField provinceTextField;
	private JTextField capTextField;
	private JTextField cfTextField;
	private JTextField pivaTextField;
	private JTextField telephone1TextField;
	private JTextField telephone2TextField;
	private JTextField faxTextField;
	private JTextField emailTextField;
	private ImagePreviewPanel logoPreviewPanel;
	private JButton logoButton;

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
	public JXTable getContentTable()
	{
		return null;
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JPanel headerPanel = new JPanel(new MigLayout("", "[100%]"));
		headerPanel.add(getAppPanel(), "growx,wrap");
		headerPanel.add(getSystemPanel(), "growx");
		return headerPanel;
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
			JLabel deleteLogsLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.deleteLogs"));

			systemPanel.add(checkUpdateLabel, "gap para");
			systemPanel.add(getCheckUpdateCheckBox(), "wrap");
			systemPanel.add(deleteLogsLabel, "gap para");
			systemPanel.add(getDeleteLogsButton());
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
			appPanel =
			  new JPanel(new MigLayout("", "[][10%][][10%][][10%][][10%]",
			    "[][][][][30%][][][]"));
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
			JLabel nameLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeName"));
			JLabel addressLabel =
			  new JLabel(getResourceMap()
			    .getString("viewOptions.Panel.officeAddress"));
			JLabel cityLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeCity"));
			JLabel provinceLabel =
			  new JLabel(getResourceMap().getString(
			    "viewOptions.Panel.officeProvince"));
			JLabel capLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeCAP"));
			JLabel pivaLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officePIVA"));
			JLabel cfLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeCF"));
			JLabel tel1Label =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeTel1"));
			JLabel tel2Label =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeTel2"));
			JLabel faxLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeFax"));
			JLabel emailLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeEmail"));
			JLabel logoLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.officeLogo"));

			// dati relativi allo studio professionale
			appPanel.add(nameLabel, "gap para");
			appPanel.add(getNameTextField(), "span 2, growx, wrap");

			appPanel.add(addressLabel, "gap para");
			appPanel.add(getAddressTextField(), "growx");
			appPanel.add(cityLabel, "gap para");
			appPanel.add(getCityTextField(), "growx");
			appPanel.add(provinceLabel, "gap para");
			appPanel.add(getProvinceTextField(), "growx");
			appPanel.add(capLabel, "gap para");
			appPanel.add(getCapTextField(), "growx, wrap");

			appPanel.add(pivaLabel, "gap para");
			appPanel.add(getPivaTextField(), "growx");
			appPanel.add(cfLabel, "gap para");
			appPanel.add(getCfTextField(), "growx, wrap");

			appPanel.add(tel1Label, "gap para");
			appPanel.add(getTelephone1TextField(), "growx");
			appPanel.add(tel2Label, "gap para");
			appPanel.add(getTelephone2TextField(), "growx");
			appPanel.add(faxLabel, "gap para");
			appPanel.add(getFaxTextField(), "growx");
			appPanel.add(emailLabel, "gap para");
			appPanel.add(getEmailTextField(), "growx, wrap");

			appPanel.add(logoLabel, "gap para");
			appPanel.add(getLogoPreviewPanel(), "grow");
			appPanel.add(getDeleteLogoButton(), "growx");
			appPanel.add(getLogoButton(), "growx, wrap");

			// dati relativi agli importi di vacazione
			appPanel.add(vacazioneLabel, "gap para");
			appPanel.add(getVacazioneTextField(), "growx,wrap");
			appPanel.add(vacazioneAiutanteLabel, "gap para");
			appPanel.add(getVacazioneAiutanteTextField(), "growx,wrap");
			appPanel.add(useYearInJobsProtocolLabel, "gap para");
			appPanel.add(getUseYearInJobProtocolCheckBox(), "growx");
		}
		return appPanel;
	}

	/**
	 * Restituisce il pannello contenente il logo dello studio.
	 * 
	 * @return il pannello contenente il logo dello studio
	 */
	public ImagePreviewPanel getLogoPreviewPanel()
	{
		if(logoPreviewPanel == null)
		{
			logoPreviewPanel = new ImagePreviewPanel();
		}
		return logoPreviewPanel;
	}

	/**
	 * Restituisce il bottone per la scelta del logo dello studio.
	 * 
	 * @return il bottone per la scelta del logo dello studio
	 */
	public JButton getLogoButton()
	{
		if(logoButton == null)
		{
			logoButton =
			  new JButton(getResourceMap().getString("viewOptions.Panel.open"));
			logoButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						JFileChooser chooser = new JFileChooser();
						ImagePreviewPanel preview = new ImagePreviewPanel();
						chooser.setAccessory(preview);
						chooser.addPropertyChangeListener(preview);
						int returnVal = chooser.showOpenDialog(OptionsPanel.this);
						if(returnVal == JFileChooser.APPROVE_OPTION)
						{
							final File file = chooser.getSelectedFile();
							getLogoPreviewPanel().setImage(file);
						}
					}
				});
		}
		return logoButton;
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

	/**
	 * Restituisce il bottone adebito all'eliminazione dei log del programma.
	 * 
	 * @return il bottone adebito all'eliminazione dei log del programma
	 */
	public JButton getDeleteLogsButton()
	{
		if(deleteLogsButton == null)
		{
			deleteLogsButton = new JButton(new AbstractAction()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						boolean result = logProvider.deleteLogs();
						if(result)
							JOptionPane.showMessageDialog(getParent(), getResourceMap()
							  .getString("viewOptions.Panel.logsDeleted"), getResourceMap()
							  .getString("Application.title"),
							  JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(getParent(), getResourceMap()
							  .getString("viewOptions.Panel.logsNotDeleted"),
							  getResourceMap().getString("Application.title"),
							  JOptionPane.WARNING_MESSAGE);
					}
				});
			deleteLogsButton.setIcon(getResourceMap().getIcon(
			  "viewOptions.Panel.deleteLogsIcon"));
			deleteLogsButton.setText(getResourceMap().getString(
			  "viewOptions.Panel.deleteLogsText"));
		}
		return deleteLogsButton;
	}

	/**
	 * Restituisce il bottone per l'eliminazione del logo nel database.
	 * 
	 * @return il bottone per l'eliminazione del logo nel database
	 */
	public JButton getDeleteLogoButton()
	{
		if(deleteLogoButton == null)
		{
			deleteLogoButton = new JButton(new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						File file = null;
						getLogoPreviewPanel().setImage(file);
					}
				});
			deleteLogoButton.setIcon(getResourceMap().getIcon(
			  "viewOptions.Panel.deleteLogsIcon"));
			deleteLogoButton.setText(getResourceMap().getString(
			  "viewOptions.Panel.deleteLogsText"));
		}
		return deleteLogoButton;
	}

	/**
	 * Restituisce il campo di inserimento del nome.
	 * 
	 * @return il campo di inserimento del nome
	 */
	public JTextField getNameTextField()
	{
		if(nameTextField == null)
			nameTextField = new JTextField();
		return nameTextField;
	}

	/**
	 * Restituisce il campo di inserimento del telefono principale.
	 * 
	 * @return il campo di inserimento del telefono principale
	 */
	public JTextField getTelephone1TextField()
	{
		if(telephone1TextField == null)
			telephone1TextField = new JTextField();
		return telephone1TextField;
	}

	/**
	 * Restituisce il campo di inserimento del telefono secondario.
	 * 
	 * @return il campo di inserimento del telefono secondario
	 */
	public JTextField getTelephone2TextField()
	{
		if(telephone2TextField == null)
			telephone2TextField = new JTextField();
		return telephone2TextField;
	}

	/**
	 * Restituisce il campo di inserimento della descrizione.
	 * 
	 * @return il campo di inserimento della descrizione
	 */
	public JTextField getAddressTextField()
	{
		if(addressTextField == null)
			addressTextField = new JTextField();
		return addressTextField;
	}

	/**
	 * Restituisce il campo di inserimento della città.
	 * 
	 * @return il campo di inserimento della città
	 */
	public JTextField getCityTextField()
	{
		if(cityTextField == null)
			cityTextField = new JTextField();
		return cityTextField;
	}

	/**
	 * Restituisce il campo di inserimento della provincia.
	 * 
	 * @return il campo di inserimento della provincia
	 */
	public JTextField getProvinceTextField()
	{
		if(provinceTextField == null)
			provinceTextField = new JTextField();
		return provinceTextField;
	}

	/**
	 * Restituisce il campo di inserimento del codice di avviamento postale.
	 * 
	 * @return il campo di inserimento del codice di avviamento postale
	 */
	public JTextField getCapTextField()
	{
		if(capTextField == null)
			capTextField = new JTextField();
		return capTextField;
	}

	/**
	 * Restituisce il campo di inserimento del codice fiscale.
	 * 
	 * @return il campo di inserimento del codice fiscale
	 */
	public JTextField getCfTextField()
	{
		if(cfTextField == null)
			cfTextField = new JTextField();
		return cfTextField;
	}

	/**
	 * Restituisce il campo di inserimento della partita IVA.<br>
	 * Accetta solo 11 cifre
	 * 
	 * @return il campo di inserimento della partita IVA
	 */
	public JTextField getPivaTextField()
	{
		if(pivaTextField == null)
			pivaTextField = new JTextField();
		return pivaTextField;
	}

	/**
	 * Restituisce il campo di inserimento del fax.
	 * 
	 * @return il campo di inserimento del fax
	 */
	public JTextField getFaxTextField()
	{
		if(faxTextField == null)
			faxTextField = new JTextField();
		return faxTextField;
	}

	/**
	 * Restituisce il campo di inserimento dell'indirizzo mail.
	 * 
	 * @return il campo di inserimento dell'indirizzo mail
	 */
	public JTextField getEmailTextField()
	{
		if(emailTextField == null)
			emailTextField = new JTextField();
		return emailTextField;
	}

	@Override
	public Void getItemFromTableRow(int rowIndex)
	{
		return null;
	}

	@Override
	public void initializeToolBar()
	{
		getContextualToolBar().add(actionProvider.getSaveOptions());
	}

	@Override
	public void populateModel()
	{
		/* aggiorno il pannello con le informazioni dello studio */
		Office office = officeService.loadOffice();
		getNameTextField().setText(office.getName());
		getAddressTextField().setText(office.getAddress());
		getCityTextField().setText(office.getCity());
		getProvinceTextField().setText(office.getProvince());
		getCapTextField().setText(office.getCap());
		getCfTextField().setText(office.getCf());
		getPivaTextField().setText(office.getPiva());
		getTelephone1TextField().setText(office.getTelephone1());
		getTelephone2TextField().setText(office.getTelephone2());
		getFaxTextField().setText(office.getFax());
		getEmailTextField().setText(office.getEmail());
		if(office.getLogo() != null)
			getLogoPreviewPanel().setImage(office.getLogo());

		/* altre impostazioni */
		getCheckUpdateCheckBox().setSelected(properties.isCheckUpdateActivated());
		getVacazioneTextField().setValue(properties.getVacazionePrice());
		getVacazioneAiutanteTextField().setValue(
		  properties.getVacazioneHelperPrice());
		getUseYearInJobProtocolCheckBox().setSelected(
		  properties.getUseYearsInJobsProtocol());
	}
}
