package it.greentone.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JSplitPane;
import javax.swing.UIManager;

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
 * Estensione dello split standard che ovvia al problema di impostazione della posizione del divider. Vedi
 * http://stackoverflow.com/questions/1879091/jsplitpane -setdividerlocation-problem
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public class JXSplitPane extends JSplitPane {
    private boolean hasProportionalLocation = false;
    private double proportionalLocation = 0.5;
    private boolean isPainted = false;

    /**
     * Crea un {@link JSplitPane}
     */
    public JXSplitPane() {
        super();
    }

    /**
     * Crea un {@link JSplitPane} impostando l'orientamento del componente
     * 
     * @param newOrientation
     *            l'orientamento del componente
     */
    public JXSplitPane(int newOrientation) {
        super(newOrientation);
    }

    /**
     * Crea un {@link JSplitPane} impostando l'orientamento del componente e i componenti stessi
     * 
     * @param newOrientation
     *            orientamento del componente
     * @param newLeftComponent
     *            componente di sinistra / in alto
     * @param newRightComponent
     *            componente di destra / in basso
     */
    public JXSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, UIManager.getBoolean("SplitPane.continuousLayout"), newLeftComponent, newRightComponent);
    }

    @Override
    public void setDividerLocation(double proportionalLocation) {
        if (!isPainted) {
            hasProportionalLocation = true;
            this.proportionalLocation = proportionalLocation;
        } else {
            super.setDividerLocation(proportionalLocation);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!isPainted) {
            if (hasProportionalLocation) {
                super.setDividerLocation(proportionalLocation);
            }
            isPainted = true;
        }
    }

}
