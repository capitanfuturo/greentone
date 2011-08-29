package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteDocumentAction;
import it.greentone.gui.action.SaveDocumentAction;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
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
public class DocumentsPanel extends ContextualPanel<Document>
{
	private static final String LOCALIZATION_PREFIX = "viewDocuments.Panel.";
	private final String panelTitle;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private PersonService personService;
	@Inject
	private DocumentService documentService;
	@Inject
	private JobService jobService;
	@Inject
	private DeleteDocumentAction deleteDocumentAction;
	@Inject
	private SaveDocumentAction saveDocumentAction;

	private JTextField protocolTextField;
	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox recipientComboBox;
	private JCheckBox isDigitalCheckBox;
	private JTextField fileTextField;
	private JCheckBox incomingCheckBox;
	private JXDatePicker releaseDateDatePicker;
	private JTextArea notesTextArea;

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

	public JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
		{
			protocolTextField = new JTextField(15);
			registerComponent(protocolTextField);
			protocolTextField.getDocument().addDocumentListener(
			  new DocumentListener()
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
						  saveDocumentAction
						    .setSaveDocumentActionEnabled(GreenToneUtilities
						      .getText(protocolTextField) != null);
					  }
				  });
		}
		return protocolTextField;
	}

	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
		{
			descriptionTextField = new JTextField(20);
			registerComponent(descriptionTextField);
		}
		return descriptionTextField;
	}

	public JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
		{
			jobComboBox = new JComboBox();
			registerComponent(jobComboBox);
		}
		return jobComboBox;
	}

	public JComboBox getRecipientComboBox()
	{
		if(recipientComboBox == null)
		{
			recipientComboBox = new JComboBox();
			registerComponent(recipientComboBox);
		}
		return recipientComboBox;
	}

	public JCheckBox getIsDigitalCheckBox()
	{
		if(isDigitalCheckBox == null)
		{
			isDigitalCheckBox = new JCheckBox();
			registerComponent(isDigitalCheckBox);
		}
		return isDigitalCheckBox;
	}

	public JTextField getFileTextField()
	{
		if(fileTextField == null)
		{
			fileTextField = new JTextField(25);
			registerComponent(fileTextField);
		}
		return fileTextField;
	}

	public JCheckBox getIncomingCheckBox()
	{
		if(incomingCheckBox == null)
		{
			incomingCheckBox = new JCheckBox();
			registerComponent(incomingCheckBox);
		}
		return incomingCheckBox;
	}

	public JXDatePicker getReleaseDateDatePicker()
	{
		if(releaseDateDatePicker == null)
		{
			releaseDateDatePicker = new JXDatePicker();
			registerComponent(releaseDateDatePicker);
		}
		return releaseDateDatePicker;
	}

	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
		{
			notesTextArea = new JTextArea(5, 50);
			registerComponent(notesTextArea);
		}
		return notesTextArea;
	}

	@Override
	public void setup()
	{
		super.setup();
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddDocument());
		getContextualToolBar().add(actionProvider.getSaveDocument());
		getContextualToolBar().add(actionProvider.getDeleteDocument());

		/* carico destinatari */
		EventList<Person> allPersonsEventList = new BasicEventList<Person>();
		allPersonsEventList.addAll(personService.getAllPersons());
		EventComboBoxModel<Person> recipientComboBoxModel =
		  new EventComboBoxModel<Person>(allPersonsEventList);
		getRecipientComboBox().setModel(recipientComboBoxModel);

		/* carico gli incarichi */
		EventComboBoxModel<Job> jobComboBoxModel =
		  new EventComboBoxModel<Job>(jobService.getAllJobs());
		getJobComboBox().setModel(jobComboBoxModel);

		/* aggiorno la tabella dei documenti */
		String[] properties =
		  new String[] {"protocol", "description", "job", "recipient", "isDigital",
		    "uri", "isIncoming", "releaseDate", "notes"};
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
		  new EventJXTableModel<Document>(documentService.getAllDocuments(),
		    new BeanTableFormat<Document>(Document.class, properties, columnsName,
		      writable));
		getContentTable().setModel(tableModel);
	}

	@Override
	protected JXTable createContentTable()
	{
		JXTable documentTable = super.createContentTable();
		documentTable.getSelectionModel().addListSelectionListener(
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
							  setSelectedItem(documentService.getAllDocuments().get(
							    selectedRow));
							  /* aggiorno il pannello */
							  getProtocolTextField().setText(getSelectedItem().getProtocol());
							  getDescriptionTextField().setText(
							    getSelectedItem().getDescription());
							  getJobComboBox().setSelectedItem(getSelectedItem().getJob());
							  getRecipientComboBox().setSelectedItem(
							    getSelectedItem().getRecipient());
							  getIsDigitalCheckBox().setSelected(
							    getSelectedItem().getIsDigital());
							  getFileTextField().setText(getSelectedItem().getUri());
							  getIncomingCheckBox().setSelected(
							    getSelectedItem().getIsIncoming());
							  getReleaseDateDatePicker().setDate(
							    getSelectedItem().getReleaseDate().toDate());
							  getNotesTextArea().setText(getSelectedItem().getNotes());
							  /* abilito le azioni legate alla selezione */
							  deleteDocumentAction.setDeleteDocumentActionEnabled(true);
						  }
						  else
						  {
							  /* disabilito le azioni legate alla selezione */
							  deleteDocumentAction.setDeleteDocumentActionEnabled(false);
						  }
					  }
				  }
			  });
		return documentTable;
	}


	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

	@Override
	public void clearForm()
	{
		getProtocolTextField().setText(null);
		getDescriptionTextField().setText(null);
		getJobComboBox().setSelectedIndex(-1);
		getRecipientComboBox().setSelectedIndex(-1);
		getIsDigitalCheckBox().setSelected(false);
		getFileTextField().setText(null);
		getIncomingCheckBox().setSelected(false);
		getReleaseDateDatePicker().setDate(null);
		getNotesTextArea().setText(null);
	}
}
