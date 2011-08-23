package it.greentone.gui;

import it.greentone.gui.action.ActionProvider;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.util.Calendar;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;
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
 * Pannello di visualizzazione di tutti i documenti presenti in database.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class DocumentsPanel extends ContextualPanel
{
	private static final String LOCALIZATION_PREFIX = "viewDocuments.Panel.";
	private final String panelTitle;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private PersonService personService;
	@Inject
	private DocumentService documentService;

	private JTextField protocolTextField;
	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox recipientComboBox;
	private JCheckBox isDigitalCheckBox;
	private JTextField fileTextField;
	private JCheckBox incomingCheckBox;
	private JXDatePicker releaseDateDatePicker;
	private JTextArea notesTextArea;

	private EventList<Document> documentsEventList;
	private EventJXTableModel<Document> tableModel;

	/**
	 * Pannello di visualizzazione di tutti i documenti presenti in database.
	 */
	public DocumentsPanel()
	{
		super();
		panelTitle = getResourceMap().getString(LOCALIZATION_PREFIX + "title");
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JLabel protocolLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
		JLabel descriptionLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "description"));
		JLabel jobLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "job"));
		JLabel recipientLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "recipient"));
		JLabel isDigitalLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "isDigital"));
		JLabel fileLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "file"));
		JLabel incomingLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "incoming"));
		JLabel releaseDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));
		JLabel notesLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));

		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.add(protocolLabel, "gap para");
		headerPanel.add(getProtocolTextField(), "growx, wrap");
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "growx, wrap");
		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "growx, wrap");
		headerPanel.add(recipientLabel, "gap para");
		headerPanel.add(getRecipientComboBox(), "growx, wrap");
		headerPanel.add(isDigitalLabel, "gap para");
		headerPanel.add(getIsDigitalCheckBox());
		headerPanel.add(fileLabel, "gap para");
		headerPanel.add(getFileTextField(), "growx, wrap");
		headerPanel.add(incomingLabel, "gap para");
		headerPanel.add(getIncomingCheckBox());
		headerPanel.add(releaseDateLabel, "gap para");
		headerPanel.add(getReleaseDateDatePicker(), "growx, wrap");
		headerPanel.add(notesLabel, "gap para");
		headerPanel.add(getNotesTextArea(), "span, growx");
		return headerPanel;
	}

	protected JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
			protocolTextField = new JTextField(15);
		return protocolTextField;
	}

	protected JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
			descriptionTextField = new JTextField(20);
		return descriptionTextField;
	}

	protected JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
			jobComboBox = new JComboBox();
		return jobComboBox;
	}

	protected JComboBox getRecipientComboBox()
	{
		if(recipientComboBox == null)
			recipientComboBox = new JComboBox();
		return recipientComboBox;
	}

	protected JCheckBox getIsDigitalCheckBox()
	{
		if(isDigitalCheckBox == null)
			isDigitalCheckBox = new JCheckBox();
		return isDigitalCheckBox;
	}

	protected JTextField getFileTextField()
	{
		if(fileTextField == null)
			fileTextField = new JTextField(25);
		return fileTextField;
	}

	protected JCheckBox getIncomingCheckBox()
	{
		if(incomingCheckBox == null)
			incomingCheckBox = new JCheckBox();
		return incomingCheckBox;
	}

	protected JXDatePicker getReleaseDateDatePicker()
	{
		if(releaseDateDatePicker == null)
			releaseDateDatePicker =
			  new JXDatePicker(Calendar.getInstance().getTime());
		return releaseDateDatePicker;
	}

	protected JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
			notesTextArea = new JTextArea(5, 50);
		return notesTextArea;
	}

	@Override
	public void setup()
	{
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddDocument());
		getContextualToolBar().add(actionProvider.getSaveDocument());
		getContextualToolBar().add(actionProvider.getDeleteDocument());

		/* carico destinatari */
		EventList<Person> allPersonsEventList = new BasicEventList<Person>();
		allPersonsEventList.addAll(personService.getAllPersons());
		getRecipientComboBox().setModel(
		  new EventComboBoxModel<Person>(allPersonsEventList));

		/* aggiorno la tabella degli incarichi */
		documentsEventList = new BasicEventList<Document>();
		documentsEventList.addAll(documentService.getAllDocuments());
		String[] properties =
		  new String[] {"protocol", "description", "job", "recipient", "isDigital",
		    "uri", "incoming", "releaseDate", "notes"};
		String[] columnsName =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.recipient"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isDigital"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.file"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.incoming"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.notes")};
		boolean[] writable =
		  new boolean[] {false, false, false, false, false, false, false, false,
		    false};

		tableModel =
		  new EventJXTableModel<Document>(documentsEventList, properties,
		    columnsName,
		    writable);
		getContentTable().setModel(tableModel);


	}

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

}
