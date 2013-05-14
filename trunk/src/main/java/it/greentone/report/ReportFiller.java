package it.greentone.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

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
 * Compila il report jasper.
 * 
 * @author Giuseppe Caliendo
 */
public abstract class ReportFiller {

    /**
     * Restituisce un {@link JasperPrint} compilato con i parametri di ingresso.
     * 
     * @param inputParams
     *            parametri di ingresso.
     * @return un'istanza di jasper print.
     * @throws IOException
     *             eccezione in caso di errore in lettura
     * @throws JRException
     *             eccezione in caso di errore di compilazione del report
     * @see JasperFillManager#fillReport(JasperReport, Map, JRDataSource)
     */
    public JasperPrint fill(final Map<String, Object> inputParams) throws JRException, IOException {
        final JRDataSource dataSource = getDataSource(inputParams);
        final Map<String, Object> parameters = getJasperParameters(inputParams);
        JasperReport report = getReport();
        JasperPrint result = JasperFillManager.fillReport(report, parameters, dataSource);
        return result;
    }

    /**
     * Restituisce i parametri componendoli con quelli di base {@link #getInitialJasperParameters(Map)} e quelli
     * opzionali {@link #getDecoratedJasperParameters(Map)}.
     * 
     * @param inputParams
     *            the report input parameters, that is the same input used to prepare the data source too.
     * @return the parameters to be passed to the <code>JasperFillManager</code> to fill the jasper report.
     * @see JasperFillManager#fillReport(JasperReport, Map, JRDataSource)
     */
    protected Map<String, Object> getJasperParameters(final Map<String, Object> inputParams) {
        final Map<String, Object> initialJasperParameters = getInitialJasperParameters(inputParams);
        final Map<String, Object> decoratedJasperParameters = getDecoratedJasperParameters(inputParams);

        final Map<String, Object> result = new HashMap<String, Object>();
        result.putAll(initialJasperParameters);
        result.putAll(decoratedJasperParameters);

        return result;
    }

    /**
     * Restituisce la mappa dei parametri tipici per un report.
     * 
     * @param inputParams
     *            la mappa dei parametri tipici per un report
     * @return la mappa dei parametri tipici per un report
     */
    protected Map<String, Object> getDecoratedJasperParameters(final Map<String, Object> inputParams) {
        return new HashMap<String, Object>(0);
    }

    private Map<String, Object> getInitialJasperParameters(final Map<String, Object> inputParams) {
        final Map<String, Object> map = new HashMap<String, Object>();
        // TODO
        return map;
    }

    /**
     * Restituisce il datasource per il report a partire dai parametri.
     * 
     * @param inputParams
     *            parametri.
     * @return il datasource per il report.
     */
    protected abstract JRDataSource getDataSource(Map<String, Object> inputParams);

    /**
     * Restituisce il report compilato.
     * <p>
     * L'implementazione di default cerca di compilare il report che ha il nome dato da {@link #getJasperReportPath()}
     * (di default il nome della classe filler).
     * </p>
     * 
     * @return il report compilato.
     * @throws JRException
     *             eccezione in caso di errori di compilazione
     * @throws IOException
     *             eccezione in caso di errori di compilazione
     */
    protected JasperReport getReport() throws JRException, IOException {
        final String jasperReportPath = getJasperReportPath();
        InputStream is = getClass().getResourceAsStream(jasperReportPath + ".jrxml");
        if (is != null) {
            try {
                final JasperDesign jd = JRXmlLoader.load(is);
                return JasperCompileManager.compileReport(jd);
            } finally {
                is.close();
            }
        }
        return null;
    }

    /**
     * Restituisce il path del report.
     * 
     * @return il path del report.
     */
    protected String getJasperReportPath() {
        return "/" + getClass().getName().replace('.', '/');
    }
}
