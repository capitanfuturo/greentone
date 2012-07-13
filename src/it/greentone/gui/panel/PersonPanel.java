package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.ModelEventManager;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Person;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
 * Pannello di riepilogo di un cliente. Mostra i dati di testata e gli incarichi
 * del cliente oggetto del pannello.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class PersonPanel extends AbstractPanel {
	@Inject
	private JobService jobService;

	private static final String LOCALIZATION_PREFIX = "viewPerson.Panel.";
	private final String[] jobsProperties;
	private final String[] jobsColumnsNames;
	private final boolean[] jobsWritables;
	private Person person;
	private JToolBar toolBar;
	private JPanel headerPanel;
	private JXTable jobsTable;
	private JLabel nameLabel;
	private JLabel nameField;
	private JLabel addressLabel;
	private JLabel cityLabel;
	private JLabel provinceLabel;
	private JLabel capLabel;
	private JLabel cfLabel;
	private JLabel pivaLabel;
	private JLabel telephone1Label;
	private JLabel telephone2Label;
	private JLabel faxLabel;
	private JLabel emailLabel;
	private JLabel identityCardLabel;
	private JButton backDetailButton;
	private JButton jobDetailButton;
	private JPanel contentPanel;
	private static final String GLOBAL_PANEL = "GLOBAL_PANEL";
	private static final String JOB_PANEL = "JOB_PANEL";

	private EventJXTableModel<Job> jobsTableModel;

	/**
	 * Pannello di riepilogo di un cliente. Mostra i dati di testata e gli
	 * incarichi del cliente oggetto del pannello.
	 * 
	 * @param modelEventManager
	 *            manager degli eventi
	 */
	@Inject
	public PersonPanel(ModelEventManager modelEventManager) {
		jobsProperties = new String[] { "protocol", "manager", "description", "dueDate", "status" };
		jobsColumnsNames = new String[] { getResourceMap().getString(LOCALIZATION_PREFIX + "Table.protocol"), getResourceMap().getString(LOCALIZATION_PREFIX + "Table.manager"), getResourceMap().getString(LOCALIZATION_PREFIX + "Table.description"),
				getResourceMap().getString(LOCALIZATION_PREFIX + "Table.dueDate"), getResourceMap().getString(LOCALIZATION_PREFIX + "Table.status") };
		jobsWritables = new boolean[] { false, false, false, false, false };

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(getToolBar(), BorderLayout.NORTH);
		northPanel.add(getHeaderPanel(), BorderLayout.CENTER);

		JPanel jobHeaderPanel = new JPanel(new MigLayout());
		JLabel jobsLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "jobs"));
		jobsLabel.setFont(FontProvider.TITLE_SMALL);
		jobHeaderPanel.add(jobsLabel);
		jobHeaderPanel.add(getJobDetailButton());

		JPanel jobsPanel = new JPanel(new BorderLayout());
		jobsPanel.add(jobHeaderPanel, BorderLayout.NORTH);
		jobsPanel.add(new JScrollPane(getJobsTable()), BorderLayout.CENTER);

		getContentPanel().add(jobsPanel, GLOBAL_PANEL);

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
				String propertyName = evt.getPropertyName();
				if (propertyName.equals(ModelEventManager.JOB_INSERTED) || propertyName.equals(ModelEventManager.JOB_MODIFIED) || propertyName.equals(ModelEventManager.JOB_DELETED)) {
					Job job = (Job) evt.getNewValue();
					if (job.getCustomer().getId() == person.getId()) {
						loadJobs();
					}
				} else if (propertyName.equals(ModelEventManager.PERSON_MODIFIED)) {
					Person p = (Person) evt.getNewValue();
					if (p.getId() == person.getId()) {
						loadHeader();
					}
				}
			}
		});
	}

	/**
	 * Restituisce la tabella dei documenti.
	 * 
	 * @return la tabella dei documenti
	 */
	public JXTable getJobsTable() {
		if (jobsTable == null) {
			jobsTable = GreenToneUtilities.createJXTable();
		}
		return jobsTable;
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
			JLabel addressLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "address"));
			JLabel cityLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "city"));
			JLabel provinceLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "province"));
			JLabel capLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "cap"));
			JLabel cfLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "cf"));
			JLabel pivaLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "piva"));
			JLabel telephone1Label = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "telephone1"));
			JLabel telephone2Label = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "telephone2"));
			JLabel faxLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "fax"));
			JLabel emailLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "email"));
			JLabel identityCardLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "identityCard"));

			headerPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%][][10%]"));

			headerPanel.add(titleLabel, "wrap");

			headerPanel.add(getNameLabel(), "gap para");
			headerPanel.add(getNameField(), "span 2, growx, wrap");

			headerPanel.add(addressLabel, "gap para");
			headerPanel.add(getAddressLabel(), "growx");
			headerPanel.add(cityLabel, "gap para");
			headerPanel.add(getCityLabel(), "growx");
			headerPanel.add(provinceLabel, "gap para");
			headerPanel.add(getProvinceLabel(), "growx");
			headerPanel.add(capLabel, "gap para");
			headerPanel.add(getCapLabel(), "growx, wrap");

			headerPanel.add(cfLabel, "gap para");
			headerPanel.add(getCfLabel(), "growx");
			headerPanel.add(pivaLabel, "gap para");
			headerPanel.add(getPivaLabel(), "growx");
			headerPanel.add(identityCardLabel, "gap para");
			headerPanel.add(getIdentityCardLabel(), "growx, wrap");

			headerPanel.add(telephone1Label, "gap para");
			headerPanel.add(getTelephone1Label(), "growx");
			headerPanel.add(telephone2Label, "gap para");
			headerPanel.add(getTelephone2Label(), "growx");
			headerPanel.add(faxLabel, "gap para");
			headerPanel.add(getFaxLabel(), "growx");
			headerPanel.add(emailLabel, "gap para");
			headerPanel.add(getEmailLabel(), "growx, wrap");
		}
		return headerPanel;
	}

	/**
	 * Restituisce il campo della ragione sociale.
	 * 
	 * @return il campo della ragione sociale
	 */
	public JLabel getNameField() {
		if (nameField == null) {
			nameField = new JLabel();
		}
		return nameField;
	}

	/**
	 * Restituisce il campo dell'indirizzo.
	 * 
	 * @return il campo dell'indirizzo
	 */
	public JLabel getAddressLabel() {
		if (addressLabel == null) {
			addressLabel = new JLabel();
		}
		return addressLabel;
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
	 * Restituisce il campo della provincia.
	 * 
	 * @return il campo della provincia
	 */
	public JLabel getProvinceLabel() {
		if (provinceLabel == null) {
			provinceLabel = new JLabel();
		}
		return provinceLabel;
	}

	/**
	 * Restituisce il campo del CAP.
	 * 
	 * @return il campo del CAP
	 */
	public JLabel getCapLabel() {
		if (capLabel == null) {
			capLabel = new JLabel();
		}
		return capLabel;
	}

	/**
	 * Restituisce il campo del codice fiscale.
	 * 
	 * @return il campo del codice fiscale
	 */
	public JLabel getCfLabel() {
		if (cfLabel == null) {
			cfLabel = new JLabel();
		}
		return cfLabel;
	}

	/**
	 * Restituisce il campo della partita IVA.
	 * 
	 * @return il campo della partita IVA
	 */
	public JLabel getPivaLabel() {
		if (pivaLabel == null) {
			pivaLabel = new JLabel();
		}
		return pivaLabel;
	}

	/**
	 * Restituisce il campo della carta di identità di riferimento.
	 * 
	 * @return il campo della carta di identità di riferimento
	 */
	public JLabel getIdentityCardLabel() {
		if (identityCardLabel == null) {
			identityCardLabel = new JLabel();
		}
		return identityCardLabel;
	}

	/**
	 * Restituisce il campo del telefono principale.
	 * 
	 * @return il campo del telefono principale
	 */
	public JLabel getTelephone1Label() {
		if (telephone1Label == null) {
			telephone1Label = new JLabel();
		}
		return telephone1Label;
	}

	/**
	 * Restituisce il campo del telefono secondario.
	 * 
	 * @return il campo del telefono secondario
	 */
	public JLabel getTelephone2Label() {
		if (telephone2Label == null) {
			telephone2Label = new JLabel();
		}
		return telephone2Label;
	}

	/**
	 * Restituisce il campo del FAX.
	 * 
	 * @return il campo del FAX
	 */
	public JLabel getFaxLabel() {
		if (faxLabel == null) {
			faxLabel = new JLabel();
		}
		return faxLabel;
	}

	/**
	 * Restituisce il campo della email.
	 * 
	 * @return il campo della email
	 */
	public JLabel getEmailLabel() {
		if (emailLabel == null) {
			emailLabel = new JLabel();
		}
		return emailLabel;
	}

	/**
	 * Restituisce l'etichetta della ragione sociale.
	 * 
	 * @return l'etichetta della ragione sociale
	 */
	public JLabel getNameLabel() {
		if (nameLabel == null) {
			nameLabel = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "surnameName"));
		}
		return nameLabel;
	}

	/**
	 * Restituisce il pannello sottostante che raccoglie i contenuti
	 * dell'incarico.
	 * 
	 * @return il pannello sottostante che raccoglie i contenuti dell'incarico
	 */
	public JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel(new CardLayout());
		}
		return contentPanel;
	}

	/**
	 * Restituisce il pulsante per tornare alla vista con gli incarichi della
	 * scheda.
	 * 
	 * @return il pulsante per tornare alla vista con gli incarichi della scheda
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

	/**
	 * Restituisce il pulsante che permette di passare dalla vista tabellare al
	 * dettaglio dell'incarico selezionato.
	 * 
	 * @return il pulsante che permette di passare dalla vista tabellare al
	 *         dettaglio dell'incarico selezionato
	 */
	public JButton getJobDetailButton() {
		if (jobDetailButton == null) {
			jobDetailButton = new JButton(getResourceMap().getIcon("viewJob.Action.smallIcon"));
			/* abilitazione del tasto */
			jobDetailButton.setEnabled(false);
			getJobsTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					int selectedRow = getJobsTable().getSelectedRow();
					jobDetailButton.setEnabled(selectedRow > -1);
				}
			});
			/* azione del pulsante */
			jobDetailButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int selectedRow = getJobsTable().getSelectedRow();
					if (selectedRow > -1) {
						int rowIndexToModel = getJobsTable().convertRowIndexToModel(getJobsTable().getSelectedRow());
						JPanel operationPanel = createJobPanel(jobsTableModel.getElementAt(rowIndexToModel));
						getContentPanel().add(operationPanel, JOB_PANEL);

						CardLayout cl = (CardLayout) (getContentPanel().getLayout());
						cl.show(getContentPanel(), JOB_PANEL);
					}
				}
			});
		}
		return jobDetailButton;
	}

	JPanel createJobPanel(Job job) {
		JPanel jobPanel = new JPanel(new MigLayout("", "[][10%][][10%][][10%]"));

		JLabel title = new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "jobDetail"));
		title.setFont(FontProvider.TITLE_SMALL);
		jobPanel.add(title);
		jobPanel.add(getBackDetailButton(), "wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.protocol")), "gap para");
		jobPanel.add(new JLabel(job.getProtocol()), "growx, wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.manager")), "gap para");
		jobPanel.add(new JLabel(job.getManager() != null ? job.getManager().toString() : ""), "growx");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.city")), "gap para");
		jobPanel.add(new JLabel(job.getCity()), "growx,wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.description")), "gap para");
		jobPanel.add(new JLabel(job.getDescription()), "span, growx, wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.dueDate")), "gap para");
		jobPanel.add(new JLabel(GreenToneUtilities.formatDateTime(job.getDueDate())), "growx");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.startDate")), "gap para");
		jobPanel.add(new JLabel(GreenToneUtilities.formatDateTime(job.getStartDate())), "growx");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.finishDate")), "gap para");
		jobPanel.add(new JLabel(GreenToneUtilities.formatDateTime(job.getFinishDate())), "growx, wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.category")), "gap para");
		jobPanel.add(new JLabel(job.getCategory() != null ? job.getCategory().getName() : ""), "growx");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.status")), "gap para");
		jobPanel.add(new JLabel(job.getStatus().getLocalizedName()), "growx, wrap");
		jobPanel.add(new JLabel(getResourceMap().getString("viewJobs.Panel.notes")), "gap para");
		JTextArea notesTextArea = new JTextArea(5, 50);
		notesTextArea.setText(job.getNotes());
		jobPanel.add(notesTextArea, "span, growx, wrap");

		return jobPanel;
	}

	@Override
	public String getBundleName() {
		return "viewPerson";
	}

	@Override
	public void setup() {
		super.setup();
		loadHeader();
		/* informazioni su incarichi */
		loadJobs();
		getJobsTable().setSortOrder(0, SortOrder.DESCENDING);
	}

	/**
	 * Imposta la persona oggetto del pannello.
	 * 
	 * @param person
	 *            la persona oggetto del pannello
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	private void loadJobs() {
		Collection<Job> jobs = jobService.getJobsAsCustomer(person);
		EventList<Job> jobsEventList = new BasicEventList<Job>();
		jobsEventList.addAll(jobs);
		jobsTableModel = new EventJXTableModel<Job>(jobsEventList, new BeanTableFormat<Job>(Job.class, jobsProperties, jobsColumnsNames, jobsWritables));
		getJobsTable().setModel(jobsTableModel);
	}

	private void loadHeader() {
		/* informazioni di testata */
		if (person.getIsLegal()) {
			getNameLabel().setText(getResourceMap().getString(LOCALIZATION_PREFIX + "name"));
		} else {
			getNameLabel().setText(getResourceMap().getString(LOCALIZATION_PREFIX + "surnameName"));
		}
		getNameField().setText(person.getName());
		getAddressLabel().setText(person.getAddress());
		getCityLabel().setText(person.getCity());
		getProvinceLabel().setText(person.getProvince());
		getCapLabel().setText(person.getCap());
		getCfLabel().setText(person.getCf());
		getPivaLabel().setText(person.getPiva());
		getTelephone1Label().setText(person.getTelephone1());
		getTelephone2Label().setText(person.getTelephone2());
		getFaxLabel().setText(person.getFax());
		getEmailLabel().setText(person.getEmail());
	}
}
