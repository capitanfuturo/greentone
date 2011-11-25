package it.greentone.gui.panel;

import it.greentone.GreenToneLogger;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeletePersonAction;
import it.greentone.gui.action.EditUserAction;
import it.greentone.gui.action.SavePersonAction;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;
import it.greentone.report.ReportManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.EventJXTableModel;

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
 * Pannello di gestione delle persone in anagrafica.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class PersonsPanel extends ContextualPanel<Person>
{
	private static final String LOCALIZATION_PREFIX = "viewPersons.Panel.";
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private EditUserAction editUserAction;
	@Inject
	private DeletePersonAction deletePersonAction;
	@Inject
	private SavePersonAction savePersonAction;
	@Inject
	private PersonService personService;
	@Inject
	private ReportManager reportManager;
	@Inject
	private GreenToneLogger logger;
	private EventJXTableModel<Person> tableModel;
	private final String panelBundle;
	private JTextField nameTextField;
	private JTextField addressTextField;
	private JTextField cityTextField;
	private JTextField provinceTextField;
	private JTextField capTextField;
	private JFormattedTextField cfTextField;
	private JTextField pivaTextField;
	private JTextField telephone1TextField;
	private JTextField telephone2TextField;
	private JTextField faxTextField;
	private JTextField emailTextField;
	private JCheckBox isLegalCheckBox;
	private JTextField identityCardTextField;
	private JLabel nameLabel;
	private final String[] properties;
	private final String[] columnsNames;
	private final boolean[] writables;

	/**
	 * Pannello di gestione delle persone in anagrafica.
	 */
	public PersonsPanel()
	{
		super();
		panelBundle = "viewPersons";

		properties =
		  new String[] {"name", "address", "city", "province", "cap", "isLegal",
		    "cf", "piva", "identityCard", "telephone1", "telephone2", "fax",
		    "email"};
		columnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.name"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.address"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.city"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.province"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.cap"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isLegal"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.cf"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.piva"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.identityCard"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.telephone1"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.telephone2"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.fax"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.email")};
		writables =
		  new boolean[] {false, false, false, false, false, false, false, false,
		    false, false, false, false, false};
	}

	@Override
	public JPanel createHeaderPanel()
	{
		JLabel addressLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "address"));
		JLabel cityLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "city"));
		JLabel provinceLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "province"));
		JLabel capLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "cap"));
		JLabel cfLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "cf"));
		JLabel pivaLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "piva"));
		JLabel telephone1Label =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "telephone1"));
		JLabel telephone2Label =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "telephone2"));
		JLabel faxLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "fax"));
		JLabel emailLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "email"));
		JLabel isLegalLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "isLegal"));
		JLabel identityCardLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "identityCard"));
		JLabel requiredLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "requiredField"));

		JPanel headerPanel =
		  new JPanel(new MigLayout("", "[][10%][][10%][][10%][][10%]"));

		headerPanel.add(isLegalLabel, "gap para");
		headerPanel.add(getIsLegalCheckBox(), "wrap");

		headerPanel.add(getNameLabel(), "gap para");
		headerPanel.add(getNameTextField(), "span 2, growx, wrap");

		headerPanel.add(addressLabel, "gap para");
		headerPanel.add(getAddressTextField(), "growx");
		headerPanel.add(cityLabel, "gap para");
		headerPanel.add(getCityTextField(), "growx");
		headerPanel.add(provinceLabel, "gap para");
		headerPanel.add(getProvinceTextField(), "growx");
		headerPanel.add(capLabel, "gap para");
		headerPanel.add(getCapTextField(), "growx, wrap");

		headerPanel.add(cfLabel, "gap para");
		headerPanel.add(getCfTexField(), "growx");
		headerPanel.add(pivaLabel, "gap para");
		headerPanel.add(getPivaTextField(), "growx");
		headerPanel.add(identityCardLabel, "gap para");
		headerPanel.add(getIdentityCardTextField(), "growx, wrap");

		headerPanel.add(telephone1Label, "gap para");
		headerPanel.add(getTelephone1TextField(), "growx");
		headerPanel.add(telephone2Label, "gap para");
		headerPanel.add(getTelephone2TextField(), "growx");
		headerPanel.add(faxLabel, "gap para");
		headerPanel.add(getFaxTextField(), "growx");
		headerPanel.add(emailLabel, "gap para");
		headerPanel.add(getEmailTextField(), "growx, wrap");
		headerPanel.add(requiredLabel);
		return headerPanel;
	}

	@Override
	protected JXTable createContentTable()
	{
		final JXTable personTable = super.createContentTable();
		personTable.getSelectionModel().addListSelectionListener(
		  new ListSelectionListener()
			  {
				  @Override
				  public void valueChanged(ListSelectionEvent e)
				  {
					  if(!e.getValueIsAdjusting())
					  {
						  int selectedRow = getContentTable().getSelectedRow();
						  if(selectedRow > -1)
						  {
							  setStatus(EStatus.EDIT);
							  int rowIndexToModel =
							    personTable.convertRowIndexToModel(selectedRow);
							  setSelectedItem(personService.getAllPersons().get(
							    rowIndexToModel));
							  getNameTextField().setText(getSelectedItem().getName());
							  getAddressTextField().setText(getSelectedItem().getAddress());
							  getCityTextField().setText(getSelectedItem().getCity());
							  getProvinceTextField().setText(getSelectedItem().getProvince());
							  getCapTextField().setText(getSelectedItem().getCap());
							  getCfTexField().setText(getSelectedItem().getCf());
							  getPivaTextField().setText(getSelectedItem().getPiva());
							  getTelephone1TextField().setText(
							    getSelectedItem().getTelephone1());
							  getTelephone2TextField().setText(
							    getSelectedItem().getTelephone2());
							  getFaxTextField().setText(getSelectedItem().getFax());
							  getEmailTextField().setText(getSelectedItem().getEmail());
							  getIsLegalCheckBox()
							    .setSelected(getSelectedItem().getIsLegal());
							  getIdentityCardTextField().setText(
							    getSelectedItem().getIdentityCard());
							  /* abilito le azioni legate alla selezione */
							  deletePersonAction.setDeletePersonActionEnabled(true);
							  editUserAction.setEditUserActionEnabled(true);
							  /* aggiorno l'etichetta del nome/ragione sociale */
							  toggleNameLabel();
						  }
						  else
						  {
							  /* disabilito le azioni legate alla selezione */
							  deletePersonAction.setDeletePersonActionEnabled(false);
							  editUserAction.setEditUserActionEnabled(false);
						  }
					  }
				  }
			  });
		return personTable;
	}

	@Override
	public void setup()
	{
		super.setup();

		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().add(actionProvider.getAddPerson());
		getContextualToolBar().add(actionProvider.getSavePerson());
		getContextualToolBar().add(actionProvider.getDeletePerson());

		// TODO
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(
		  new AbstractAction("", getResourceMap().getIcon(
		    "viewReports.Action.largeIcon"))
			  {

				  @Override
				  public void actionPerformed(ActionEvent arg0)
				  {
					  reportManager.generateTest();
				  }
			  });
		// getContextualToolBar().add(actionProvider.getEditUser());

		/* aggiorno la tabella delle persone in anagrafica */
		tableModel =
		  new EventJXTableModel<Person>(personService.getAllPersons(),
		    new BeanTableFormat<Person>(Person.class, properties, columnsNames,
		      writables));
		getContentTable().setModel(tableModel);
		getContentTable().setSortOrder(0, SortOrder.ASCENDING);
	}

	/**
	 * Restituisce l'etichetta della ragione sociale.
	 * 
	 * @return l'etichetta della ragione sociale
	 */
	public JLabel getNameLabel()
	{
		if(nameLabel == null)
		{
			nameLabel =
			  new JLabel(getResourceMap().getString(
			    LOCALIZATION_PREFIX + "surnameName"));
		}
		return nameLabel;
	}

	/**
	 * Restituisce il campo di inserimento del nome.
	 * 
	 * @return il campo di inserimento del nome
	 */
	public JTextField getNameTextField()
	{
		if(nameTextField == null)
		{
			nameTextField = new JTextField();
			registerComponent(nameTextField);
			nameTextField.getDocument().addDocumentListener(new DocumentListener()
				{

					@Override
					public void removeUpdate(DocumentEvent e)
					{
						toogleAction();
					}

					@Override
					public void insertUpdate(DocumentEvent e)
					{
						toogleAction();
					}

					@Override
					public void changedUpdate(DocumentEvent e)
					{
						toogleAction();
					}

					private void toogleAction()
					{
						savePersonAction.setSavePersonActionEnabled(GreenToneUtilities
						  .getText(nameTextField) != null);
					}
				});
		}
		return nameTextField;
	}

	/**
	 * Restituisce il campo di inserimento della descrizione.
	 * 
	 * @return il campo di inserimento della descrizione
	 */
	public JTextField getAddressTextField()
	{
		if(addressTextField == null)
		{
			addressTextField = new JTextField();
			registerComponent(addressTextField);
		}
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
		{
			cityTextField = new JTextField();
			registerComponent(cityTextField);
		}
		return cityTextField;
	}

	/**
	 * Restituisce il campo di inserimento della provincia.<br>
	 * Accetta solo 2 lettere
	 * 
	 * @return il campo di inserimento della provincia
	 */
	public JTextField getProvinceTextField()
	{
		if(provinceTextField == null)
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("UU");
			provinceTextField = new JFormattedTextField(mf);
			registerComponent(provinceTextField);
		}
		return provinceTextField;
	}

	/**
	 * Restituisce il campo di inserimento del codice di avviamento postale.<br>
	 * Accetta 5 cifre
	 * 
	 * @return il campo di inserimento del codice di avviamento postale
	 */
	public JTextField getCapTextField()
	{
		if(capTextField == null)
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("#####");
			capTextField = new JFormattedTextField(mf);
			registerComponent(capTextField);
		}
		return capTextField;
	}

	/**
	 * Restituisce il campo di inserimento del codice fiscale.<br>
	 * Accetta valori contenti, in sequenza:
	 * <ol>
	 * <li>6 lettere</li>
	 * <li>2 cifre numeriche</li>
	 * <li>1 lettera</li>
	 * <li>2 cifre numeriche</li>
	 * <li>1 lettera</li>
	 * <li>3 cifre numeriche</li>
	 * <li>1 lettera</li>
	 * </ol>
	 * ES: RSSGPP 00 A 00 G 111 Z
	 * 
	 * @return il campo di inserimento del codice fiscale
	 */
	public JFormattedTextField getCfTexField()
	{
		if(cfTextField == null)
		{
			final MaskFormatter mf =
			  GreenToneUtilities.createMaskFormatter("UUUUUU##U##U###U");
			cfTextField = new JFormattedTextField(mf);
			/*
			 * Issue 25: se è obbligatorio il codice fiscale, flag legale non
			 * impostato, allora in caso di malformattazione del codice fiscale far
			 * comparire un popup che spiega il problema
			 */
			cfTextField.addFocusListener(new FocusAdapter()
				{

					@Override
					public void focusLost(FocusEvent e)
					{
						try
						{
							if(!getIsLegalCheckBox().isSelected())
							{
								if(mf.stringToValue(cfTextField.getText()) == null)
								{
									showCFMessageDialog();
								}
							}
						}
						catch(Exception e1)
						{
							logger.getLogger().info(
							  getResourceMap().getString("ErrorMessage.parsingValue"));
						}
					}
				});
			registerComponent(cfTextField);
		}
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
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("###########");
			pivaTextField = new JFormattedTextField(mf);
			registerComponent(pivaTextField);
		}
		return pivaTextField;
	}

	/**
	 * Restituisce il campo di inserimento del telefono principale.
	 * 
	 * @return il campo di inserimento del telefono principale
	 */
	public JTextField getTelephone1TextField()
	{
		if(telephone1TextField == null)
		{
			telephone1TextField = new JTextField();
			registerComponent(telephone1TextField);
		}
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
		{
			telephone2TextField = new JTextField();
			registerComponent(telephone2TextField);
		}
		return telephone2TextField;
	}

	/**
	 * Restituisce il campo di inserimento del fax.
	 * 
	 * @return il campo di inserimento del fax
	 */
	public JTextField getFaxTextField()
	{
		if(faxTextField == null)
		{
			faxTextField = new JTextField();
			registerComponent(faxTextField);
		}
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
		{
			emailTextField = new JTextField();
			registerComponent(emailTextField);
		}
		return emailTextField;
	}

	/**
	 * Restituisce il flag che marca una persona come legale.
	 * 
	 * @return il flag che marca una persona come legale
	 */
	public JCheckBox getIsLegalCheckBox()
	{
		if(isLegalCheckBox == null)
		{
			isLegalCheckBox = new JCheckBox();
			isLegalCheckBox.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						toggleNameLabel();
					}
				});
			registerComponent(isLegalCheckBox);
		}
		return isLegalCheckBox;
	}

	/**
	 * Restituisce il campo di numero di carta di identità.
	 * 
	 * @return il campo di numero di carta di identità
	 */
	public JTextField getIdentityCardTextField()
	{
		if(identityCardTextField == null)
		{
			identityCardTextField = new JTextField();
			registerComponent(identityCardTextField);
		}
		return identityCardTextField;
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}

	/**
	 * Mostra una finestra di dialogo che informa come inserire il campo codice
	 * fiscale.
	 */
	private void showCFMessageDialog()
	{
		JOptionPane.showMessageDialog(this,
		  getResourceMap().getString(LOCALIZATION_PREFIX + "cfMessage"),
		  getResourceMap().getString(LOCALIZATION_PREFIX + "infoTitle"),
		  JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void clearForm()
	{
		super.clearForm();
		toggleNameLabel();
	}

	private void toggleNameLabel()
	{
		if(getIsLegalCheckBox().isSelected())
		{
			getNameLabel().setText(
			  getResourceMap().getString(LOCALIZATION_PREFIX + "name"));
		}
		else
		{
			getNameLabel().setText(
			  getResourceMap().getString(LOCALIZATION_PREFIX + "surnameName"));
		}
	}
}
