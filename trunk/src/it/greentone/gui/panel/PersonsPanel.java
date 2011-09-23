package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeletePersonAction;
import it.greentone.gui.action.EditUserAction;
import it.greentone.gui.action.SavePersonAction;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.SortedList;
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
	private SortedList<Person> sortedPersonEventList;
	private EventJXTableModel<Person> tableModel;
	private final String panelBundle;
	private JTextField nameTextField;
	private JTextField addressTextField;
	private JTextField cityTextField;
	private JTextField provinceTextField;
	private JTextField capTextField;
	private JTextField cfTexField;
	private JTextField pivaTextField;
	private JTextField telephone1TextField;
	private JTextField telephone2TextField;
	private JTextField faxTextField;
	private JTextField emailTextField;
	private JCheckBox isLegalCheckBox;
	private JTextField identityCardTextField;
	private JLabel nameLabel;

	/**
	 * Pannello di gestione delle persone in anagrafica.
	 */
	public PersonsPanel()
	{
		super();
		panelBundle = "viewPersons";
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

		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.add(isLegalLabel, "gap para");
		headerPanel.add(getIsLegalCheckBox(), "growx, wrap");
		headerPanel.add(getNameLabel(), "gap para");
		headerPanel.add(getNameTextField(), "growx, wrap");
		headerPanel.add(addressLabel, "gap para");
		headerPanel.add(getAddressTextField());
		headerPanel.add(cityLabel, "gap para");
		headerPanel.add(getCityTextField());
		headerPanel.add(provinceLabel, "gap para");
		headerPanel.add(getProvinceTextField());
		headerPanel.add(capLabel, "gap para");
		headerPanel.add(getCapTextField(), "wrap");
		headerPanel.add(cfLabel, "gap para");
		headerPanel.add(getCfTexField());
		headerPanel.add(pivaLabel, "gap para");
		headerPanel.add(getPivaTextField(), "wrap");
		headerPanel.add(identityCardLabel, "gap para");
		headerPanel.add(getIdentityCardTextField(), "wrap");
		headerPanel.add(telephone1Label, "gap para");
		headerPanel.add(getTelephone1TextField());
		headerPanel.add(telephone2Label, "gap para");
		headerPanel.add(getTelephone2TextField());
		headerPanel.add(faxLabel, "gap para");
		headerPanel.add(getFaxTextField());
		headerPanel.add(emailLabel, "gap para");
		headerPanel.add(getEmailTextField(), "span, growx");
		return headerPanel;
	}

	@Override
	protected JXTable createContentTable()
	{
		JXTable personTable = super.createContentTable();
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
							  setSelectedItem(getSortedPersonEventList().get(selectedRow));
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
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddPerson());
		getContextualToolBar().add(actionProvider.getSavePerson());
		getContextualToolBar().add(actionProvider.getDeletePerson());
		// TODO getContextualToolBar().addSeparator();
		// TODO getContextualToolBar().add(actionProvider.getEditUser());

		/* aggiorno la tabella delle persone in anagrafica */
		sortedPersonEventList =
		  new SortedList<Person>(personService.getAllPersons(),
		    new Comparator<Person>()
			    {
				    @Override
				    public int compare(Person o1, Person o2)
				    {
					    return o1.getName().compareToIgnoreCase(o2.getName());
				    }
			    });
		String[] properties =
		  new String[] {"name", "address", "city", "province", "cap", "isLegal",
		    "cf", "piva", "identityCard", "telephone1", "telephone2", "fax",
		    "email"};
		String[] columnsName =
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
		boolean[] writable =
		  new boolean[] {false, false, false, false, false, false, false, false,
		    false, false, false, false, false};

		tableModel =
		  new EventJXTableModel<Person>(sortedPersonEventList,
		    new BeanTableFormat<Person>(Person.class, properties, columnsName,
		      writable));
		getContentTable().setModel(tableModel);
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
			nameTextField = new JTextField(20);
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
			addressTextField = new JTextField(20);
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
			cityTextField = new JTextField(20);
			registerComponent(cityTextField);
		}
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
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("UU");
			provinceTextField = new JFormattedTextField(mf);
			provinceTextField.setColumns(4);
			registerComponent(provinceTextField);
		}
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
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("#####");
			capTextField = new JFormattedTextField(mf);
			capTextField.setColumns(10);
			registerComponent(capTextField);
		}
		return capTextField;
	}

	/**
	 * Restituisce il campo di inserimento del codice fiscale.
	 * 
	 * @return il campo di inserimento del codice fiscale
	 */
	public JTextField getCfTexField()
	{
		if(cfTexField == null)
		{
			cfTexField = new JTextField(15);
			registerComponent(cfTexField);
		}
		return cfTexField;
	}

	/**
	 * Restituisce il campo di inserimento della partita IVA.
	 * 
	 * @return il campo di inserimento della partita IVA
	 */
	public JTextField getPivaTextField()
	{
		if(pivaTextField == null)
		{
			MaskFormatter mf = GreenToneUtilities.createMaskFormatter("###########");
			pivaTextField = new JFormattedTextField(mf);
			pivaTextField.setColumns(20);
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
			telephone1TextField = new JTextField(12);
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
			telephone2TextField = new JTextField(12);
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
			faxTextField = new JTextField(9);
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
			emailTextField = new JTextField(20);
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
						if(isLegalCheckBox.isSelected())
						{
							getNameLabel().setText(
							  getResourceMap().getString(LOCALIZATION_PREFIX + "name"));
						}
						else
						{
							getNameLabel()
							  .setText(
							    getResourceMap().getString(
							      LOCALIZATION_PREFIX + "surnameName"));
						}
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
			identityCardTextField = new JTextField(9);
			registerComponent(identityCardTextField);
		}
		return identityCardTextField;
	}

	protected SortedList<Person> getSortedPersonEventList()
	{
		return sortedPersonEventList;
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}
}
