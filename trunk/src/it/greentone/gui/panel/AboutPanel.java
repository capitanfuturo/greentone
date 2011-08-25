package it.greentone.gui.panel;

import it.greentone.gui.ContextualPanel;
import it.greentone.gui.FontProvider;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
public class AboutPanel extends ContextualPanel
{
	private final String panelTitle;

	/**
	 * Pannello delle informazioni sul programma.
	 */
	public AboutPanel()
	{
		super();
		remove(getContextualToolBar());
		panelTitle = getResourceMap().getString("viewAbout.Panel.title");
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
		headerPanel.add(subtitleLabel, "span");
		JLabel authorsLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.authors"));
		headerPanel.add(authorsLabel);
		JTextArea authorsTextArea =
		  new JTextArea(getResourceMap()
		    .getString("viewAbout.Panel.authorsContent"));
		authorsTextArea.setEditable(false);
		headerPanel.add(authorsTextArea, "span");
		JLabel licenseLabel =
		  new JLabel(getResourceMap().getString("viewAbout.Panel.license"));
		headerPanel.add(licenseLabel);
		JTextArea licenceTextArea = new JTextArea(50, 100);
		licenceTextArea.setEditable(false);
		licenceTextArea.setFont(FontProvider.CODE);
		try
		{
			InputStream inputStream =
			  getClass().getResourceAsStream(
			    "/" + getResourceMap().getResourcesDir() + "license.txt");
			licenceTextArea.read(new InputStreamReader(inputStream), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		headerPanel.add(new JScrollPane(licenceTextArea), "span");
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
	public String getPanelName()
	{
		return panelTitle;
	}
}
