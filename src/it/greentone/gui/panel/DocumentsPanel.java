package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.SaveDocumentAction;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.joda.time.DateTime;
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
	private static final String PANEL_BUNDLE = "viewDocuments";
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private PersonService personService;
	@Inject
	private DocumentService documentService;
	@Inject
	private JobService jobService;
	@Inject
	private SaveDocumentAction saveDocumentAction;

	private JTextField protocolTextField;
	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox recipientComboBox;
	private JCheckBox isDigitalCheckBox;
	private JTextField fileTextField;
	private JCheckBox outgoingCheckBox;
	private JXDatePicker releaseDateDatePicker;
	private JTextArea notesTextArea;
	private JButton fileChooserButton;

	private EventJXTableModel<Document> tableModel;
	private JLabel recipientLabel;
	private JLabel fileLabel;

	/**
	 * Pannello di visualizzazione di tutti i documenti presenti in database.
	 */
	public DocumentsPanel()
	{
		super();
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
		JLabel isDigitalLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "isDigital"));
		JLabel incomingLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "outgoing"));
		JLabel releaseDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));
		JLabel notesLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));
		JLabel requiredLabel =
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "requiredField"));

		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.add(protocolLabel, "gap para");
		headerPanel.add(getProtocolTextField(), "growx, wrap");
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "growx, wrap");
		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "growx, wrap");
		headerPanel.add(isDigitalLabel, "gap para");
		headerPanel.add(getIsDigitalCheckBox());
		headerPanel.add(getFileLabel(), "gap para");
		headerPanel.add(getFileTextField());
		headerPanel.add(getFileChooserButton(), "growx, wrap");
		headerPanel.add(releaseDateLabel, "gap para");
		headerPanel.add(getReleaseDateDatePicker(), "growx, wrap");
		headerPanel.add(incomingLabel, "gap para");
		headerPanel.add(getOutgoingCheckBox());
		headerPanel.add(getRecipientLabel(), "gap para");
		headerPanel.add(getRecipientComboBox(), "growx, wrap");
		headerPanel.add(notesLabel, "gap para");
		headerPanel.add(new JScrollPane(getNotesTextArea()), "span, growx, wrap");
		headerPanel.add(requiredLabel);

		return headerPanel;
	}

	/**
	 * Restituisce l'etichetta utilizzata per il destinatario e mittente.
	 * 
	 * @return l'etichetta utilizzata per il destinatario e mittente
	 */
	public JLabel getRecipientLabel()
	{
		if(recipientLabel == null)
		{
			recipientLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
		}
		return recipientLabel;
	}

	/**
	 * Restituisce l'etichetta del campo di scelta del file.
	 * 
	 * @return l'etichetta del campo di scelta del file
	 */
	public JLabel getFileLabel()
	{
		if(fileLabel == null)
		{
			fileLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "file"));
		}
		return fileLabel;
	}

	/**
	 * Restituisce il campo protocollo.
	 * 
	 * @return il campo protocollo
	 */
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
						  toggleSaveAction();
					  }
				  });
		}
		return protocolTextField;
	}

	/**
	 * Restituisce il campo descrizione.
	 * 
	 * @return il campo descrizione
	 */
	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
		{
			descriptionTextField = new JTextField(20);
			registerComponent(descriptionTextField);
			descriptionTextField.getDocument().addDocumentListener(
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
						  toggleSaveAction();
					  }
				  });
		}
		return descriptionTextField;
	}

	/**
	 * Restituisce il campo incarico.
	 * 
	 * @return il campo incarico
	 */
	public JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
		{
			jobComboBox = new JComboBox();
			registerComponent(jobComboBox);
			jobComboBox.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						toggleSaveAction();
					}
				});
		}
		return jobComboBox;
	}

	/**
	 * Restituisce il campo destinatario.
	 * 
	 * @return il campo destinatario
	 */
	public JComboBox getRecipientComboBox()
	{
		if(recipientComboBox == null)
		{
			recipientComboBox = new JComboBox();
			registerComponent(recipientComboBox);
		}
		return recipientComboBox;
	}

	/**
	 * Restituisce il flag che indica se un documento è digitale.
	 * 
	 * @return il flag che indica se un documento è digitale
	 */
	public JCheckBox getIsDigitalCheckBox()
	{
		if(isDigitalCheckBox == null)
		{
			isDigitalCheckBox = new JCheckBox();
			registerComponent(isDigitalCheckBox);
			isDigitalCheckBox.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						toggleFileSection();
					}
				});
		}
		return isDigitalCheckBox;
	}

	/**
	 * Restituisce il campo file.
	 * 
	 * @return il campo file
	 */
	public JTextField getFileTextField()
	{
		if(fileTextField == null)
		{
			fileTextField = new JTextField(25);
			registerComponent(fileTextField);
		}
		return fileTextField;
	}

	/**
	 * Restituisce il flag che indica se il documento è in uscita.
	 * 
	 * @return il flag che indica se il documento è in uscita
	 */
	public JCheckBox getOutgoingCheckBox()
	{
		if(outgoingCheckBox == null)
		{
			outgoingCheckBox = new JCheckBox();
			registerComponent(outgoingCheckBox);
			outgoingCheckBox.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(outgoingCheckBox.isSelected())
						{
							getRecipientLabel().setText(
							  getResourceMap().getString(LOCALIZATION_PREFIX + "recipient"));
						}
						else
						{
							getRecipientLabel().setText(
							  getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
						}
					}
				});
		}
		return outgoingCheckBox;
	}

	/**
	 * Restituisce il campo della data di rilascio del documento.
	 * 
	 * @return il campo della data di rilascio del documento
	 */
	public JXDatePicker getReleaseDateDatePicker()
	{
		if(releaseDateDatePicker == null)
		{
			releaseDateDatePicker = new JXDatePicker();
			registerComponent(releaseDateDatePicker);
		}
		return releaseDateDatePicker;
	}

	/**
	 * Restituisce il campo note.
	 * 
	 * @return il campo note
	 */
	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
		{
			notesTextArea = new JTextArea(5, 50);
			registerComponent(notesTextArea);
		}
		return notesTextArea;
	}

	/**
	 * Restituisce un pulsante per navigare nel file system. I file accettatati
	 * sono di tipo:
	 * <ul>
	 * <li>pdf</li>
	 * <li>png</li>
	 * <li>tif</li>
	 * <li>zip</li>
	 * <li>rar</li>
	 * <li>7z</li>
	 * <li>p7m</li>
	 * </ul>
	 * 
	 * @return un pulsante per navigare nel file system
	 */
	public JButton getFileChooserButton()
	{
		if(fileChooserButton == null)
		{
			fileChooserButton = new JButton(new AbstractAction()
				{

					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileHidingEnabled(true);
						fileChooser.setAcceptAllFileFilterUsed(false);
						fileChooser.setFileFilter(new FileNameExtensionFilter(
						  "pdf, png, tif, zip, rar, 7z, p7m", "pdf", "png", "tif", "zip",
						  "rar", "7z", "p7m"));

						int returnVal = fileChooser.showOpenDialog(null);
						if(returnVal == JFileChooser.APPROVE_OPTION)
						{
							File file = fileChooser.getSelectedFile();
							try
							{
								getFileTextField().setText(file.getCanonicalPath().toString());
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
						}
					}
				});
			fileChooserButton.setText(getResourceMap().getString(
			  LOCALIZATION_PREFIX + "openFile"));
		}
		return fileChooserButton;
	}

	@Override
	public void setup()
	{
		super.setup();
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddDocument());
		getContextualToolBar().add(actionProvider.getSaveDocument());

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
		    "uri", "isOutgoing", "releaseDate", "notes"};
		String[] columnsName =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.recipient"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isDigital"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.file"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.outgoing"),
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
							  getJobComboBox().getModel().setSelectedItem(
							    getSelectedItem().getJob());
							  getRecipientComboBox().getModel().setSelectedItem(
							    getSelectedItem().getRecipient());
							  getIsDigitalCheckBox().setSelected(
							    getSelectedItem().getIsDigital());
							  getFileTextField().setText(getSelectedItem().getUri());
							  getOutgoingCheckBox().setSelected(
							    getSelectedItem().getIsOutgoing());
							  getReleaseDateDatePicker().setDate(
							    getSelectedItem().getReleaseDate() != null? getSelectedItem()
							      .getReleaseDate().toDate(): null);
							  getNotesTextArea().setText(getSelectedItem().getNotes());

							  /* calcolo visibilità della sezione del file */
							  toggleFileSection();
						  }
					  }
				  }
			  });
		return documentTable;
	}

	@Override
	public String getBundleName()
	{
		return PANEL_BUNDLE;
	}

	/**
	 * Issue 66: Controlla che sia possibile abilitare l'azione di salvataggio di
	 * un documento:
	 * <ul>
	 * <li>Deve essere assegnato un protocollo</li>
	 * <li>Deve essere assegnato un incarico</li>
	 * <li>Deve essere assegnato una descrizione</li>
	 * </ul>
	 */
	private void toggleSaveAction()
	{
		saveDocumentAction.setSaveDocumentActionEnabled(GreenToneUtilities
		  .getText(getDescriptionTextField()) != null
		  && getJobComboBox().getSelectedItem() != null
		  && GreenToneUtilities.getText(getProtocolTextField()) != null);
	}

	@Override
	public void clearForm()
	{
		super.clearForm();
		/* Issue 69: di default impostare la data odierna */
		getReleaseDateDatePicker().setDate(new DateTime().toDate());
		/* Issue 70: di default l'etichetta è impostata a destinatario */
		getRecipientLabel().setText(
		  getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
		/* Issue 77: di default non viene mostrata la scelta del file */
		toggleFileSection();
	}

	/**
	 * Mostra / nasconde la parte relativa alla scelta del file del documento.
	 */
	private void toggleFileSection()
	{
		getFileLabel().setVisible(getIsDigitalCheckBox().isSelected());
		getFileTextField().setVisible(getIsDigitalCheckBox().isSelected());
		getFileChooserButton().setVisible(getIsDigitalCheckBox().isSelected());
	}
}
