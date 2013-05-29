package it.greentone.gui;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.action.ActionProvider;

import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.joda.time.DateTime;
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
 * </code> <br>
 * <br>
 * Pannello principale dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class MainPanel extends JPanel {
    private JToolBar mainToolBar;
    @Inject
    private ActionProvider actionProvider;
    private JTabbedPane mainTabbedPane;
    private JLabel statusLabel;
    private ResourceMap resourceMap;
    private String applicationName;

    /**
     * Inizializza l'interfaccia grafica.
     */
    public void initialize() {
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
        applicationName = resourceMap.getString("Application.title");

        setLayout(new BorderLayout());
        add(getMainToolBar(), BorderLayout.WEST);
        add(getMainTabbedPane(), BorderLayout.CENTER);

        DateTime now = new DateTime();
        String today = GreenToneUtilities.formatDateTime(now);
        String appName = resourceMap.getString("Application.name");
        String version = resourceMap.getString("Application.version");
        String ready = resourceMap.getString("MainPanel.statusBarReady");

        statusLabel = new JLabel(appName + " " + version + " - " + today + " " + ready);
        add(statusLabel, BorderLayout.PAGE_END);
    }

    /**
     * Restituisce la barra laterale con la barra dei bottoni principale.
     * 
     * @return la barra laterale con la barra dei bottoni principale
     */
    public JToolBar getMainToolBar() {
        if (mainToolBar == null) {
            mainToolBar = new JToolBar();
            mainToolBar.setFloatable(false);
            mainToolBar.add(actionProvider.getViewHome());
            mainToolBar.add(actionProvider.getViewJobs());
            mainToolBar.add(actionProvider.getViewOperations());
            mainToolBar.add(actionProvider.getViewDocuments());
            mainToolBar.add(actionProvider.getViewPersons());
            mainToolBar.add(actionProvider.getViewOptions());
            mainToolBar.add(actionProvider.getExit());
            mainToolBar.add(actionProvider.getViewAbout());
            mainToolBar.setOrientation(SwingConstants.VERTICAL);
        }
        return mainToolBar;
    }

    /**
     * Restituisce la tabbed pane che contiene le schede aperte dell'applicazione.
     * 
     * @return la tabbed pane che contiene le schede aperte dell'applicazione
     */
    public JTabbedPane getMainTabbedPane() {
        if (mainTabbedPane == null) {
            mainTabbedPane = new DnDTabbedPane();
            mainTabbedPane.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    /* aggiorno il titolo dell'applicazione */
                    int selectedIndex = mainTabbedPane.getSelectedIndex();
                    String title = mainTabbedPane.getTitleAt(selectedIndex);
                    Application.getInstance(GreenTone.class).getMainFrame().setTitle(title + " - " + applicationName);
                }
            });
        }
        return mainTabbedPane;
    }

    /**
     * Restituisce l'etichetta di stato.
     * 
     * @return l'etichetta di stato
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
