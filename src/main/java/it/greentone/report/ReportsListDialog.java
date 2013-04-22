package it.greentone.report;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.report.group.ReportGroup;
import it.greentone.report.impl.AbstractReportImpl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.springframework.stereotype.Component;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2012 GreenTone Developer Team.<br>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * </code> <br>
 * <br>
 * Finestra di dialogo per la selezione del report da stampare.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class ReportsListDialog extends JDialog {
    private final ResourceMap resourceMap;
    private List<AbstractReportImpl> reportDescriptorList;
    private AbstractReportImpl selectedReportDescriptor;
    private JList reportsList;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Finestra di dialogo per la selezione del report da stampare.
     */
    public ReportsListDialog() {
        super();
        setModal(true);
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
        setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
        setTitle(resourceMap.getString("viewReports.Dialog.title"));

        setLayout(new BorderLayout());

        JPanel listContent = new JPanel(new MigLayout("fill"));
        listContent.add(new JLabel(resourceMap.getString("viewReports.Dialog.reports")), "wrap");
        listContent.add(new JScrollPane(getReportsList()), "grow");

        JPanel buttonPanel = new JPanel(new MigLayout("rtl"));
        buttonPanel.add(getOkButton());
        buttonPanel.add(getCancelButton());

        getContentPane().add(listContent, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setMinimumSize(GreenToneUtilities.DIALOG_SIZE);
        setLocationRelativeTo(null);
    }

    private JList getReportsList() {
        if (reportsList == null) {
            reportsList = new JList();
            reportsList.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        getOkButton().setEnabled(e.getSource() != null);
                    }
                }
            });
        }
        return reportsList;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int selectedIndex = getReportsList().getSelectedIndex();
                    if (selectedIndex > -1) {
                        selectedReportDescriptor = (AbstractReportImpl) getReportsList().getSelectedValue();
                    }
                    setVisible(false);
                }
            });
            okButton.setEnabled(false);
            okButton.setText(resourceMap.getString("viewReports.Dialog.ok"));
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
    public AbstractReportImpl getSelectedReportDescriptor() {
        return selectedReportDescriptor;
    }

    /**
     * Imposta la dialog prima di essere utilizzata.
     * 
     * @param group
     *            gruppo di report da visualizzare
     */
    public void setup(ReportGroup group) {
        reportDescriptorList = (List<AbstractReportImpl>) group.getReports();
        Collections.sort(reportDescriptorList, new Comparator<AbstractReportImpl>() {

            @Override
            public int compare(AbstractReportImpl o1, AbstractReportImpl o2) {
                return o1.getDescriptor().getName().compareToIgnoreCase(o2.getDescriptor().getName());
            }
        });

        ListModel listModel = new ListComboBoxModel<AbstractReportImpl>(reportDescriptorList);
        getReportsList().setModel(listModel);
        pack();
    }
}
