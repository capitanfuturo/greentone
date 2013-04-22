package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.DeleteDocumentAction;
import it.greentone.gui.action.SaveDocumentAction;
import it.greentone.gui.action.ViewReportsAction;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Comparator;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.DefaultEventComboBoxModel;
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
 * </code> <br>
 * <br>
 * Pannello di visualizzazione di tutti i documenti presenti in database.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class DocumentsPanel extends ContextualPanel<Document> {
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
    private DeleteDocumentAction deleteDocumentAction;
    @Inject
    private SaveDocumentAction saveDocumentAction;
    @Inject
    private GreenToneUtilities utilities;
    @Inject
    private ViewReportsAction viewReportsAction;

    private JTextField protocolTextField;
    private JTextField descriptionTextField;
    private JComboBox jobComboBox;
    private JComboBox recipientComboBox;
    private JCheckBox isDigitalCheckBox;
    private JEditorPane filePathField;
    private JCheckBox outgoingCheckBox;
    private JXDatePicker releaseDateDatePicker;
    private JTextArea notesTextArea;
    private JButton fileChooserButton;

    private EventJXTableModel<Document> tableModel;
    private JLabel recipientLabel;
    private JLabel fileLabel;
    private File file;
    private final String[] properties;
    private final String[] columnsNames;
    private final boolean[] writables;

    /**
     * Pannello di visualizzazione di tutti i documenti presenti in database.
     */
    public DocumentsPanel() {
        super();

        properties =
                new String[] { "protocol", "description", "job", "recipient", "isDigital", "uri", "isOutgoing",
                        "releaseDate", "notes" };
        columnsNames =
                new String[] { getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.job"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.recipient"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isDigital"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.file"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.outgoing"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.notes") };
        writables = new boolean[] { false, false, false, false, false, false, false, false, false };
    }

    @Override
    protected JPanel createHeaderPanel() {
        JLabel titleLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "title"));
        titleLabel.setFont(FontProvider.TITLE_SMALL);
        titleLabel.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "titleIcon"));
        JLabel protocolLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
        JLabel descriptionLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "description"));
        JLabel jobLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "job"));
        JLabel isDigitalLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "isDigital"));
        JLabel incomingLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "outgoing"));
        JLabel releaseDateLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));
        JLabel notesLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));
        JLabel requiredLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "requiredField"));

        JPanel headerPanel = new JPanel(new MigLayout("", "[][10%][][20%][]"));

        headerPanel.add(titleLabel, "wrap");

        headerPanel.add(protocolLabel, "gap para");
        headerPanel.add(getProtocolTextField(), "growx, wrap");

        headerPanel.add(descriptionLabel, "gap para");
        headerPanel.add(getDescriptionTextField(), "span 2, growx, wrap");

        headerPanel.add(jobLabel, "gap para");
        headerPanel.add(getJobComboBox(), "growx, wrap");

        headerPanel.add(isDigitalLabel, "gap para");
        headerPanel.add(getIsDigitalCheckBox());
        headerPanel.add(getFileLabel(), "gap para");
        headerPanel.add(getFilePathField(), "growx");
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
    public JLabel getRecipientLabel() {
        if (recipientLabel == null) {
            recipientLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
        }
        return recipientLabel;
    }

    /**
     * Restituisce l'etichetta del campo di scelta del file.
     * 
     * @return l'etichetta del campo di scelta del file
     */
    public JLabel getFileLabel() {
        if (fileLabel == null) {
            fileLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "file"));
        }
        return fileLabel;
    }

    /**
     * Restituisce il campo protocollo.
     * 
     * @return il campo protocollo
     */
    public JTextField getProtocolTextField() {
        if (protocolTextField == null) {
            protocolTextField = new JTextField();
            registerComponent(protocolTextField);
            protocolTextField.setEnabled(false);

            protocolTextField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    toogleAction();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    toogleAction();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    toogleAction();
                }

                private void toogleAction() {
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
    public JTextField getDescriptionTextField() {
        if (descriptionTextField == null) {
            descriptionTextField = new JTextField();
            registerComponent(descriptionTextField);
            descriptionTextField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void removeUpdate(DocumentEvent e) {
                    toogleAction();
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    toogleAction();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    toogleAction();
                }

                private void toogleAction() {
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
    public JComboBox getJobComboBox() {
        if (jobComboBox == null) {
            jobComboBox = new JComboBox();
            registerComponent(jobComboBox);
            jobComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
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
    public JComboBox getRecipientComboBox() {
        if (recipientComboBox == null) {
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
    public JCheckBox getIsDigitalCheckBox() {
        if (isDigitalCheckBox == null) {
            isDigitalCheckBox = new JCheckBox();
            registerComponent(isDigitalCheckBox);
            isDigitalCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
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
    public JEditorPane getFilePathField() {
        if (filePathField == null) {
            filePathField = new JEditorPane();
            filePathField.setForeground(Color.blue);
            registerComponent(filePathField);
        }
        return filePathField;
    }

    /**
     * Restituisce il flag che indica se il documento è in uscita.
     * 
     * @return il flag che indica se il documento è in uscita
     */
    public JCheckBox getOutgoingCheckBox() {
        if (outgoingCheckBox == null) {
            outgoingCheckBox = new JCheckBox();
            registerComponent(outgoingCheckBox);
            outgoingCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (outgoingCheckBox.isSelected()) {
                        getRecipientLabel().setText(getResourceMap().getString(LOCALIZATION_PREFIX + "recipient"));
                    } else {
                        getRecipientLabel().setText(getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
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
    public JXDatePicker getReleaseDateDatePicker() {
        if (releaseDateDatePicker == null) {
            releaseDateDatePicker = GreenToneUtilities.createJXDataPicker();
            registerComponent(releaseDateDatePicker);
        }
        return releaseDateDatePicker;
    }

    /**
     * Restituisce il campo note.
     * 
     * @return il campo note
     */
    public JTextArea getNotesTextArea() {
        if (notesTextArea == null) {
            notesTextArea = new JTextArea(5, 50);
            registerComponent(notesTextArea);
        }
        return notesTextArea;
    }

    /**
     * Restituisce un pulsante per navigare nel file system. I file accettatati sono di tipo:
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
    public JButton getFileChooserButton() {
        if (fileChooserButton == null) {
            fileChooserButton = new JButton(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileHidingEnabled(true);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    fileChooser.setFileFilter(new FileNameExtensionFilter("pdf, png, tif, zip, rar, 7z, p7m", "pdf",
                            "png", "tif", "zip", "rar", "7z", "p7m"));

                    int returnVal = fileChooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        final File file = fileChooser.getSelectedFile();
                        getFilePathField().setText(file.getName());
                        setFile(file);
                    }
                }
            });
            fileChooserButton.setText(getResourceMap().getString(LOCALIZATION_PREFIX + "openFile"));
        }
        return fileChooserButton;
    }

    @Override
    public String getBundleName() {
        return PANEL_BUNDLE;
    }

    /**
     * Issue 66: Controlla che sia possibile abilitare l'azione di salvataggio di un documento:
     * <ul>
     * <li>Deve essere assegnato un protocollo</li>
     * <li>Deve essere assegnato un incarico</li>
     * <li>Deve essere assegnato una descrizione</li>
     * </ul>
     */
    private void toggleSaveAction() {
        saveDocumentAction.setSaveDocumentActionEnabled(GreenToneUtilities.getText(getDescriptionTextField()) != null
                && getJobComboBox().getSelectedItem() != null
                && GreenToneUtilities.getText(getProtocolTextField()) != null);
    }

    @Override
    protected void clearForm() {
        super.clearForm();
        /* Issue 69: di default impostare la data odierna */
        getReleaseDateDatePicker().setDate(new DateTime().toDate());
        /* Issue 70: di default l'etichetta è impostata a destinatario */
        getRecipientLabel().setText(getResourceMap().getString(LOCALIZATION_PREFIX + "sender"));
        /* Issue 77: di default non viene mostrata la scelta del file */
        toggleFileSection();

        file = null;
    }

    /**
     * Mostra / nasconde la parte relativa alla scelta del file del documento.
     */
    private void toggleFileSection() {
        getFileLabel().setVisible(getIsDigitalCheckBox().isSelected());
        getFilePathField().setVisible(getIsDigitalCheckBox().isSelected());
        getFileChooserButton().setVisible(getIsDigitalCheckBox().isSelected());
    }

    private void setFile(final File file) {
        for (MouseListener listener : getFilePathField().getMouseListeners()) {
            getFilePathField().removeMouseListener(listener);
        }
        getFilePathField().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        utilities.open(file);
                        return null;
                    }
                }.execute();
            };
        });
        this.file = file;
    }

    /**
     * Restituisce il file del documento.
     * 
     * @return il file del documento
     */
    public File getFile() {
        return file;
    }

    @Override
    public Document getItemFromTableRow(int rowIndex) {
        return documentService.getAllDocuments().get(rowIndex);
    }

    @Override
    public void initializeToolBar() {
        getContextualToolBar().add(actionProvider.getAddDocument());
        getContextualToolBar().add(actionProvider.getSaveDocument());
        getContextualToolBar().add(actionProvider.getDeleteDocument());
        getContextualToolBar().addSeparator();
        getContextualToolBar().add(actionProvider.getViewReports());
        getContextualToolBar().addSeparator();
        getContextualToolBar().add(actionProvider.getViewHelp());
    }

    @Override
    public void populateModel() {
        tableModel =
                new EventJXTableModel<Document>(documentService.getAllDocuments(), new BeanTableFormat<Document>(
                        Document.class, properties, columnsNames, writables));
        getContentTable().setModel(tableModel);
        getContentTable().setSortOrder(0, SortOrder.DESCENDING);

        /* carico destinatari */
        EventList<Person> allPersons = personService.getAllPersons();
        SortedList<Person> sortedAllPersons = new SortedList<Person>(allPersons, new Comparator<Person>() {

            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        DefaultEventComboBoxModel<Person> recipientComboBoxModel =
                new DefaultEventComboBoxModel<Person>(sortedAllPersons);
        getRecipientComboBox().setModel(recipientComboBoxModel);

        /* carico gli incarichi */
        SortedList<Job> jobsList = new SortedList<Job>(jobService.getAllJobs(), new Comparator<Job>() {

            @Override
            public int compare(Job o1, Job o2) {
                return o2.getProtocol().compareToIgnoreCase(o1.getProtocol());
            }
        });
        DefaultEventComboBoxModel<Job> jobComboBoxModel = new DefaultEventComboBoxModel<Job>(jobsList);
        getJobComboBox().setModel(jobComboBoxModel);
    }

    @Override
    public void initializeForEditing() {
        super.initializeForEditing();
        getProtocolTextField().setEnabled(false);
        /* aggiorno il pannello */
        getProtocolTextField().setText(getSelectedItem().getProtocol());
        getDescriptionTextField().setText(getSelectedItem().getDescription());
        getJobComboBox().getModel().setSelectedItem(getSelectedItem().getJob());
        getRecipientComboBox().getModel().setSelectedItem(getSelectedItem().getRecipient());
        getIsDigitalCheckBox().setSelected(getSelectedItem().getIsDigital());

        String URI = getSelectedItem().getUri();
        if (URI != null) {
            final File file = new File(URI);
            getFilePathField().setText(file.getName());
            setFile(file);
        } else {
            getFilePathField().setText(null);
        }

        getOutgoingCheckBox().setSelected(getSelectedItem().getIsOutgoing());
        getReleaseDateDatePicker().setDate(
                getSelectedItem().getReleaseDate() != null ? getSelectedItem().getReleaseDate().toDate() : null);
        getNotesTextArea().setText(getSelectedItem().getNotes());

        /* calcolo visibilità della sezione del file */
        toggleFileSection();
    }

    @Override
    public void initializeForInsertion() {
        super.initializeForInsertion();
        getProtocolTextField().setEnabled(false);
        /* calcolo il protocollo da impostare */
        getProtocolTextField().setText(documentService.getNextProtocol());
    }

    @Override
    protected void tableSelectionHook() {
        super.tableSelectionHook();
        deleteDocumentAction.setDeleteDocumentActionEnabled(true);
    }

    @Override
    protected void tableSelectionLostHook() {
        super.tableSelectionLostHook();
        deleteDocumentAction.setDeleteDocumentActionEnabled(false);
    }
}
