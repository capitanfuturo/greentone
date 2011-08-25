package it.greentone;

import it.greentone.gui.panel.MainPanel;

import javax.inject.Inject;
import javax.jdo.PersistenceManagerFactory;

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
 * Classe di lancio dell'applicazione e di collegamento a BSAF.
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
		/* carico il contesto creato da Spring Framework */
		ApplicationContext applicationContext =
		  new ClassPathXmlApplicationContext(
		    GreenToneAppConfig.SPRING_CONFIG_LOCATION);
		/* recupero il wrapper che contiene i bean creati da Spring Framework */
		springBeansHolder = applicationContext.getBean(SpringBeansHolder.class);
		// TODO PasswordDialog passwordDialog = new PasswordDialog();
	}

	@Override
	protected void startup()
	{
		getMainFrame().setTitle(
		  getContext().getResourceMap().getString("Application.title"));
		MainPanel mainPanel = springBeansHolder.getMainPanel();
		mainPanel.initialize();
		show(mainPanel);
	}

	@Override
	protected void shutdown()
	{
		super.shutdown();
	}

	@Component
	public static class SpringBeansHolder
	{
		@Inject
		private PersistenceManagerFactory pmf;
		@Inject
		private MainPanel mainPanel;

		public PersistenceManagerFactory getPmf()
		{
			return pmf;
		}

		public void setPmf(PersistenceManagerFactory pmf)
		{
			this.pmf = pmf;
		}

		public MainPanel getMainPanel()
		{
			return mainPanel;
		}

		public void setMainPanel(MainPanel mainPanel)
		{
			this.mainPanel = mainPanel;
		}
	}
}
