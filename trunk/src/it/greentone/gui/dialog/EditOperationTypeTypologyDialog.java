package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.persistence.OperationTypeTypology;
import it.greentone.persistence.OperationTypeTypologyService;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.swing.EventListModel;

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
 * Finestra di dialogo per la gestione delle tipologie dei tipi di operazione.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class EditOperationTypeTypologyDialog extends JDialog
{
	@Inject
	OperationTypeTypologyService operationTypeTypologyService;
	private static final String LOCALIZATION_PREFIX =
	  "editOperationTypeTypology.Dialog.";
	private final ResourceMap resourceMap;
	private JTextField inputTextField;
	private JList operationTypeTypologyJList;
	private JButton addButton;
	private JButton deleteButton;

	/**
	 * Finestra di dialogo per la gestione delle categorie degli incarichi.
	 */
	public EditOperationTypeTypologyDialog()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		setIconImage(resourceMap.getImageIcon("Application.icon").getImage());

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(resourceMap.getString(LOCALIZATION_PREFIX + "title"));
		setLayout(new BorderLayout());

		JPanel controlPanel = new JPanel(new MigLayout("flowy, fillx"));
		controlPanel.add(getAddButton(), "growx");
		controlPanel.add(getDeleteButton(), "growx");

		JPanel dataPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new MigLayout());
		inputPanel.add(new JLabel(resourceMap.getString(LOCALIZATION_PREFIX
		  + "name")));
		inputPanel.add(getInputTextField());
		dataPanel.add(inputPanel, BorderLayout.NORTH);
		dataPanel.add(new JScrollPane(getOperationTypeTypologyJList()),
		  BorderLayout.CENTER);
		getContentPane().add(dataPanel, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.EAST);

		setLocationRelativeTo(null);
		pack();
	}

	protected JTextField getInputTextField()
	{
		if(inputTextField == null)
			inputTextField = new JTextField(10);
		return inputTextField;
	}

	protected JList getOperationTypeTypologyJList()
	{
		if(operationTypeTypologyJList == null)
		{
			operationTypeTypologyJList = new JList();
			operationTypeTypologyJList
			  .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return operationTypeTypologyJList;
	}

	protected JButton getAddButton()
	{
		if(addButton == null)
		{
			addButton = new JButton(new AbstractAction()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e)
					{
						String name = GreenToneUtilities.getText(getInputTextField());
						if(name != null)
						{
							OperationTypeTypology operationTypeTypology =
							  new OperationTypeTypology();
							operationTypeTypology.setName(name);
							operationTypeTypologyService
							  .addOperationTypeTypology(operationTypeTypology);
							getInputTextField().setText(null);
						}
					}
				});
			addButton.setToolTipText(resourceMap.getString(LOCALIZATION_PREFIX
			  + "addToolTip"));
			addButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX + "addIcon"));
		}
		return addButton;
	}

	protected JButton getDeleteButton()
	{
		if(deleteButton == null)
		{
			deleteButton = new JButton(new AbstractAction()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e)
					{
						int selectedIndex =
						  getOperationTypeTypologyJList().getSelectedIndex();
						if(selectedIndex > -1)
						{
							OperationTypeTypology operationTypeTypology =
							  operationTypeTypologyService.getAllOperationTypeTypologies()
							    .get(selectedIndex);
							operationTypeTypologyService
							  .deleteOperationTypeTypology(operationTypeTypology);
						}
					}
				});
			deleteButton.setToolTipText(resourceMap.getString(LOCALIZATION_PREFIX
			  + "deleteToolTip"));
			deleteButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX
			  + "deleteIcon"));
		}
		return deleteButton;
	}

	/**
	 * Configura la finestra di dialogo prima che venga visualizzata.
	 */
	public void setup()
	{
		ListModel listModel =
		  new EventListModel<OperationTypeTypology>(
		    operationTypeTypologyService.getAllOperationTypeTypologies());
		getOperationTypeTypologyJList().setModel(listModel);
	}

}
