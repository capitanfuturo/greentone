package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.JXSplitPane;
import it.greentone.gui.MainPanel;
import it.greentone.gui.ModelEventManager;
import it.greentone.gui.action.ContextualAction;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.impl.beans.BeanTableFormat;
import ca.odell.glazedlists.swing.EventJXTableModel;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2012 GreenTone Developer Team.<br>
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
 * Pannello di riepilogo di un incarico. Mostra i dati di testata, le operazioni e i documenti coninvolti nell'incarico
 * oggetto del pannello.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class JobPanel extends AbstractPanel {
    @Inject
    private DocumentService documentService;
    @Inject
    private OperationService operationService;
    @Inject
    private JobsPanel jobsPanel;
    @Inject
    private MainPanel gtMainPanel;
    @Inject
    private GreenToneUtilities utilities;

    private static final String LOCALIZATION_PREFIX = "viewJob.Panel.";
    private Job job;
    private Collection<Operation> operations;
    private Collection<Document> documents;
    private JToolBar toolBar;
    private JPanel headerPanel;
    private JLabel protocolLabel;
    private JLabel customerLabel;
    private JLabel managerLabel;
    private JLabel cityLabel;
    private JLabel descriptionLabel;
    private JLabel dueDateLabel;
    private JLabel startDateLabel;
    private JLabel finishDateLabel;
    private JLabel categoryLabel;
    private JLabel statusLabel;
    private JTextArea notesTextArea;
    private JXTable operationsTable;
    private JXTable documentsTable;

    private final String[] operationsProperties;
    private final String[] operationsColumnsNames;
    private final boolean[] operationsWritables;

    private final String[] documentsProperties;
    private final String[] documentsColumnsNames;
    private final boolean[] documentsWritables;
    private JXSplitPane mainSplitPane;
    private JPanel contentPanel;
    private static final String GLOBAL_PANEL = "GLOBAL_PANEL";
    private static final String OPERATION_PANEL = "OPERATION_PANEL";
    private static final String DOCUMENT_PANEL = "DOCUMENT_PANEL";
    private JButton backDetailButton;
    private JButton documentDetailButton;
    private JButton operationDetailButton;
    private EventJXTableModel<Document> documentsTableModel;
    private EventJXTableModel<Operation> operationsTableModel;

    /**
     * Pannello di riepilogo di un incarico. Mostra i dati di testata, le operazioni e i documenti coninvolti
     * nell'incarico oggetto del pannello.
     * 
     * @param modelEventManager
     *            manager degli eventi greentone
     */
    @Inject
    public JobPanel(ModelEventManager modelEventManager) {
        operationsProperties = new String[] { "description", "operationDate", "amount" };
        operationsColumnsNames =
                new String[] { getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.amount") };
        operationsWritables = new boolean[] { false, false, false };

        documentsProperties = new String[] { "isDigital", "protocol", "description", "releaseDate" };
        documentsColumnsNames =
                new String[] { getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isDigital"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
                        getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date") };
        documentsWritables = new boolean[] { false, false, false, false };

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(getToolBar(), BorderLayout.NORTH);
        northPanel.add(getHeaderPanel(), BorderLayout.CENTER);

        JPanel operationsHeaderPanel = new JPanel(new MigLayout());
        JLabel operationsLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "operations"));
        operationsLabel.setFont(FontProvider.TITLE_SMALL);
        operationsLabel.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "operationIcon"));
        operationsHeaderPanel.add(operationsLabel);
        operationsHeaderPanel.add(getOperationDetailButton());

        JPanel operationsPanel = new JPanel(new BorderLayout());
        operationsPanel.add(operationsHeaderPanel, BorderLayout.NORTH);
        operationsPanel.add(new JScrollPane(getOperationsTable()), BorderLayout.CENTER);

        JPanel docHeaderPanel = new JPanel(new MigLayout());
        JLabel documentsLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "documents"));
        documentsLabel.setFont(FontProvider.TITLE_SMALL);
        documentsLabel.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "documentIcon"));
        docHeaderPanel.add(documentsLabel);
        docHeaderPanel.add(getDocumentDetailButton());

        JPanel documentsPanel = new JPanel(new BorderLayout());
        documentsPanel.add(docHeaderPanel, BorderLayout.NORTH);
        documentsPanel.add(new JScrollPane(getDocumentsTable()), BorderLayout.CENTER);

        getMainSplitPane().add(operationsPanel);
        getMainSplitPane().add(documentsPanel);
        getContentPanel().add(getMainSplitPane(), GLOBAL_PANEL);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(getContentPanel(), BorderLayout.CENTER);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        add(mainPanel);

        /* aggiornamento dinamico */
        modelEventManager.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (job != null) {
                    if (name.equals(ModelEventManager.OPERATION_INSERTED)
                            || name.equals(ModelEventManager.OPERATION_MODIFIED)
                            || name.equals(ModelEventManager.OPERATION_DELETED)) {
                        Operation operation = (Operation) evt.getNewValue();
                        if (operation.getJob().getId() == job.getId()) {
                            loadOperations();
                        }
                    } else if (name.equals(ModelEventManager.DOCUMENT_INSERTED)
                            || name.equals(ModelEventManager.DOCUMENT_MODIFIED)
                            || name.equals(ModelEventManager.DOCUMENT_DELETED)) {
                        Document document = (Document) evt.getNewValue();
                        if (document.getJob().getId() == job.getId()) {
                            loadDocuments();
                        }
                    } else if (name.equals(ModelEventManager.JOB_MODIFIED)) {
                        Job j = (Job) evt.getNewValue();
                        if (j.getId() == job.getId()) {
                            loadHeader();
                        }
                    }
                }
            }
        });
    }

    /**
     * Restituisce la toolbar.
     * 
     * @return la toolbar
     */
    public JToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new JToolBar();
            toolBar.setFloatable(false);
        }
        return toolBar;
    }

    /**
     * Restituisce il pannello di testata.
     * 
     * @return il pannello di testata
     */
    public JPanel getHeaderPanel() {
        if (headerPanel == null) {
            JLabel titleLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "title"));
            titleLabel.setFont(FontProvider.TITLE_SMALL);
            titleLabel.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "jobIcon"));
            JLabel protocolLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
            JLabel dueDateLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "dueDate"));
            JLabel startDateLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "startDate"));
            JLabel finishDateLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "finishDate"));
            JLabel categoryLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "category"));
            JLabel statusLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "status"));
            JLabel descriptionLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "description"));
            JLabel managerLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "manager"));
            JLabel customerLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "customer"));
            JLabel notesLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));
            JLabel cityLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "city"));

            headerPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%]"));

            headerPanel.add(titleLabel, "wrap");

            headerPanel.add(protocolLabel, "gap para");
            headerPanel.add(getProtocolLabel(), "growx, wrap");

            headerPanel.add(customerLabel, "gap para");
            headerPanel.add(getCustomerLabel(), "growx");
            headerPanel.add(managerLabel, "gap para");
            headerPanel.add(getManagerLabel(), "growx, wrap");

            headerPanel.add(cityLabel, "gap para");
            headerPanel.add(getCityLabel(), "growx,wrap");

            headerPanel.add(descriptionLabel, "gap para");
            headerPanel.add(getDescriptionLabel(), "span, growx, wrap");

            headerPanel.add(dueDateLabel, "gap para");
            headerPanel.add(getDueDateLabel(), "growx");
            headerPanel.add(startDateLabel, "gap para");
            headerPanel.add(getStartDateLabel(), "growx");
            headerPanel.add(finishDateLabel, "gap para");
            headerPanel.add(getFinishDateLabel(), "growx, wrap");

            headerPanel.add(categoryLabel, "gap para");
            headerPanel.add(getCategoryLabel(), "growx");
            headerPanel.add(statusLabel, "gap para");
            headerPanel.add(getStatusLabel(), "growx, wrap");

            headerPanel.add(notesLabel, "gap para");
            headerPanel.add(new JScrollPane(getNotesTextArea()), "span, growx, wrap");
        }
        return headerPanel;
    }

    /**
     * Restituisce il campo del protocollo.
     * 
     * @return il campo del protocollo
     */
    public JLabel getProtocolLabel() {
        if (protocolLabel == null) {
            protocolLabel = new JLabel();
            protocolLabel.setFont(FontProvider.TITLE_SMALL);
        }
        return protocolLabel;
    }

    /**
     * Restituisce il campo del cliente.
     * 
     * @return il campo del cliente
     */
    public JLabel getCustomerLabel() {
        if (customerLabel == null) {
            customerLabel = new JLabel();
        }
        return customerLabel;
    }

    /**
     * Restituisce il campo del responsabile.
     * 
     * @return il campo del responsabile
     */
    public JLabel getManagerLabel() {
        if (managerLabel == null) {
            managerLabel = new JLabel();
        }
        return managerLabel;
    }

    /**
     * Restituisce il campo della città.
     * 
     * @return il campo della città
     */
    public JLabel getCityLabel() {
        if (cityLabel == null) {
            cityLabel = new JLabel();
        }
        return cityLabel;
    }

    /**
     * Restituisce il campo della descrizione.
     * 
     * @return il campo della descrizione
     */
    public JLabel getDescriptionLabel() {
        if (descriptionLabel == null) {
            descriptionLabel = new JLabel();
        }
        return descriptionLabel;
    }

    /**
     * Restituisce il campo della scadenza.
     * 
     * @return il campo della scadenza
     */
    public JLabel getDueDateLabel() {
        if (dueDateLabel == null) {
            dueDateLabel = new JLabel();
        }
        return dueDateLabel;
    }

    /**
     * Restituisce il campo della data di inizio.
     * 
     * @return il campo della data di inizio
     */
    public JLabel getStartDateLabel() {
        if (startDateLabel == null) {
            startDateLabel = new JLabel();
        }
        return startDateLabel;
    }

    /**
     * Restituisce il campo della data di fine.
     * 
     * @return il campo della data di fine
     */
    public JLabel getFinishDateLabel() {
        if (finishDateLabel == null) {
            finishDateLabel = new JLabel();
        }
        return finishDateLabel;
    }

    /**
     * Restituisce il campo della categoria.
     * 
     * @return il campo della categoria
     */
    public JLabel getCategoryLabel() {
        if (categoryLabel == null) {
            categoryLabel = new JLabel();
        }
        return categoryLabel;
    }

    /**
     * Restituisce il campo dello stato.
     * 
     * @return il campo dello stato
     */
    public JLabel getStatusLabel() {
        if (statusLabel == null) {
            statusLabel = new JLabel();
        }
        return statusLabel;
    }

    /**
     * Restituisce il campo delle note.
     * 
     * @return il campo delle note
     */
    public JTextArea getNotesTextArea() {
        if (notesTextArea == null) {
            notesTextArea = new JTextArea();
            notesTextArea.setEditable(false);
        }
        return notesTextArea;
    }

    /**
     * Restituisce il pannello sottostante che raccoglie i contenuti dell'incarico.
     * 
     * @return il pannello sottostante che raccoglie i contenuti dell'incarico
     */
    public JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new CardLayout());
        }
        return contentPanel;
    }

    /**
     * Restituisce lo splitpane delle operazioni / documenti.
     * 
     * @return lo splitpane delle operazioni / documenti
     */
    public JXSplitPane getMainSplitPane() {
        if (mainSplitPane == null) {
            mainSplitPane = new JXSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        }
        return mainSplitPane;
    }

    /**
     * Restituisce la tabella delle operazioni.
     * 
     * @return la tabella delle operazioni
     */
    public JXTable getOperationsTable() {
        if (operationsTable == null) {
            operationsTable = GreenToneUtilities.createJXTable();
        }
        return operationsTable;
    }

    /**
     * Restituisce la tabella dei documenti.
     * 
     * @return la tabella dei documenti
     */
    public JXTable getDocumentsTable() {
        if (documentsTable == null) {
            documentsTable = GreenToneUtilities.createJXTable();
            documentsTable.setDefaultRenderer(Boolean.class, new TableCellRenderer() {

                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    if (value instanceof Boolean) {
                        Boolean v = (Boolean) value;
                        if (v) {
                            return new JLabel(getResourceMap()
                                    .getImageIcon(LOCALIZATION_PREFIX + "Table.isDigitalIcon"));
                        } else {
                            return new JLabel("");
                        }
                    }
                    return null;
                }
            });

        }
        return documentsTable;
    }

    /**
     * Restituisce il pulsante che permette di passare dalla vista a split pane al dettaglio del documento selezionato.
     * 
     * @return il pulsante che permette di passare dalla vista a split pane al dettaglio del documento selezionato
     */
    public JButton getDocumentDetailButton() {
        if (documentDetailButton == null) {
            documentDetailButton = new JButton(getResourceMap().getIcon("viewJob.Action.smallIcon"));
            /* abilitazione del tasto */
            documentDetailButton.setEnabled(false);
            getDocumentsTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRow = getDocumentsTable().getSelectedRow();
                    documentDetailButton.setEnabled(selectedRow > -1);
                }
            });
            /* azione del pulsante */
            documentDetailButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = getDocumentsTable().getSelectedRow();
                    if (selectedRow > -1) {
                        int rowIndexToModel =
                                getDocumentsTable().convertRowIndexToModel(getDocumentsTable().getSelectedRow());
                        JPanel documentPanel = createDocumentPanel(documentsTableModel.getElementAt(rowIndexToModel));
                        getContentPanel().add(documentPanel, DOCUMENT_PANEL);

                        CardLayout cl = (CardLayout) (getContentPanel().getLayout());
                        cl.show(getContentPanel(), DOCUMENT_PANEL);
                    }
                }
            });
        }
        return documentDetailButton;
    }

    /**
     * Restituisce il pulsante che permette di passare dalla vista a split pane al dettaglio dell'operazione
     * selezionata.
     * 
     * @return il pulsante che permette di passare dalla vista a split pane al dettaglio dell'operazione selezionata
     */
    public JButton getOperationDetailButton() {
        if (operationDetailButton == null) {
            operationDetailButton = new JButton(getResourceMap().getIcon("viewJob.Action.smallIcon"));
            /* abilitazione del tasto */
            operationDetailButton.setEnabled(false);
            getOperationsTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRow = getOperationsTable().getSelectedRow();
                    operationDetailButton.setEnabled(selectedRow > -1);
                }
            });
            /* azione del pulsante */
            operationDetailButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = getOperationsTable().getSelectedRow();
                    if (selectedRow > -1) {
                        int rowIndexToModel =
                                getOperationsTable().convertRowIndexToModel(getOperationsTable().getSelectedRow());
                        JPanel operationPanel =
                                createOperationPanel(operationsTableModel.getElementAt(rowIndexToModel));
                        getContentPanel().add(operationPanel, OPERATION_PANEL);

                        CardLayout cl = (CardLayout) (getContentPanel().getLayout());
                        cl.show(getContentPanel(), OPERATION_PANEL);
                    }
                }
            });
        }
        return operationDetailButton;
    }

    /**
     * Restituisce il pulsante per tornare alla vista con split pane della scheda.
     * 
     * @return il pulsante per tornare alla vista con split pane della scheda
     */
    public JButton getBackDetailButton() {
        if (backDetailButton == null) {
            backDetailButton = new JButton(getResourceMap().getIcon(LOCALIZATION_PREFIX + "zoomOutIcon"));
            backDetailButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (getContentPanel().getLayout());
                    cl.show(getContentPanel(), GLOBAL_PANEL);
                }
            });
        }
        return backDetailButton;
    }

    @Override
    public String getBundleName() {
        return "viewJob";
    }

    /**
     * Prima di invocare questo metodo, impostare l'oggetto del pannello con il metodo {@link #setJob(Job)}.
     */
    @Override
    public void setup() {
        super.setup();

        /* toolbar */
        getToolBar().removeAll();
        JButton goButton = new JButton(getResourceMap().getIcon("viewJob.Action.goToJobs"));
        goButton.setToolTipText(getResourceMap().getString("viewJob.Panel.goto"));
        goButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jobsPanel.setup();
                ContextualAction.addTab(gtMainPanel, jobsPanel);
                jobsPanel.setSelectedJob(job);
            }
        });
        getToolBar().add(goButton);

        /* informazioni di testata */
        loadHeader();

        /* informazioni su operazioni */
        loadOperations();
        getOperationsTable().setSortOrder(1, SortOrder.DESCENDING);

        /* informazioni su documenti */
        loadDocuments();
        getDocumentsTable().setSortOrder(0, SortOrder.DESCENDING);

        /* aggiorno la posizione dello splitpane */
        mainSplitPane.setDividerLocation(0.6);
    }

    /**
     * Imposta l'incarico di cui mostrare il dettaglio delle operazioni e dei documenti.
     * 
     * @param job
     *            l'incarico di cui mostrare il dettaglio delle operazioni e dei documenti
     */
    public void setJob(Job job) {
        this.job = job;
    }

    protected JPanel createDocumentPanel(Document document) {
        JPanel documentDetailPanel = new JPanel(new MigLayout("", "[][10%][][20%][]"));

        JLabel title = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "documentDetail"));
        title.setFont(FontProvider.TITLE_SMALL);
        title.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "documentIcon"));
        documentDetailPanel.add(title);
        documentDetailPanel.add(getBackDetailButton(), "wrap");
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.protocol")), "gap para");
        documentDetailPanel.add(new JLabel(document.getProtocol()), "growx, wrap");
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.description")), "gap para");
        documentDetailPanel.add(new JLabel(document.getDescription()), "span 2, growx, wrap");
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.isDigital")), "gap para");
        if (document.getIsDigital()) {
            documentDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "yes")));
            documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.file")), "gap para");
            JEditorPane filePathField = new JEditorPane();
            filePathField.setForeground(Color.blue);
            final File file = new File(document.getUri());
            filePathField.setText(file.getName());
            filePathField.addMouseListener(new MouseAdapter() {
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

            documentDetailPanel.add(filePathField, "growx");
        } else {
            documentDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "no")));
        }
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.date")), "gap para");
        documentDetailPanel
                .add(new JLabel(GreenToneUtilities.formatDateTime(document.getReleaseDate())), "growx, wrap");
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.outgoing")), "gap para");
        if (document.getIsOutgoing()) {
            documentDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "yes")));
            documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.recipient")));
        } else {
            documentDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "no")));
            documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.sender")));
        }
        documentDetailPanel.add(new JLabel(document.getRecipient() != null ? document.getRecipient().toString() : ""),
                "growx, wrap");
        documentDetailPanel.add(new JLabel(getResourceMap().getString("viewDocuments.Panel.notes")), "gap para");
        JTextArea ta = new JTextArea(5, 50);
        ta.setText(document.getNotes());
        documentDetailPanel.add(new JScrollPane(ta), "span, growx, wrap");

        return documentDetailPanel;
    }

    protected JPanel createOperationPanel(Operation operation) {
        JPanel operationDetailPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%]"));

        JLabel title = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "operationDetail"));
        title.setFont(FontProvider.TITLE_SMALL);
        title.setIcon(getResourceMap().getIcon(LOCALIZATION_PREFIX + "operationIcon"));
        operationDetailPanel.add(title);
        operationDetailPanel.add(getBackDetailButton(), "wrap");
        operationDetailPanel
                .add(new JLabel(getResourceMap().getString("viewOperations.Panel.description")), "gap para");
        operationDetailPanel.add(new JLabel(operation.getDescription()), "span 3, growx, wrap");
        operationDetailPanel.add(new JLabel(getResourceMap().getString("viewOperations.Panel.type")), "gap para");
        operationDetailPanel.add(new JLabel(operation.getOperationType().getLocalizedName()), "growx, wrap");
        operationDetailPanel
                .add(new JLabel(getResourceMap().getString("viewOperations.Panel.isVacazione")), "gap para");
        if (operation.getIsVacazione()) {
            operationDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "yes")), "growx");
            if (operation.getIsProfessionalVacazione()) {
                operationDetailPanel.add(
                        new JLabel(getResourceMap().getString("viewOperations.Panel.isProfessionalVacazione")),
                        "gap para");
                operationDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "yes")),
                        "growx, wrap");
            }
        } else {
            operationDetailPanel.add(new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "no")), "growx");
        }
        operationDetailPanel.add(new JLabel(getResourceMap().getString("viewOperations.Panel.date")), "gap para");
        operationDetailPanel.add(new JLabel(GreenToneUtilities.formatDateTime(operation.getOperationDate())), "growx");
        if (operation.getIsVacazione()) {
            operationDetailPanel.add(new JLabel(getResourceMap().getString("viewOperations.Panel.vacazioni")), "growx");
            operationDetailPanel.add(new JLabel(operation.getNumVacazioni() + ""), "gap para");
        }
        operationDetailPanel.add(new JLabel(getResourceMap().getString("viewOperations.Panel.amount")), "gap para");
        Double amount = operation.getAmount();
        operationDetailPanel.add(new JLabel(amount != null ? amount.toString() : ""), "growx");

        return operationDetailPanel;
    }

    private void loadOperations() {
        operations = operationService.getOperationsJob(job);
        EventList<Operation> operationsEventList = new BasicEventList<Operation>();
        operationsEventList.addAll(operations);
        operationsTableModel =
                new EventJXTableModel<Operation>(operationsEventList, new BeanTableFormat<Operation>(Operation.class,
                        operationsProperties, operationsColumnsNames, operationsWritables));
        getOperationsTable().setModel(operationsTableModel);
        /* imposto la dimensione delle colonne più importanti */
        getOperationsTable().getColumnModel().getColumn(0).setPreferredWidth(500);
    }

    private void loadDocuments() {
        documents = documentService.getDocumentsJob(job);
        EventList<Document> documentsEventList = new BasicEventList<Document>();
        documentsEventList.addAll(documents);
        documentsTableModel =
                new EventJXTableModel<Document>(documentsEventList, new BeanTableFormat<Document>(Document.class,
                        documentsProperties, documentsColumnsNames, documentsWritables));
        getDocumentsTable().setModel(documentsTableModel);
        /* imposto la dimensione delle colonne più importanti */
        getDocumentsTable().getColumnModel().getColumn(0).setPreferredWidth(17);
        getDocumentsTable().getColumnModel().getColumn(2).setPreferredWidth(200);
    }

    private void loadHeader() {
        getProtocolLabel().setText(job.getProtocol());
        getCustomerLabel().setText(job.getCustomer() != null ? job.getCustomer().toString() : null);
        getManagerLabel().setText(job.getManager() != null ? job.getManager().toString() : null);
        getCityLabel().setText(job.getCity());
        getDescriptionLabel().setText(job.getDescription());
        getDueDateLabel().setText(GreenToneUtilities.formatDateTime(job.getDueDate()));
        getStartDateLabel().setText(GreenToneUtilities.formatDateTime(job.getStartDate()));
        getFinishDateLabel().setText(GreenToneUtilities.formatDateTime(job.getFinishDate()));
        getCategoryLabel().setText(job.getCategory() != null ? job.getCategory().toString() : null);
        getStatusLabel().setText(job.getStatus() != null ? job.getStatus().getLocalizedName() : null);
        getNotesTextArea().setText(job.getNotes());
    }
}
