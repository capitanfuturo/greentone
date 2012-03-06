package it.greentone;

import it.greentone.gui.MainPanel;
import it.greentone.gui.action.ViewHomeAction;

import java.io.File;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

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
 * Classe di lancio dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
public class GreenTone extends SingleFrameApplication
{
	private SpringBeansHolder springBeansHolder;

	/**
	 * Esegue l'applicazione. Per il momento non sono disponibili argomenti di
	 * lancio.
	 * 
	 * @param args
	 *          array degli argomenti del comando di lancio
	 */
	public static void main(final String[] args)
	{
		Application.launch(GreenTone.class, args);
	}

	@Override
	protected void initialize(String[] args)
	{
		super.initialize(args);
		/*
		 * Imposto come directory di salvataggio delle proprietà grafiche del
		 * programma
		 */
		File filePath = new File(GreenToneAppConfig.BASE_PATH);
		if(!filePath.exists())
		{
			filePath.mkdirs();
		}
		getContext().getLocalStorage().setDirectory(filePath);

		/* carico il contesto creato da Spring Framework */
		ApplicationContext applicationContext =
		  new ClassPathXmlApplicationContext(
		    GreenToneAppConfig.SPRING_CONFIG_LOCATION);
		/* recupero il wrapper che contiene i bean creati da Spring Framework */
		springBeansHolder = applicationContext.getBean(SpringBeansHolder.class);
		// TODO JXLoginDialog PasswordDialog passwordDialog = new PasswordDialog();
		springBeansHolder.getLogProvider().getLogger().info("Avvio Greentone");
	}

	@Override
	protected void startup()
	{
		final String applicationTitle =
		  getContext().getResourceMap().getString("Application.title");
		getMainFrame().setTitle(applicationTitle);
		final MainPanel mainPanel = springBeansHolder.getMainPanel();
		springBeansHolder.getLogProvider().getLogger()
		  .info("Inizializzazione pannello principale");
		mainPanel.initialize();
		/* aggiungo un listener per la chiusura dell'applicazione */
		addExitListener(new ExitListener()
			{

				@Override
				public void willExit(EventObject event)
				{
					// non va nulla per adesso.
				}

				@Override
				public boolean canExit(EventObject event)
				{
					int confirmDialog =
					  JOptionPane.showConfirmDialog(mainPanel, getContext()
					    .getResourceMap().getString("exit.Action.confirmMessage"),
					    applicationTitle, JOptionPane.YES_NO_OPTION);
					return confirmDialog == JOptionPane.OK_OPTION;
				}
			});
		show(mainPanel);
		springBeansHolder.getLogProvider().getLogger()
		  .info("Interfaccia grafica avviata");

		/* se è il primo avvio allora va gestito l'uso dell'annno del protocollo */
		if(springBeansHolder.getConfigurationProperties().isFirstLaunch())
		{
			springBeansHolder.getLogProvider().getLogger()
			  .info("Rilevato primo avvio");
			int answer =
			  JOptionPane.showConfirmDialog(mainPanel, getContext().getResourceMap()
			    .getString("Application.firstLaunch"), applicationTitle,
			    JOptionPane.YES_NO_OPTION);
			springBeansHolder.getConfigurationProperties().setUseYearsInJobsProtocol(
			  answer == JOptionPane.YES_OPTION);
			springBeansHolder.getConfigurationProperties().store();
		}

		/*
		 * processo in background per verificare l'esistenza di una nuova versione
		 * del programma
		 */
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
			{

				@Override
				protected Void doInBackground() throws Exception
				{
					String remoteVersion =
					  springBeansHolder.getUtilities().checkUpdates();
					if(remoteVersion != null)
					{
						String currentVersion =
						  getContext().getResourceMap().getString("Application.version");
						String message =
						  MessageFormat.format(
						    getContext().getResourceMap().getString(
						      "Application.newVersionAvaible"), currentVersion,
						    remoteVersion);
						JOptionPane.showMessageDialog(mainPanel, message, getContext()
						  .getResourceMap().getString("viewPersons.Panel.infoTitle"),
						  JOptionPane.INFORMATION_MESSAGE);
					}
					return null;
				}

			};
		/*
		 * se è abilitato il controllo degli aggiornamenti allora avvio il processo
		 * in background
		 */
		if(springBeansHolder.getConfigurationProperties().isCheckUpdateActivated())
		{
			springBeansHolder.getLogProvider().getLogger()
			  .info("Controllo aggiornamenti");
			worker.execute();
		}
		/* mostro la schermata iniziale */
		springBeansHolder.getViewHomeAction().viewHome();
	}

