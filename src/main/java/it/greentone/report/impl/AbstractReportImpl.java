package it.greentone.report.impl;

import it.greentone.report.ReportFiller;
import it.greentone.report.descriptor.ReportDescriptor;

public abstract class AbstractReportImpl extends ReportFiller {

    ReportDescriptor descriptor;

    public AbstractReportImpl(ReportDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public ReportDescriptor getDescriptor() {
        return descriptor;
    }
}
