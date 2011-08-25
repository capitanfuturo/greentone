package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.persistence.OperationTypeService;
import it.greentone.persistence.OperationTypeTypologyService;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Finestra di dialogo per la gestione dei tipi di operazione.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class EditOperationTypeDialog extends JDialog
{
	@Inject
	OperationTypeService operationTypeService;
	@Inject
	OperationTypeTypologyService operationTypeTypologyService;
	private static final String LOCALIZATION_PREFIX = "editOperationType.Dialog.";
	private final ResourceMap resourceMap;
	private JButton addButton;
	private JButton deleteButton;

	private JComboBox typologyComboBox;
	private JTextField nameTextField;
	private JCheckBox activeCheckBox;
	private JCheckBox taxableCheckBox;
	private JXTable typeTable;

	/**
	 * Finestra di dialogo per la gestione dei tipi di operazione.
	 */
	public EditOperationTypeDialog()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(resourceMap.getString(LOCALIZATION_PREFIX + "title"));
		setLayout(new BorderLayout());

		JPanel controlPanel = new JPanel(new MigLayout("flowy, fillx"));
		controlPanel.add(getAddButton(), "growx");
		controlPanel.add(getDeleteButton(), "growx");

		JPanel dataPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new MigLayout());
		inputPanel.add(
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "typology")),
		  "gap para");
		inputPanel.add(getTypologyComboBox(), "wrap");
		inputPanel.add(
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "name")),
		  "gap para");
		inputPanel.add(getNameTextField(), "wrap");
		inputPanel.add(
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "active")),
		  "gap para");
		inputPanel.add(getActiveCheckBox(), "wrap");
		inputPanel.add(
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "taxable")),
		  "gap para");
		inputPanel.add(getTaxableCheckBox(), "wrap");
		dataPanel.add(inputPanel, BorderLayout.NORTH);
		dataPanel.add(new JScrollPane(getTypeTable()), BorderLayout.CENTER);
		getContentPane().add(dataPanel, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.EAST);

		setLocationRelativeTo(null);
		pack();
	}

	protected JComboBox getTypologyComboBox()
	{
		if(typologyComboBox == null)
			typologyComboBox = new JComboBox();
		return typologyComboBox;
	}

	protected JTextField getNameTextField()
	{
		if(nameTextField == null)
			nameTextField = new JTextField(15);
		return nameTextField;
	}

	protected JCheckBox getActiveCheckBox()
	{
		if(activeCheckBox == null)
			activeCheckBox = new JCheckBox();
		return activeCheckBox;
	}

	protected JCheckBox getTaxableCheckBox()
	{
		if(taxableCheckBox == null)
			taxableCheckBox = new JCheckBox();
		return taxableCheckBox;
	}
	
	protected JXTable getTypeTable()
  {
		if(typeTable==null)typeTable=new JXTable();
	  return typeTable;
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

					}
				});
			deleteButton.setToolTipText(resourceMap.getString(LOCALIZATION_PREFIX
			  + "deleteToolTip"));
			deleteButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX
			  + "deleteIcon"));
		}
		return deleteButton;
	}

	public void setup()
	{

	}

}
