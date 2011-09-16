package it.greentone.gui.panel;

import it.greentone.gui.ContextualPanel;
import it.greentone.gui.FontProvider;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
		JPanel headerPanel = new JPanel(new MigLayout());
		JLabel logoLabel =
		  new JLabel(getResourceMap().getIcon("viewAbout.Panel.logo"));
		headerPanel.add(logoLabel, "spany 2");
		JLabel titleLabel =
		  new JLabel(getResourceMap().getString("Application.title") + " v."
		    + getResourceMap().getString("Application.version"));
		titleLabel.setFont(FontProvider.TITLE_BIG);
		headerPanel.add(titleLabel, "span");
		JLabel subtitleLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.description"));
		subtitleLabel.setFont(FontProvider.TITLE_SMALL);
		headerPanel.add(subtitleLabel, "wrap");

		JLabel authorsLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.authors"));
		headerPanel.add(authorsLabel);
		JTextArea authorsTextArea =
		  new JTextArea(getResourceMap()
		    .getString("viewAbout.Panel.authorsContent"));
		authorsTextArea.setOpaque(false);
		authorsTextArea.setEditable(false);
		headerPanel.add(authorsTextArea, "wrap");

		/* Indirizzo web */
		JLabel webLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.web"));
		headerPanel.add(webLabel);
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
							open(hle.getURL().toURI());
						}
						catch(URISyntaxException e)
						{
							e.printStackTrace();
						}
					}
				}
			});
		headerPanel.add(webContent, "wrap");

		/* Indirizzo mail */
		JLabel mailLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.mail"));
		headerPanel.add(mailLabel);
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
							open(hle.getURL().toURI());
						}
						catch(URISyntaxException e)
						{
							e.printStackTrace();
						}
					}
				}
			});
		headerPanel.add(mailContent, "wrap");

		JLabel licenseLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.license"));
		headerPanel.add(licenseLabel);
		JTextArea licenceTextArea = new JTextArea(50, 100);
		licenceTextArea.setEditable(false);
		licenceTextArea.setFont(FontProvider.CODE);
		licenceTextArea.setLineWrap(true);
		licenceTextArea.setWrapStyleWord(true);
		try
		{
			String licenceURL =
			  "/" + getResourceMap().getResourcesDir() + "license.txt";
			InputStream inputStream = getClass().getResourceAsStream(licenceURL);
			licenceTextArea.read(new InputStreamReader(inputStream), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		headerPanel.add(new JScrollPane(licenceTextArea), "shrinkx");
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

	/**
	 * Apre un indirizzo di risorsa.
	 * 
	 * @param uri
	 *          indirizzo di risorsa
	 */
	private static void open(URI uri)
	{
		if(Desktop.isDesktopSupported())
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				desktop.browse(uri);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
