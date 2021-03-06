package it.greentone.gui.action;

import it.greentone.gui.dialog.EditJobStakeholderDialog;

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
 * Visualizza una finestra di dialogo per la gestione delle persone interessate ad un incarico.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class EditJobStakeholderAction extends AbstractBean {
    @Inject
    EditJobStakeholderDialog editJobStakeholderDialog;

    /**
     * Visualizza una finestra di dialogo per la gestione delle persone interessate ad un incarico.
     */
    @Action
    public void editJobStakeholder() {
        editJobStakeholderDialog.setVisible(true);
    }
}
