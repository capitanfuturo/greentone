package it.greentone.gui.action;

import it.greentone.gui.dialog.CalculatorDialog;
import it.greentone.gui.panel.OperationsPanel;

import javax.inject.Inject;

import org.jdesktop.application.Action;
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
 * Visualizza la calcolatrice dell'applicazione.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ViewCalcAction {
    @Inject
    private CalculatorDialog calcDialog;
    @Inject
    private OperationsPanel operationsPanel;

    /**
     * Visualizza il pannello sulle informazioni dell'applicazione.
     */
    @Action
    public void viewCalc() {
        String amount = operationsPanel.getAmountTextField().getText();
        calcDialog.setup(amount);
        calcDialog.setVisible(true);
    }
}
