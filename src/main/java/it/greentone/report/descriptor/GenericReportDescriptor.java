package it.greentone.report.descriptor;

import it.greentone.GreenTone;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Classe base di descrizione di un report in GreenTone.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class GenericReportDescriptor implements ReportDescriptor {

    private final ExtensionType type;
    private final ResourceMap resourceMap;
    private final String reportKey;

    /**
     * Classe base di descrizione di un report in GreenTone.
     * 
     * @param reportKey
     *            chiave del report
     * @param type
     *            estensione del report
     */
    public GenericReportDescriptor(String reportKey, ExtensionType type) {
        this.type = type;
        this.reportKey = reportKey;
        resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
    }

    @Override
    public ExtensionType getExtensionType() {
        return type;
    }

    @Override
    public String getName() {
        return resourceMap.getString("Reports." + reportKey + ".name");
    }

    @Override
    public String getDescription() {
        return resourceMap.getString("Reports." + reportKey + ".description");
    }
}
