package it.greentone.report;

import it.greentone.GreenTone;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
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
 * Finestra di dialogo per la selezione del report da stampare.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class ReportsListDialog extends JDialog
{
	private final ResourceMap resourceMap;
	private List<ReportDescriptorInterface> reportDescriptorList;
	private ReportDescriptorInterface selectedReportDescriptor;
	private JList reportsList;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Finestra di dialogo per la selezione del report da stampare.
	 */
	public ReportsListDialog()
	{
		super();
		setModal(true);
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
		setTitle(resourceMap.getString("viewReports.Dialog.title"));

		setLayout(new BorderLayout());

		JPanel listContent = new JPanel(new MigLayout());
		listContent.add(
		  new JLabel(resourceMap.getString("viewReports.Dialog.reports")), "wrap");
		listContent.add(new JScrollPane(getReportsList()), "grow");

		JPanel buttonPanel = new JPanel(new MigLayout("rtl"));
		buttonPanel.add(getOkButton());
		buttonPanel.add(getCancelButton());

		getContentPane().add(listContent, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
	}

	private JList getReportsList()
	{
		if(reportsList == null)
		{
			reportsList = new JList();
		}
		return reportsList;
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
						int selectedIndex = getReportsList().getSelectedIndex();
						if(selectedIndex > -1)
						{
							selectedReportDescriptor =
							  (ReportDescriptorInterface) getReportsList().getSelectedValue();
						}
						setVisible(false);
					}
				});
			okButton.setText(resourceMap.getString("viewReports.Dialog.ok"));
		}
		return okButton;
	}

	private JButton getCancelButton()
	{
		if(cancelButton == null)
		{
			cancelButton = new JButton(new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						selectedReportDescriptor = null;
						setVisible(false);
					}
				});
			cancelButton.setText(resourceMap.getString("viewReports.Dialog.cancel"));
		}
		return cancelButton;
	}

	/**
	 * Restituisce il report selezionato.
	 * 
	 * @return il report selezionato
	 */
	public ReportDescriptorInterface getSelectedReportDescriptor()
	{
		return selectedReportDescriptor;
	}

	/**
	 * Imposta la dialog prima di essere utilizzata.
	 * 
	 * @param category
	 */
	public void setup(ReportsCategoryInterface category)
	{
		reportDescriptorList = category.getDescriptors();
		ListModel listModel =
		  new ListComboBoxModel<ReportDescriptorInterface>(reportDescriptorList);
		reportsList.setModel(listModel);
		pack();
	}
}
