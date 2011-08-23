package it.greentone.gui;

import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeletePersonAction;
import it.greentone.gui.action.EditUserAction;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.util.Comparator;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
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
public class PersonsPanel extends ContextualPanel
{
	private static final String LOCALIZATION_PREFIX = "viewPersons.Panel.";

	@Inject
	private PersonService personService;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private EditUserAction editUserAction;
	@Inject
	private DeletePersonAction deletePersonAction;
	private boolean isNewPerson;
	private Person selectedPerson;
	private SortedList<Person> sortedPersonEventList;
	private EventList<Person> personEventList;
	private EventJXTableModel<Person> tableModel;
	private final String panelTitle;
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

	/**
	 * Pannello di gestione delle persone in anagrafica.
	 */
	public PersonsPanel()
	{
		super();
		panelTitle = getResourceMap().getString(LOCALIZATION_PREFIX + "title");
	}

	@Override
	public JPanel createHeaderPanel()
	{
		JLabel nameLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "name"));
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
		headerPanel.add(nameLabel, "gap para");
		headerPanel.add(getNameTextField(), "growx, wrap");
		headerPanel.add(addressLabel, "gap para");
		headerPanel.add(getAddressTextField());
		headerPanel.add(cityLabel, "gap para");
		headerPanel.add(getCityTextField());
		headerPanel.add(provinceLabel, "gap para");
		headerPanel.add(getProvinceTextField());
		headerPanel.add(capLabel, "gap para");
		headerPanel.add(getCapTextField(), "wrap");
		headerPanel.add(isLegalLabel, "gap para");
		headerPanel.add(getIsLegalCheckBox());
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
							  setNewPerson(false);
							  selectedPerson = getSortedPersonEventList().get(selectedRow);
							  getNameTextField().setText(selectedPerson.getName());
							  getAddressTextField().setText(selectedPerson.getAddress());
							  getCityTextField().setText(selectedPerson.getCity());
							  getProvinceTextField().setText(selectedPerson.getProvince());
							  getCapTextField().setText(selectedPerson.getCap());
							  getCfTexField().setText(selectedPerson.getCf());
							  getPivaTextField().setText(selectedPerson.getPiva());
							  getTelephone1TextField()
							    .setText(selectedPerson.getTelephone1());
							  getTelephone2TextField()
							    .setText(selectedPerson.getTelephone2());
							  getFaxTextField().setText(selectedPerson.getFax());
							  getEmailTextField().setText(selectedPerson.getEmail());
							  getIsLegalCheckBox().setSelected(
							    selectedPerson.getPiva() != null);
							  getIdentityCardTextField().setText(
							    selectedPerson.getIdentityCard());
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
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddPerson());
		getContextualToolBar().add(actionProvider.getSavePerson());
		getContextualToolBar().add(actionProvider.getDeletePerson());
		getContextualToolBar().addSeparator();
		getContextualToolBar().add(actionProvider.getEditUser());

		/* imposto la modalità di aggiunta risorsa */
		setNewPerson(true);

		/* aggiorno la tabella delle persone in anagrafica */
		personEventList = new BasicEventList<Person>();
		personEventList.addAll(personService.getAllPersons());
		sortedPersonEventList =
		  new SortedList<Person>(personEventList, new Comparator<Person>()
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
		  new EventJXTableModel<Person>(sortedPersonEventList, properties,
		    columnsName, writable);
		getContentTable().setModel(tableModel);
	}

	@Override
	public void clearForm()
	{
		getNameTextField().setText(null);
		getAddressTextField().setText(null);
		getCityTextField().setText(null);
		getProvinceTextField().setText(null);
		getCapTextField().setText(null);
		getCfTexField().setText(null);
		getPivaTextField().setText(null);
		getTelephone1TextField().setText(null);
		getTelephone2TextField().setText(null);
		getFaxTextField().setText(null);
		getEmailTextField().setText(null);
		getIsLegalCheckBox().setSelected(false);
		getIdentityCardTextField().setText(null);
	}

	public JTextField getNameTextField()
	{
		if(nameTextField == null)
		{
			nameTextField = new JTextField(25);
		}
		return nameTextField;
	}

	public JTextField getAddressTextField()
	{
		if(addressTextField == null)
		{
			addressTextField = new JTextField(25);
		}
		return addressTextField;
	}

	public JTextField getCityTextField()
	{
		if(cityTextField == null)
		{
			cityTextField = new JTextField(20);
		}
		return cityTextField;
	}

	public JTextField getProvinceTextField()
	{
		if(provinceTextField == null)
		{
			provinceTextField = new JTextField(20);
		}
		return provinceTextField;
	}

	public JTextField getCapTextField()
	{
		if(capTextField == null)
		{
			capTextField = new JTextField(5);
		}
		return capTextField;
	}

	public JTextField getCfTexField()
	{
		if(cfTexField == null)
		{
			cfTexField = new JTextField(16);
		}
		return cfTexField;
	}

	public JTextField getPivaTextField()
	{
		if(pivaTextField == null)
		{
			pivaTextField = new JTextField(9);
		}
		return pivaTextField;
	}

	public JTextField getTelephone1TextField()
	{
		if(telephone1TextField == null)
		{
			telephone1TextField = new JTextField(12);
		}
		return telephone1TextField;
	}

	public JTextField getTelephone2TextField()
	{
		if(telephone2TextField == null)
		{
			telephone2TextField = new JTextField(12);
		}
		return telephone2TextField;
	}

	public JTextField getFaxTextField()
	{
		if(faxTextField == null)
		{
			faxTextField = new JTextField(9);
		}
		return faxTextField;
	}

	public JTextField getEmailTextField()
	{
		if(emailTextField == null)
		{
			emailTextField = new JTextField(30);
		}
		return emailTextField;
	}

	public JCheckBox getIsLegalCheckBox()
	{
		if(isLegalCheckBox == null)
		{
			isLegalCheckBox = new JCheckBox();
		}
		return isLegalCheckBox;
	}

	public JTextField getIdentityCardTextField()
	{
		if(identityCardTextField == null)
		{
			identityCardTextField = new JTextField(9);
		}
		return identityCardTextField;
	}

	/**
	 * Restituisce <code>true</code> se la persona è da aggiungere,
	 * <code>false</code> se modificata.
	 * 
	 * @return <code>true</code> se la persona è da aggiungere, <code>false</code>
	 *         se modificata
	 */
	public boolean isNewPerson()
	{
		return isNewPerson;
	}

	/**
	 * Imposta lo stato di nuova persona al pannello.
	 * 
	 * @param isNewPerson
	 *          <code>true</code> se la modifica nel pannello implica una nuova
	 *          persona, <code>false</code> altrimenti
	 */
	public void setNewPerson(boolean isNewPerson)
	{
		this.isNewPerson = isNewPerson;
	}

	/**
	 * Restituisce la persona correntemente selezionata.
	 * 
	 * @return la persona correntemente selezionata
	 */
	public Person getSelectedPerson()
	{
		return selectedPerson;
	}

	/**
	 * Restituisce la lista delle persone disponibili in anagrafica.
	 * 
	 * @return la lista delle persone disponibili in anagrafica
	 */
	public EventList<Person> getPersonEventList()
	{
		return personEventList;
	}

	protected SortedList<Person> getSortedPersonEventList()
	{
		return sortedPersonEventList;
	}

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}
}
