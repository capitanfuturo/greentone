package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;
import it.greentone.gui.AbstractPanel;
import it.greentone.gui.FontProvider;
import it.greentone.gui.MainPanel;
import it.greentone.gui.action.ActionProvider;
import it.greentone.gui.action.ViewJobsAction;
import it.greentone.persistence.Job;
import it.greentone.persistence.JobService;
import it.greentone.persistence.JobStatus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Comparator;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.ResourceMap;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.Matcher;

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
 * Pannello di inizio dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class HomePanel extends AbstractPanel
{
	private static final String LOCALIZATION_PREFIX = "viewHome.Panel.";
	private static final String PANEL_BUNDLE = "viewHome";
	private static final int NEXT_EXPIRING_JOB_DAYS_INTERVAL = 30;

	@Inject
	private ActionProvider actionProvider;
	@Inject
	private JobService jobService;
	@Inject
	private JobPanel jobPanel;
	@Inject
	private MainPanel mainPanel;
	@Inject
	private ViewJobsAction viewJobsAction;

	ResourceMap resourceMap;
	JPanel centralPanel;
	private JPanel searchPanel;
	private JPanel agendaPanel;
	private JButton searchButton;
	private JTextField searchTextField;
	private JPanel resultPanel;
	private JToolBar contextualToolBar;
	private JSplitPane sideSplitPane;
	private JSplitPane splitPane;

	/**
	 * Pannello di inizio dell'applicazione.
	 */
	public HomePanel()
	{
		super();
		setLayout(new BorderLayout());

		add(getContextualToolBar(), BorderLayout.NORTH);
		add(getSplitPane(), BorderLayout.CENTER);
	}

	private JSplitPane getSplitPane()
	{
		if(splitPane == null)
		{
			splitPane =
			  new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getCentralPanel(),
			    getSideSplitPane());
		}
		return splitPane;
	}

	private JSplitPane getSideSplitPane()
	{
		if(sideSplitPane == null)
		{
			sideSplitPane =
			  new JSplitPane(JSplitPane.VERTICAL_SPLIT, getSearchPanel(),
			    getAgendaPanel());
		}
		return sideSplitPane;
	}

	/**
	 * Restituisce il pannello centrale della home page.
	 * 
	 * @return il pannello centrale della home page
	 */
	public JPanel getCentralPanel()
	{
		if(centralPanel == null)
		{
			centralPanel = new JPanel(new MigLayout("", "[100%]"));
		}
		return centralPanel;
	}

	/**
	 * Restituisce il pannello dedicato alla ricerca degli incarichi.
	 * 
	 * @return il pannello dedicato alla ricerca degli incarichi
	 */
	public JPanel getSearchPanel()
	{
		if(searchPanel == null)
		{
			searchPanel = new JPanel(new MigLayout("", "[70%][30%]", "[][][]"));
			JLabel searchLabel =
			  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "search"));
			searchLabel.setFont(FontProvider.TITLE_SMALL);

			searchPanel.add(searchLabel, "span 2, wrap");
			searchPanel.add(getSearchTextField(), "growx");
			searchPanel.add(getSearchButton(), "wrap");
			searchPanel.add(getResultPanel(), "span 2");
		}
		return searchPanel;
	}

	/**
	 * Restituisce il campo di testo della ricerca.
	 * 
	 * @return il campo di testo della ricerca
	 */
	public JTextField getSearchTextField()
	{
		if(searchTextField == null)
		{
			searchTextField = new JTextField();
			searchTextField.getDocument().addDocumentListener(new DocumentListener()
				{

					@Override
					public void removeUpdate(DocumentEvent e)
					{
						validateButton();
					}

					@Override
					public void insertUpdate(DocumentEvent e)
					{
						validateButton();
					}

					@Override
					public void changedUpdate(DocumentEvent e)
					{
						validateButton();
					}

					private void validateButton()
					{
						getSearchButton().setEnabled(
						  GreenToneUtilities.getText(searchTextField) != null);
					}
				});
		}
		return searchTextField;
	}

	/**
	 * Restituisce il pulsante di ricerca.
	 * 
	 * @return il pulsante di ricerca
	 */
	public JButton getSearchButton()
	{
		if(searchButton == null)
		{
			searchButton =
			  new JButton(getResourceMap().getString(
			    LOCALIZATION_PREFIX + "searchButton"));
			searchButton.setToolTipText(getResourceMap().getString(
			  LOCALIZATION_PREFIX + "searchButtonToolTip"));
			searchButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						searchButton.setEnabled(false);
						getResultPanel().removeAll();
						getResultPanel().add(
						  new JLabel(getResourceMap().getString(
						    LOCALIZATION_PREFIX + "searching")));
						getSplitPane().validate();

						new SwingWorker<Void, Void>()
							{

								@Override
								protected Void doInBackground() throws Exception
								{

									Collection<Job> jobs =
									  jobService.getJobsContainingDescription(GreenToneUtilities
									    .getText(getSearchTextField()));
									getResultPanel().removeAll();

									if(jobs.isEmpty())
									{
										getResultPanel().add(
										  new JLabel(getResourceMap().getString(
										    LOCALIZATION_PREFIX + "noSearchResultFound")));
									}
									else
									{
										for(Job job : jobs)
										{
											getResultPanel().add(
											  new AgendaDetailPanel(job, jobPanel, viewJobsAction,
											    getResourceMap()), "growx, wrap");
											getSplitPane().validate();
										}
									}
									return null;
								}

								@Override
								protected void done()
								{
									searchButton.setEnabled(true);
									validate();
								};
							}.execute();
					}
				});
			searchButton.setEnabled(false);
		}
		return searchButton;
	}

	/**
	 * Restituisce il pannello con i risultati della ricerca.
	 * 
	 * @return il pannello con i risultati della ricerca
	 */
	public JPanel getResultPanel()
	{
		if(resultPanel == null)
		{
			resultPanel = new JPanel(new MigLayout("fillx"));
		}
		return resultPanel;
	}

	/**
	 * Restituisce il pannello "Agenda" con con in testa gli incarichi "scaduti"
	 * senza data di fine e in coda gli incarichi in scadenza nei trenta giorni
	 * successivi.
	 * 
	 * @return il pannello "Agenda" con con in testa gli incarichi "scaduti" senza
	 *         data di fine e in coda gli incarichi in scadenza nei trenta giorni
	 *         successivi
	 */
	public JPanel getAgendaPanel()
	{
		if(agendaPanel == null)
		{
			agendaPanel = new JPanel(new MigLayout("fillx"));
		}
		return agendaPanel;
	}

	@Override
	public void setup()
	{
		super.setup();
		/* toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getViewHelp());

		/* pulisco i pannelli */
		getCentralPanel().removeAll();
		getAgendaPanel().removeAll();

		/* aggiorno i pannelli */
		EventList<Job> allJobs = jobService.getAllJobs();
		FilterList<Job> fileteredJobs =
		  new FilterList<Job>(allJobs, new Matcher<Job>()
			  {

				  @Override
				  public boolean matches(Job job)
				  {
					  if(job.getStatus() == JobStatus.PLANNING
					    || job.getStatus() == JobStatus.WORKING)
					  {
						  return true;
					  }
					  return false;
				  }
			  });
		SortedList<Job> allJobsStartDate =
		  new SortedList<Job>(fileteredJobs, new Comparator<Job>()
			  {

				  @Override
				  public int compare(Job o1, Job o2)
				  {
					  DateTime o1StartDate = o1.getStartDate();
					  DateTime o2StartDate = o2.getStartDate();

					  if(o1StartDate != null)
					  {
						  return o1StartDate.compareTo(o2StartDate);
					  }
					  else
						  if(o2StartDate != null)
						  {
							  return -(o2StartDate.compareTo(o1StartDate));
						  }
						  else
							  return o1.getProtocol().compareToIgnoreCase(o2.getProtocol());
				  }
			  });

		JLabel titleLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "jobList"));
		titleLabel.setFont(FontProvider.TITLE_SMALL);
		getCentralPanel().add(titleLabel, "wrap");
		getCentralPanel().add(
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "jobListDescription")), "wrap");

		/*
		 * popolo il pannello centrale con gli incarichi in lavorazione ordinati per
		 * data di inizio
		 */
		for(Job job : allJobsStartDate)
		{
			JobDetailsPanel jobDetailsPanel =
			  new JobDetailsPanel(job, jobPanel, viewJobsAction, getResourceMap());
			getCentralPanel().add(jobDetailsPanel, "growx, wrap");
		}
		/*
		 * Issue 127: se non c'è nessun incarico da visualizzare lo segnalo con un
		 * messaggio all'utente
		 */
		if(allJobsStartDate.isEmpty())
		{
			getCentralPanel().add(
			  new JLabel(getResourceMap().getString("viewHome.Panel.noJobs")));
		}

		/*
		 * popolo il pannello laterale con con in testa gli incarichi "scaduti"
		 * senza data di fine e in coda gli incarichi in scadenza nei trenta giorni
		 * successivi
		 */
		JLabel agendaLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "agenda"));
		agendaLabel.setFont(FontProvider.TITLE_SMALL);
		getAgendaPanel().add(agendaLabel, "wrap");
		getAgendaPanel().add(
		  new JLabel(getResourceMap().getString(
		    LOCALIZATION_PREFIX + "agendaDescription")), "wrap");

		Collection<Job> nextExpiringJob =
		  jobService.getNextExpiringJobs(NEXT_EXPIRING_JOB_DAYS_INTERVAL);
		Collection<Job> expiredJob = jobService.getExpiredJobs();
		for(Job job : nextExpiringJob)
		{
			AgendaDetailPanel panel =
			  new AgendaDetailPanel(job, jobPanel, viewJobsAction, getResourceMap());
			getAgendaPanel().add(panel, "growx, wrap");
		}
		for(Job job : expiredJob)
		{
			AgendaDetailPanel panel =
			  new AgendaDetailPanel(job, jobPanel, viewJobsAction, getResourceMap());
			getAgendaPanel().add(panel, "growx, wrap");
		}
		if(nextExpiringJob.isEmpty() && expiredJob.isEmpty())
		{
			getAgendaPanel().add(
			  new JLabel(getResourceMap().getString(
			    LOCALIZATION_PREFIX + "noAgendaEntry")));
		}

		/* aggiusto gli splitpane */
		getSplitPane().setDividerLocation((int) (mainPanel.getWidth() * 0.7));
		getSideSplitPane().setDividerLocation((int) (mainPanel.getHeight() * 0.5));
	}

	@Override
	public String getBundleName()
	{
		return PANEL_BUNDLE;
	}

	/**
	 * Restituisce la barra degli strumenti delle azioni disponibili per il
	 * pannello corrente.
	 * 
	 * @return la barra degli strumenti delle azioni disponibili per il pannello
	 *         corrente
	 */
	public JToolBar getContextualToolBar()
	{
		if(contextualToolBar == null)
		{
			contextualToolBar = new JToolBar();
			contextualToolBar.setFloatable(false);
		}
		return contextualToolBar;
	}

	/**
	 * <ul>
	 * <li>Aggiorna il contenuto del pannello</li>
	 * <li>Pulisce la ricerca</li>
	 * </ul>
	 */
	public void refresh()
	{
		setup();
		getResultPanel().removeAll();
	}
}
