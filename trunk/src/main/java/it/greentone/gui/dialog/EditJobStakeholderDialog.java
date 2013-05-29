package it.greentone.gui.dialog;

import it.greentone.GreenTone;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Finestra di dialogo per la gestione delle persone interessate ad un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class EditJobStakeholderDialog extends JDialog {
    private static final String LOCALIZATION_PREFIX = "editJobStakeholder.Dialog.";
    private final ResourceMap resourceMap;

    private JList canditatePersonList;
    private JList selectedPersonList;
    private JButton addToListButton;
    private JButton removeFromListButton;

    /**
     * Finestra di dialogo per la gestione delle persone interessate ad un incarico.
     */
    public EditJobStakeholderDialog() {
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
        setIconImage(resourceMap.getImageIcon("Application.icon").getImage());

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(resourceMap.getString(LOCALIZATION_PREFIX + "title"));
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new MigLayout());
        buttonPanel.add(new JLabel(), "wrap");
        buttonPanel.add(getAddToListButton(), "wrap");
        buttonPanel.add(new JLabel(), "wrap");
        buttonPanel.add(getRemoveFromListButton(), "wrap");
        buttonPanel.add(new JLabel(), "wrap");

        getContentPane().add(new JScrollPane(getCanditatePersonList()), BorderLayout.WEST);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(new JScrollPane(getSelectedPersonList()), BorderLayout.EAST);

        setLocationRelativeTo(null);
        pack();
    }

    protected JList getCanditatePersonList() {
        if (canditatePersonList == null)
            canditatePersonList = new JList();
        return canditatePersonList;
    }

    protected JList getSelectedPersonList() {
        if (selectedPersonList == null)
            selectedPersonList = new JList();
        return selectedPersonList;
    }

    protected JButton getAddToListButton() {
        if (addToListButton == null) {
            addToListButton = new JButton();
            addToListButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX + "addToListIcon"));
        }
        return addToListButton;
    }

    protected JButton getRemoveFromListButton() {
        if (removeFromListButton == null) {
            removeFromListButton = new JButton();
            removeFromListButton.setIcon(resourceMap.getIcon(LOCALIZATION_PREFIX + "removeFromListIcon"));
        }
        return removeFromListButton;
    }
}
