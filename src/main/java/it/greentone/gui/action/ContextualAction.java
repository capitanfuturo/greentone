package it.greentone.gui.action;

import it.greentone.GreenTone;
import it.greentone.gui.ButtonTabComponent;
import it.greentone.gui.MainPanel;
import it.greentone.gui.panel.AbstractPanel;
import it.greentone.gui.panel.HomePanel;

import java.awt.Component;

import javax.swing.JTabbedPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

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
 * Azione legata alla visualizzazione di tipi di oggetti del database nel pannello principale dell'applicazione.
 * Aggiunge una scheda se non già presente nel pannello principale.
 * 
 * @author Giuseppe Caliendo
 */
public class ContextualAction {
    protected static final String PANEL_TITLE_SUFFIX = ".Panel.title";
    protected static final String ACTION_SMALL_ICON_SUFFIX = ".Action.smallIcon";
    protected static final String ACTION_TOOLTIP_SUFFIX = ".Action.shortDescription";

    /**
     * Aggiunge una scheda alla finestra principale.
     * 
     * @param mainPanel
     *            pannello al quale aggiungere il tab
     * @param panel
     *            contenuto della scheda
     */
    public static void addTab(MainPanel mainPanel, AbstractPanel panel) {
        ResourceMap resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();

        /* controllo che il tab non sia già presente */
        JTabbedPane tabbedPane = mainPanel.getMainTabbedPane();
        String title = resourceMap.getString(panel.getBundleName() + PANEL_TITLE_SUFFIX);

        boolean tabInserted = false;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component componentAt = tabbedPane.getComponentAt(i);
            if (componentAt.equals(panel)) {
                tabInserted = true;
            }
        }
        if (!tabInserted) {
            /* configuro e aggiungo il tab */
            panel.setup();
            /*
             * Issue 126: la pagina iniziale deve rimanere in prima posizione e non si può chiudere
             */
            if (panel instanceof HomePanel) {
                tabbedPane.insertTab(title, resourceMap.getIcon(panel.getBundleName() + ACTION_SMALL_ICON_SUFFIX),
                        panel, resourceMap.getString(panel.getBundleName() + ACTION_TOOLTIP_SUFFIX), 0);
            } else {
                tabbedPane.insertTab(title, resourceMap.getIcon(panel.getBundleName() + ACTION_SMALL_ICON_SUFFIX),
                        panel, resourceMap.getString(panel.getBundleName() + ACTION_TOOLTIP_SUFFIX), 1);
                tabbedPane.setTabComponentAt(1, new ButtonTabComponent(tabbedPane));
            }
        }
        tabbedPane.setSelectedComponent(panel);
    }
}
