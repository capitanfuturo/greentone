package it.greentone.gui.panel;

import it.greentone.GreenToneAppConfig;
import it.greentone.GreenToneLogger;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.ContextualPanel;
import it.greentone.gui.FontProvider;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
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
 * Pannello delle informazioni sul programma.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class AboutPanel extends ContextualPanel<Void>
{
	@Inject
	private GreenToneLogger logger;
	@Inject
	private GreenToneUtilities utilities;
	private final String panelBundle;

	/**
	 * Pannello delle informazioni sul programma.
	 */
	public AboutPanel()
	{
		super();
		remove(getContextualToolBar());
		panelBundle = "viewAbout";
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JPanel headerPanel = new JPanel(new MigLayout("", "", "[][][][][][][85%]"));

		JLabel logoLabel =
		  new JLabel(getResourceMap().getIcon("viewAbout.Panel.logo"));
		JLabel titleLabel =
		  new JLabel(getResourceMap().getString("Application.title") + " v."
		    + getResourceMap().getString("Application.version"));
		titleLabel.setFont(FontProvider.TITLE_BIG);
		JLabel subtitleLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.description"));
		subtitleLabel.setFont(FontProvider.TITLE_SMALL);

		JLabel authorsLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.authors"));
		JTextArea authorsTextArea =
		  new JTextArea(getResourceMap()
		    .getString("viewAbout.Panel.authorsContent"));
		authorsTextArea.setOpaque(false);
		authorsTextArea.setEditable(false);

		/* Indirizzo web */
		JLabel webLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.web"));
		String web = getResourceMap().getString("Application.homepage");
		JEditorPane webContent =
		  new JEditorPane("text/html", "<a href='" + web + "'>" + web + "</a>");
		webContent.setEditable(false);
		webContent.setOpaque(false);
		webContent.addHyperlinkListener(new HyperlinkListener()
			{
				@Override
				public void hyperlinkUpdate(HyperlinkEvent hle)
				{
					if(HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType()))
					{
						try
						{
							utilities.browse(hle.getURL().toURI());
						}
						catch(URISyntaxException e)
						{
							logger.getLogger().log(Level.WARNING,
							  getResourceMap().getString("ErrorMessage.cannotOpenURL"), e);
						}
					}
				}
			});

		/* Indirizzo mail */
		JLabel mailLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.mail"));
		String mail = getResourceMap().getString("Application.email");
		JEditorPane mailContent =
		  new JEditorPane("text/html", "<a href='mailto:" + mail
		    + "?Subject=Greentone%20"
		    + getResourceMap().getString("Application.version") + "'>" + mail
		    + "</a>");
		mailContent.setEditable(false);
		mailContent.setOpaque(false);
		mailContent.addHyperlinkListener(new HyperlinkListener()
			{
				@Override
				public void hyperlinkUpdate(HyperlinkEvent hle)
				{
					if(HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType()))
					{
						try
						{
							utilities.browse(hle.getURL().toURI());
						}
						catch(URISyntaxException e)
						{
							logger.getLogger().log(Level.WARNING,
							  getResourceMap().getString("ErrorMessage.cannotOpenURL"), e);
						}
					}
				}
			});

		/* Manuale utente */
		JLabel manualLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.manual"));
		final File manualFile =
		  new File(GreenToneAppConfig.MANUAL_REPOSITORY
		    + "GreenTone-ManualeUtente.pdf");
		JEditorPane manualPathField = new JEditorPane();
		manualPathField.setText(manualFile.getName());
		manualPathField.setForeground(Color.blue);

		manualPathField.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					new SwingWorker<Void, Void>()
						{

							@Override
							protected Void doInBackground() throws Exception
							{
								utilities.open(manualFile);
								return null;
							}
						}.execute();
				};
			});

		JLabel licenseLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.license"));
		final JTextArea licenseTextArea = new JTextArea();
		licenseTextArea.setEditable(false);
		licenseTextArea.setFont(FontProvider.CODE);
		String licenceURL =
		  "/" + getResourceMap().getResourcesDir() + "license.txt";
		try
		{
			InputStream inputStream = getClass().getResourceAsStream(licenceURL);
			licenseTextArea.read(new InputStreamReader(inputStream), null);
		}
		catch(Exception e)
		{
			logger.getLogger().log(
			  Level.WARNING,
			  getResourceMap().getString("ErrorMessage.cannotOpenURL") + " "
			    + licenceURL, e);
		}
		final JScrollPane scrollPane = new JScrollPane(licenseTextArea);

		/* assemblo il pannello */
		headerPanel.add(logoLabel, "spany 2");
		headerPanel.add(titleLabel, "span");
		headerPanel.add(subtitleLabel, "wrap");

		headerPanel.add(authorsLabel);
		headerPanel.add(authorsTextArea, "wrap");

		headerPanel.add(webLabel);
		headerPanel.add(webContent, "wrap");

		headerPanel.add(mailLabel);
		headerPanel.add(mailContent, "wrap");

		headerPanel.add(manualLabel);
		headerPanel.add(manualPathField, "grow, wrap");

		headerPanel.add(licenseLabel);
		headerPanel.add(scrollPane, "grow");

		return headerPanel;
	}

	@Override
	protected JXTable createContentTable()
	{
		return null;
	}

	@Override
	public void setup()
	{
		return;
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}

}
