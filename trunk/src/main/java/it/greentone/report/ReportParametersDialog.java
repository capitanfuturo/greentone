package it.greentone.report;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2013 GreenTone Developer Team.<br>
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
 * Finestra di dialogo per l'impostazione dei parametri del report da stampare.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public abstract class ReportParametersDialog extends JDialog {
    private final ResourceMap resourceMap;
    private JButton okButton;
    private JButton cancelButton;
    private final HashMap<String, Object> parametersMap;

    /**
     * Finestra di dialogo per l'impostazione dei parametri del report da stampare.
     */
    public ReportParametersDialog() {
        super();
        parametersMap = new HashMap<String, Object>();

        setModal(true);
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
        setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
        setTitle(resourceMap.getString("viewReports.Dialog.title"));

        setLayout(new BorderLayout());

        JPanel parametersPanel = new JPanel(new MigLayout("fill"));
        parametersPanel.add(new JLabel(resourceMap.getString("viewReports.Dialog.reports")), "wrap");
        parametersPanel.add(new JScrollPane(getParametersPanel()), "grow");

        JPanel buttonPanel = new JPanel(new MigLayout("rtl"));
        buttonPanel.add(getOkButton());
        buttonPanel.add(getCancelButton());

        getContentPane().add(parametersPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setMinimumSize(GreenToneUtilities.DIALOG_SIZE);
        setLocationRelativeTo(null);
    }

    /**
     * Restitusice il pannello contenente la finestra di dialogo dei parametri.
     * 
     * @return il pannello contenente la finestra di dialogo dei parametri
     */
    protected abstract JPanel getParametersPanel();

    /**
     * Restituisce il titolo da dare al pannello di recupero dei parametri del report.
     * 
     * @return il titolo da dare al pannello di recupero dei parametri del report
     */
    protected abstract String getParametersPanelTitle();

    JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton(resourceMap.getString("viewReports.Dialog.ok"));
        }
        return okButton;
    }

    JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton(resourceMap.getString("viewReports.Dialog.cancel"));
        }
        return cancelButton;
    }

    Map<String, Object> getOutputParameters() {
        return parametersMap;
    }
}
