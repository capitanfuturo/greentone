package it.greentone.persistence;

import it.greentone.GreenTone;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

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
 * Tipo di operazione:
 * <ul>
 * <li>Lavoro: Imponibile, Attivo, Imponibile</li>
 * <li>Spesa: Imponibile, Non attivo, Imponibile</li>
 * <li>Spesa non imponibile: Non imponibile, Non attivo, Non imponibile</li>
 * <li>Acconto spese: Acconto, Non attivo, non imponibile</li>
 * </ul>
 * 
 * @author Giuseppe Caliendo
 */
public enum OperationType {
    /** Lavoro */
    TASK(true, true) {
        @Override
        public String getLocalizationKey() {
            return "Enum.OperationType.Task";
        }
    },
    /** Spesa */
    EXPENSE(false, true) {
        @Override
        public String getLocalizationKey() {
            return "Enum.OperationType.Expense";
        }
    },
    /** Spesa non imponibile */
    NO_TAXABLE_EXPENSE(false, false) {
        @Override
        public String getLocalizationKey() {
            return "Enum.OperationType.NoTaxableExpense";
        }
    },
    /** Acconto di spesa */
    EXPENSE_DEPOSIT(false, false) {
        @Override
        public String getLocalizationKey() {
            return "Enum.OperationType.ExpenseDeposit";
        }
    };

    ResourceMap resourceMap;
    boolean isActive;
    boolean isTaxable;

    OperationType(boolean isActive, boolean isTaxable) {
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
        this.isActive = isActive;
        this.isTaxable = isTaxable;
    }

    /**
     * Restituisce <code>true</code> se il tipo di operazione è attivo, <code>false</code> altrimenti.
     * 
     * @return <code>true</code> se il tipo di operazione è attivo, <code>false</code> altrimenti
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Restituisce <code>true</code> se il tipo di operazione è imponibile, <code>false</code> altrimenti.
     * 
     * @return <code>true</code> se il tipo di operazione è imponibile, <code>false</code> altrimenti
     */
    public boolean isTaxable() {
        return isTaxable;
    }

    /**
     * Restituisce la chiave di localizzazione del valore dell'enumerato.
     * 
     * @return la chiave di localizzazione del valore dell'enumerato
     */
    public abstract String getLocalizationKey();

    /**
     * Restituisce il nome localizzato del valore dell'enumerato.
     * 
     * @return il nome localizzato del valore dell'enumerato
     */
    public String getLocalizedName() {
        return resourceMap.getString(getLocalizationKey());
    }
}
