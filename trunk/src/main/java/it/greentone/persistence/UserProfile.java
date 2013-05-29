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
 * Profili applicativi. A seconda della profilazione di un utente appartenente all'anagrafica il programma consentirà
 * l'utilizzo o meno di alcune funzionalità.
 * 
 * @author Giuseppe Caliendo
 */
public enum UserProfile {
    /** Profilo amministrativo */
    ADMINISTRATOR {
        @Override
        public String getLocalizationKey() {
            return "Enum.UserProfile.Administrator";
        }
    },
    /** Geometra */
    SURVEYOUR {
        @Override
        public String getLocalizationKey() {
            return "Enum.UserProfile.Surveyour";
        }
    },
    /** Ospite */
    GUEST {
        @Override
        public String getLocalizationKey() {
            return "Enum.UserProfile.Guest";
        }
    };

    ResourceMap resourceMap;

    private UserProfile() {
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
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
