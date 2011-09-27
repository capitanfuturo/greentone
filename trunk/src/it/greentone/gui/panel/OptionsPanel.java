package it.greentone.gui.panel;

import it.greentone.ConfigurationProperties;
import it.greentone.gui.ContextualPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
 * Pannello delle configurazioni utente.
 * 
 * @author Giuseppe Caliendo
 */
@Component
@SuppressWarnings("serial")
public class OptionsPanel extends ContextualPanel<Void>
{
	@Inject
	private ConfigurationProperties properties;
	private final String panelBundle;
	private JPanel systemPanel;
	private JCheckBox checkUpdateCheckBox;

	/**
	 * Pannello delle configurazioni utente.
	 */
	public OptionsPanel()
	{
		super();
		remove(getContextualToolBar());
		panelBundle = "viewOptions";
	}

	@Override
	public String getBundleName()
	{
		return panelBundle;
	}

	@Override
	protected JXTable createContentTable()
	{
		return null;
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(getSystemPanel(), BorderLayout.CENTER);
		return headerPanel;
	}

	@Override
	public void setup()
	{
		super.setup();
		getCheckUpdateCheckBox().setSelected(properties.isCheckUpdateActivated());
	}

	private JPanel getSystemPanel()
	{
		if(systemPanel == null)
		{
			systemPanel = new JPanel(new MigLayout());
			systemPanel.setBorder(BorderFactory.createTitledBorder(getResourceMap()
			  .getString("viewOptions.Panel.systemTitle")));
			JLabel checkUpdateLabel =
			  new JLabel(getResourceMap().getString("viewOptions.Panel.checkUpdate"));
			systemPanel.add(checkUpdateLabel);
			systemPanel.add(getCheckUpdateCheckBox());
		}
		return systemPanel;
	}

	private JCheckBox getCheckUpdateCheckBox()
	{
		if(checkUpdateCheckBox == null)
		{
			checkUpdateCheckBox = new JCheckBox();
			checkUpdateCheckBox.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						properties.setCheckUpdateActivated(checkUpdateCheckBox.isSelected());
					}
				});
		}
		return checkUpdateCheckBox;
	}
}
