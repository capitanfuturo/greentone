package it.greentone.report.descriptor;

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
 * Descrive un report di GreenTone.
 * 
 * @author Giuseppe Caliendo
 */
public interface ReportDescriptor {
    /**
     * Restituisce il nome localizzato del report.
     * 
     * @return il nome localizzato del report.
     */
    String getName();

    /**
     * Restituisce la descrizione del report.
     * 
     * @return la descrizione del report.
     */
    String getDescription();

    /**
     * Restituisce l'estensione del report.
     * 
     * @return l'estensione del report
     */
    ExtensionType getExtensionType();

    /**
     * File supportati dal motore di reportistica.
     * 
     * @author Giuseppe Caliendo
     */
    public enum ExtensionType {
        /**
         * Portable Document Format
         */
        PDF {

            @Override
            public String getExtension() {
                return ".pdf";
            }

        },
        /** eXtensible Markup Language */
        XML {
            @Override
            public String getExtension() {
                return ".xml";
            }
        },
        /** HyperText Markup Language */
        HTML {
            @Override
            public String getExtension() {
                return ".html";
            }
        };

        /**
         * Restituisce l'estensione del report.
         * 
         * @return l'estensione del report
         */
        public abstract String getExtension();
    }
}
