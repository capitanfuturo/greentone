package it.greentone.report.impl;

import it.greentone.report.ReportFiller;
import it.greentone.report.descriptor.ReportDescriptor;

/**
 * <code>
 * GreenTone - gestionale per geometri italiani.<br>
 * Copyright (C) 2011-2013 GreenTone Developer Team.<br>
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
 * Report di base.
 * 
 * @author Giuseppe Caliendo
 */
public abstract class AbstractReportImpl extends ReportFiller {

    ReportDescriptor descriptor;

    /**
     * Report di base.
     * 
     * @param descriptor
     *            descrittore Greentone di report
     */
    public AbstractReportImpl(ReportDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * Restituisce il descrittore Greentone di report.
     * 
     * @return il descrittore Greentone di report
     */
    public ReportDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return descriptor.getName();
    }
}
