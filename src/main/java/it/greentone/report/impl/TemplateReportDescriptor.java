package it.greentone.report.impl;

import it.greentone.report.descriptor.GenericReportDescriptor;
import it.greentone.report.descriptor.ReportDescriptor.ExtensionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Component;

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
 * Template per lo sviluppo di report.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class TemplateReportDescriptor extends AbstractReportImpl {

    /**
     * Template per lo sviluppo di report.
     */
    public TemplateReportDescriptor() {
        super(new GenericReportDescriptor("template", ExtensionType.PDF));
    }

    @Override
    protected JRDataSource getDataSource(Map<String, Object> inputParams) {

        Collection<TemplateBean> tmp = new ArrayList<TemplateReportDescriptor.TemplateBean>();
        for (int i = 0; i < 10; i++) {
            TemplateBean bean = new TemplateBean();
            bean.setField1("field1: " + i);
            tmp.add(bean);
        }
        JRDataSource datasource = new JRBeanCollectionDataSource(tmp);
        return datasource;
    }

    /**
     * Bean ad uso del template
     * 
     * @author Giuseppe Caliendo
     */
    public class TemplateBean implements Serializable {
        private static final long serialVersionUID = 1L;
        String field1;

        public void setField1(String field1) {
            this.field1 = field1;
        };

        public String getField1() {
            return field1;
        }
    }
}
