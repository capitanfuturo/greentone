package it.greentone.gui.panel;

import it.greentone.gui.action.ActionProvider;

import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

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
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class MainPanel extends JPanel
{
	private JToolBar mainToolBar;
	@Inject
	private ActionProvider actionProvider;
	private JTabbedPane mainTabbedPane;

	public void initialize()
	{
		setLayout(new BorderLayout());
		add(getMainToolBar(), BorderLayout.WEST);
		add(getMainTabbedPane(), BorderLayout.CENTER);
		JLabel statusLabel = new JLabel("New label");
		add(statusLabel, BorderLayout.PAGE_END);
	}

	public JToolBar getMainToolBar()
	{
		if(mainToolBar == null)
		{
			mainToolBar = new JToolBar();
			mainToolBar.setFloatable(false);
			mainToolBar.add(actionProvider.getViewJobs());
			mainToolBar.add(actionProvider.getViewOperations());
			mainToolBar.add(actionProvider.getViewDocuments());
			mainToolBar.add(actionProvider.getViewPersons());
			mainToolBar.add(actionProvider.getExit());
			mainToolBar.add(actionProvider.getViewAbout());
			mainToolBar.setOrientation(SwingConstants.VERTICAL);
		}
		return mainToolBar;
	}

	public JTabbedPane getMainTabbedPane()
	{
		if(mainTabbedPane == null)
		{
			mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		}
		return mainTabbedPane;
	}
}
