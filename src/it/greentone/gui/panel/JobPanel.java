package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.MainPanel;
import it.greentone.gui.action.ContextualAction;
import it.greentone.persistence.Document;
import it.greentone.persistence.DocumentService;
import it.greentone.persistence.Job;
import it.greentone.persistence.Operation;
import it.greentone.persistence.OperationService;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SortOrder;

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
 * </code>
 * <br>
 * <br>
 * Pannello di riepilogo di un incarico. Mostra i dati di testata, le operazioni
 * e i documenti coninvolti nell'incarico oggetto del pannello.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class JobPanel extends AbstractPanel
{
	@Inject
	private DocumentService documentService;
	@Inject
	private OperationService operationService;
	@Inject
	private JobsPanel jobsPanel;
	@Inject
	private MainPanel gtMainPanel;

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

	/**
	 * Pannello di riepilogo di un incarico. Mostra i dati di testata, le
	 * operazioni e i documenti coninvolti nell'incarico oggetto del pannello.
	 */
	public JobPanel()
	{
		operationsProperties =
		  new String[] {"description", "operationType", "isVacazione",
		    "isProfessionalVacazione", "operationDate", "amount", "numVacazioni"};
		operationsColumnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.type"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isVacazione"),
		    getResourceMap().getString(
		      LOCALIZATION_PREFIX + "Table.isProfessionalVacazione"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.amount"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.numVacazioni")};
		operationsWritables =
		  new boolean[] {false, false, false, false, false, false, false};

		documentsProperties =
		  new String[] {"protocol", "description", "recipient", "isDigital", "uri",
		    "isOutgoing", "releaseDate", "notes"};
		documentsColumnsNames =
		  new String[] {
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.recipient"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.isDigital"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.file"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.outgoing"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.date"),
		    getResourceMap().getString(LOCALIZATION_PREFIX + "Table.notes")};
		documentsWritables =
		  new boolean[] {false, false, false, false, false, false, false, false};


		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(getToolBar(), BorderLayout.NORTH);
		northPanel.add(getHeaderPanel(), BorderLayout.CENTER);

		JPanel contentPanel = new JPanel(new BorderLayout());

		JPanel operationsPanel = new JPanel(new BorderLayout());
		JLabel operationsLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "operations"));
		operationsLabel.setFont(FontProvider.TITLE_SMALL);
		operationsPanel.add(operationsLabel, BorderLayout.NORTH);
		operationsPanel.add(new JScrollPane(getOperationsTable()),
		  BorderLayout.CENTER);

		JPanel documentsPanel = new JPanel(new BorderLayout());
		JLabel documentsLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "documents"));
		documentsLabel.setFont(FontProvider.TITLE_SMALL);
		documentsPanel.add(documentsLabel, BorderLayout.NORTH);
		documentsPanel.add(new JScrollPane(getDocumentsTable()),
		  BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.add(operationsPanel);
		splitPane.add(documentsPanel);
		contentPanel.add(splitPane, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		add(mainPanel);
	}

	/**
	 * Restituisce la toolbar.
	 * 
	 * @return la toolbar
	 */
	public JToolBar getToolBar()
	{
		if(toolBar == null)
		{
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
	public JPanel getHeaderPanel()
	{
		if(headerPanel == null)
		{
			JLabel titleLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "title"));
			titleLabel.setFont(FontProvider.TITLE_SMALL);
			JLabel protocolLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
			JLabel dueDateLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "dueDate"));
			JLabel startDateLabel =
			  new JLabel(getResourceMap()
			    .getString(LOCALIZATION_PREFIX + "startDate"));
			JLabel finishDateLabel =
			  new JLabel(getResourceMap().getString(
			    LOCALIZATION_PREFIX + "finishDate"));
			JLabel categoryLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "category"));
			JLabel statusLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "status"));
			JLabel descriptionLabel =
			  new JLabel(getResourceMap().getString(
			    LOCALIZATION_PREFIX + "description"));
			JLabel managerLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "manager"));
			JLabel customerLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "customer"));
			JLabel notesLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));
			JLabel cityLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "city"));

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
	public JLabel getProtocolLabel()
	{
		if(protocolLabel == null)
		{
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
	public JLabel getCustomerLabel()
	{
		if(customerLabel == null)
		{
			customerLabel = new JLabel();
		}
		return customerLabel;
	}

	/**
	 * Restituisce il campo del responsabile.
	 * 
	 * @return il campo del responsabile
	 */
	public JLabel getManagerLabel()
	{
		if(managerLabel == null)
		{
			managerLabel = new JLabel();
		}
		return managerLabel;
	}

	/**
	 * Restituisce il campo della città.
	 * 
	 * @return il campo della città
	 */
	public JLabel getCityLabel()
	{
		if(cityLabel == null)
		{
			cityLabel = new JLabel();
		}
		return cityLabel;
	}

	/**
	 * Restituisce il campo della descrizione.
	 * 
	 * @return il campo della descrizione
	 */
	public JLabel getDescriptionLabel()
	{
		if(descriptionLabel == null)
		{
			descriptionLabel = new JLabel();
		}
		return descriptionLabel;
	}

	/**
	 * Restituisce il campo della scadenza.
	 * 
	 * @return il campo della scadenza
	 */
	public JLabel getDueDateLabel()
	{
		if(dueDateLabel == null)
		{
			dueDateLabel = new JLabel();
		}
		return dueDateLabel;
	}

	/**
	 * Restituisce il campo della data di inizio.
	 * 
	 * @return il campo della data di inizio
	 */
	public JLabel getStartDateLabel()
	{
		if(startDateLabel == null)
		{
			startDateLabel = new JLabel();
		}
		return startDateLabel;
	}

	/**
	 * Restituisce il campo della data di fine.
	 * 
	 * @return il campo della data di fine
	 */
	public JLabel getFinishDateLabel()
	{
		if(finishDateLabel == null)
		{
			finishDateLabel = new JLabel();
		}
		return finishDateLabel;
	}

	/**
	 * Restituisce il campo della categoria.
	 * 
	 * @return il campo della categoria
	 */
	public JLabel getCategoryLabel()
	{
		if(categoryLabel == null)
		{
			categoryLabel = new JLabel();
		}
		return categoryLabel;
	}

	/**
	 * Restituisce il campo dello stato.
	 * 
	 * @return il campo dello stato
	 */
	public JLabel getStatusLabel()
	{
		if(statusLabel == null)
		{
			statusLabel = new JLabel();
		}
		return statusLabel;
	}

	/**
	 * Restituisce il campo delle note.
	 * 
	 * @return il campo delle note
	 */
	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
		{
			notesTextArea = new JTextArea();
			notesTextArea.setEditable(false);
		}
		return notesTextArea;
	}

	/**
	 * Restituisce la tabella delle operazioni.
	 * 
	 * @return la tabella delle operazioni
	 */
	public JXTable getOperationsTable()
	{
		if(operationsTable == null)
		{
			operationsTable = GreenToneUtilities.createJXTable();
		}
		return operationsTable;
	}

	/**
	 * Restituisce la tabella dei documenti.
	 * 
	 * @return la tabella dei documenti
	 */
	public JXTable getDocumentsTable()
	{
		if(documentsTable == null)
		{
			documentsTable = GreenToneUtilities.createJXTable();
		}
		return documentsTable;
	}

	@Override
	public String getBundleName()
	{
		return "viewJob";
	}

	/**
	 * Prima di invocare questo metodo, impostare l'oggetto del pannello con il
	 * metodo {@link #setJob(Job)}.
	 */
	@Override
	public void setup()
	{
		super.setup();

		/* toolbar */
		getToolBar().removeAll();
		JButton goButton =
		  new JButton(getResourceMap().getIcon("viewJob.Action.goToJobs"));
		goButton.setToolTipText(getResourceMap().getString("viewJob.Panel.goto"));
		goButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
				{
					jobsPanel.setup();
					ContextualAction.addTab(gtMainPanel, jobsPanel);
					jobsPanel.setSelectedJob(job);
				}
			});
		getToolBar().add(goButton);


		/* informazioni di testata */
		getProtocolLabel().setText(job.getProtocol());
		getCustomerLabel().setText(
		  job.getCustomer() != null? job.getCustomer().toString(): null);
		getManagerLabel().setText(
		  job.getManager() != null? job.getManager().toString(): null);
		getCityLabel().setText(job.getCity());
		getDescriptionLabel().setText(job.getDescription());
		getDueDateLabel().setText(
		  GreenToneUtilities.formatDateTime(job.getDueDate()));
		getStartDateLabel().setText(
		  GreenToneUtilities.formatDateTime(job.getStartDate()));
		getFinishDateLabel().setText(
		  GreenToneUtilities.formatDateTime(job.getFinishDate()));
		getCategoryLabel().setText(
		  job.getCategory() != null? job.getCategory().toString(): null);
		getStatusLabel().setText(
		  job.getStatus() != null? job.getStatus().getLocalizedName(): null);
		getNotesTextArea().setText(job.getNotes());

		/* informazioni su operazioni */
		operations = operationService.getOperationsJob(job);
		EventList<Operation> operationsEventList = new BasicEventList<Operation>();
		operationsEventList.addAll(operations);
		EventJXTableModel<Operation> operationsTableModel =
		  new EventJXTableModel<Operation>(operationsEventList,
		    new BeanTableFormat<Operation>(Operation.class, operationsProperties,
		      operationsColumnsNames, operationsWritables));
		getOperationsTable().setModel(operationsTableModel);
		getOperationsTable().setSortOrder(1, SortOrder.DESCENDING);

		/* informazioni su documenti */
		documents = documentService.getDocumentsJob(job);
		EventList<Document> documentsEventList = new BasicEventList<Document>();
		documentsEventList.addAll(documents);
		EventJXTableModel<Document> documentsTableModel =
		  new EventJXTableModel<Document>(documentsEventList,
		    new BeanTableFormat<Document>(Document.class, documentsProperties,
		      documentsColumnsNames, documentsWritables));
		getDocumentsTable().setModel(documentsTableModel);
		getDocumentsTable().setSortOrder(0, SortOrder.DESCENDING);
	}

	/**
	 * Imposta l'incarico di cui mostrare il dettaglio delle operazioni e dei
	 * documenti.
	 * 
	 * @param job
	 *          l'incarico di cui mostrare il dettaglio delle operazioni e dei
	 *          documenti
	 */
	public void setJob(Job job)
	{
		this.job = job;
	}
}
