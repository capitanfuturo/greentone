package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.persistence.JobCategory;
import it.greentone.persistence.JobCategoryService;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
 * Finestra di dialogo per la gestione delle categorie degli incarichi.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class EditJobCategoryDialog extends JDialog
{
	@Inject
	JobCategoryService jobCategoryService;
	private static final String LOCALIZATION_PREFIX = "editJobCategory.Dialog.";
	private final ResourceMap resourceMap;
	private JTextField inputTextField;
	private JList jobCategoryJList;
	private JButton addButton;
	private JButton deleteButton;
	private JButton okButton;

	/**
	 * Finestra di dialogo per la gestione delle categorie degli incarichi.
	 */
	public EditJobCategoryDialog()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		setIconImage(resourceMap.getImageIcon("Application.icon").getImage());

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(resourceMap.getString(LOCALIZATION_PREFIX + "title"));
		setLayout(new BorderLayout());


		JPanel northPanel = new JPanel(new MigLayout());
		northPanel.add(new JLabel(resourceMap.getString(LOCALIZATION_PREFIX
		  + "name")));
		northPanel.add(getInputTextField());
		northPanel.add(getAddButton());

		JPanel centerPanel = new JPanel(new MigLayout("", "[90%][]", ""));
		centerPanel.add(
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "categories")),
		  "wrap");
		centerPanel.add(new JScrollPane(getJobCategoryJList()), "grow");
		centerPanel.add(getDeleteButton());

		JPanel buttonPanel = new JPanel(new MigLayout("rtl"));
		buttonPanel.add(getOkButton());

		getContentPane().add(northPanel, BorderLayout.NORTH);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		pack();
	}

	protected JTextField getInputTextField()
	{
		if(inputTextField == null)
			inputTextField = new JTextField(10);
		return inputTextField;
	}

	protected JList getJobCategoryJList()
	{
		if(jobCategoryJList == null)
		{
			jobCategoryJList = new JList();
			jobCategoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jobCategoryJList;
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
							JobCategory jobCategory = new JobCategory();
							jobCategory.setName(name);
							jobCategoryService.addJobCategory(jobCategory);
							getInputTextField().setText(null);
						}
					}
				});
			getInputTextField().getDocument().addDocumentListener(
			  new DocumentListener()
				  {

					  @Override
					  public void removeUpdate(DocumentEvent e)
					  {
						  toggleButton();
					  }

					  @Override
					  public void insertUpdate(DocumentEvent e)
					  {
						  toggleButton();
					  }

					  @Override
					  public void changedUpdate(DocumentEvent e)
					  {
						  toggleButton();
					  }

					  void toggleButton()
					  {
						  addButton.setEnabled(GreenToneUtilities
						    .getText(getInputTextField()) != null);
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
						int selectedIndex = getJobCategoryJList().getSelectedIndex();
						if(selectedIndex > -1)
						{
							int confirmDialog =
							  JOptionPane.showConfirmDialog(getContentPane(), resourceMap
							    .getString("editJobCategory.Action.confirmMessage"));
							if(confirmDialog == JOptionPane.OK_OPTION)
							{
								JobCategory jobCategory =
								  jobCategoryService.getAllJobCategories().get(selectedIndex);
								jobCategoryService.deleteJobCategory(jobCategory);
							}
						}
					}
				});
			getJobCategoryJList().addListSelectionListener(
			  new ListSelectionListener()
				  {

					  @Override
					  public void valueChanged(ListSelectionEvent e)
					  {
						  deleteButton
						    .setEnabled(getJobCategoryJList().getSelectedIndex() > -1);
					  }
				  });

			deleteButton.setToolTipText(resourceMap.getString(LOCALIZATION_PREFIX
			  + "deleteToolTip"));
			deleteButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX
			  + "deleteIcon"));
		}
		return deleteButton;
	}

	private JButton getOkButton()
	{
		if(okButton == null)
		{
			okButton = new JButton(new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						setVisible(false);
					}
				});
			okButton.setText(resourceMap.getString(LOCALIZATION_PREFIX + "okButton"));
		}
		return okButton;
	}

	/**
	 * Configura la finestra di dialogo prima che venga visualizzata.
	 */
	public void setup()
	{
		ListModel listModel =
		  new EventListModel<JobCategory>(jobCategoryService.getAllJobCategories());
		getJobCategoryJList().setModel(listModel);
		getInputTextField().setText(null);
		getDeleteButton().setEnabled(false);
		getAddButton().setEnabled(false);
	}

}