	@Override
	protected void shutdown()
	{
		super.shutdown();
		springBeansHolder.getConfigurationProperties().store();
		springBeansHolder.getLogProvider().getLogger().info("Esco");
	}

	/**
	 * Bean di comunicazione tra la classe di lancio dell'applicazione (framework
	 * BSAF) e il framework di Spring.
	 * 
	 * @author Giuseppe Caliendo
	 */
	@Component
	public static class SpringBeansHolder
	{
		@Inject
		private PersistenceManagerFactory pmf;
		@Inject
		private MainPanel mainPanel;
		@Inject
		private ConfigurationProperties configurationProperties;
		@Inject
		private GreenToneUtilities utilities;
		@Inject
		private GreenToneLogProvider logProvider;
		@Inject
		private ViewHomeAction viewHomeAction;

		/**
		 * Restituisce la factory del manager della persistenza di JDO.
		 * 
		 * @return la factory del manager della persistenza di JDO
		 */
		public PersistenceManagerFactory getPmf()
		{
			return pmf;
		}

		/**
		 * Imposta la factory del manager della persistenza di JDO.
		 * 
		 * @param pmf
		 *          la factory del manager della persistenza di JDO
		 */
		public void setPmf(PersistenceManagerFactory pmf)
		{
			this.pmf = pmf;
		}

		/**
		 * Restituisce il pannello principale dell'applicazione.
		 * 
		 * @return il pannello principale dell'applicazione
		 */
		public MainPanel getMainPanel()
		{
			return mainPanel;
		}

		/**
		 * Imposta il pannello principale dell'applicazione.
		 * 
		 * @param mainPanel
		 *          il pannello principale dell'applicazione
		 */
		public void setMainPanel(MainPanel mainPanel)
		{
			this.mainPanel = mainPanel;
		}

		/**
		 * Restituisce la configurazione utente di Greentone.
		 * 
		 * @return la configurazione utente di Greentone
		 */
		public ConfigurationProperties getConfigurationProperties()
		{
			return configurationProperties;
		}

		/**
		 * Imposta la configurazione utente di Greentone.
		 * 
		 * @param configurationProperties
		 *          la configurazione utente di Greentone
		 */
		public void setConfigurationProperties(
		  ConfigurationProperties configurationProperties)
		{
			this.configurationProperties = configurationProperties;
		}

		/**
		 * Restituisce l'oggetto di utilità dell'applicazione.
		 * 
		 * @return l'oggetto di utilità dell'applicazione
		 */
		public GreenToneUtilities getUtilities()
		{
			return utilities;
		}

		/**
		 * Imposta l'oggetto di utilità dell'applicazione.
		 * 
		 * @param utilities
		 *          l'oggetto di utilità dell'applicazione
		 */
		public void setUtilities(GreenToneUtilities utilities)
		{
			this.utilities = utilities;
		}

		/**
		 * Restituisce il provider del logger.
		 * 
		 * @return il provider del logger
		 */
		public GreenToneLogProvider getLogProvider()
		{
			return logProvider;
		}

		/**
		 * Imposta il provider del logger.
		 * 
		 * @param logProvider
		 *          il provider del logger
		 */
		public void setLogProvider(GreenToneLogProvider logProvider)
		{
			this.logProvider = logProvider;
		}

		/**
		 * Restituisce il provider delle azioni.
		 * 
		 * @return il provider delle azioni
		 */
		public ViewHomeAction getViewHomeAction()
		{
			return viewHomeAction;
		}

		/**
		 * Imposta il provider delle azioni.
		 * 
		 * @param viewHomeAction
		 *          il provider delle azioni
		 */
		public void setViewHomeAction(ViewHomeAction viewHomeAction)
		{
			this.viewHomeAction = viewHomeAction;
		}
	}
}
