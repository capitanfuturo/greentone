package it.greentone.gui.action;

import it.greentone.gui.dialog.EditUserDialog;
import it.greentone.gui.panel.PersonsPanel;
import it.greentone.persistence.Person;

import javax.inject.Inject;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
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
 * Visualizza la finestra di dialogo per la gestione dell'account dell'anagrafica selezionata.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class EditUserAction extends AbstractBean {
    @Inject
    private PersonsPanel personsPanel;
    @Inject
    private EditUserDialog editUserDialog;
    boolean editUserActionEnabled = false;

    /**
     * Visualizza la finestra di dialogo per la gestione dell'account dell'anagrafica selezionata.
     */
    @Action(enabledProperty = "editUserActionEnabled")
    public void editUser() {
        Person person = personsPanel.getSelectedItem();
        editUserDialog.setup(person);
        editUserDialog.setVisible(true);
    }

    /**
     * Restituisce <code>true</code> se è possibile abilitare l'azione, <code>false</code> altrimenti.
     * 
     * @return <code>true</code> se è possibile abilitare l'azione, <code>false</code> altrimenti
     */
    public boolean isEditUserActionEnabled() {
        return editUserActionEnabled;
    }

    /**
     * Imposta l'abilitazione dell'azione.
     * 
     * @param editUserActionEnabled
     *            <code>true</code> se si vuole abilitare l'azione, <code>false</code> altrimenti
     */
    public void setEditUserActionEnabled(boolean editUserActionEnabled) {
        final boolean oldValue = this.editUserActionEnabled;
        this.editUserActionEnabled = editUserActionEnabled;
        firePropertyChange("editUserActionEnabled", oldValue, editUserActionEnabled);
    }
}
