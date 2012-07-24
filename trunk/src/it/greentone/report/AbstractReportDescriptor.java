package it.greentone.report;

import it.greentone.GreenTone;
import it.greentone.persistence.Office;
import it.greentone.persistence.OfficeService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
 * </code>
 * <br>
 * <br>
 * Classe base di descrizione di un report in GreenTone.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public abstract class AbstractReportDescriptor implements
  ReportDescriptorInterface
{
	private static final String LOC_PREFIX = "Reports.";
	private static final String LOC_NAME_SUFFIX = ".name";
	private static final String REPORT_SOURCE_SUFFIX = ".jasper";
	/** Nome dello studio */
	public static final String PARAMETER_OFFICE_NAME = "officeName";
	/** Indirizzo completo dello studio */
	public static final String PARAMETER_OFFICE_ADDRESS = "officeAddress";
	/** Contatti telefonici dello studio */
	public static final String PARAMETER_OFFICE_TELEPHONES = "officeTelephones";
	/** Codice fiscale e partita iva dello studio */
	public static final String PARAMETER_OFFICE_CF_PIVA = "officeCfPiva";
	/** Email dello studio */
	public static final String PARAMETER_OFFICE_EMAIL = "officeEmail";
	/** Logo dello studio */
	public static final String PARAMETER_OFFICE_LOGO = "officeLogo";

	private final Map<String, Object> reportParameters;
	private final ExtensionType type;
	private final ResourceMap resourceMap;
	private final String reportKey;
	@Inject
	private OfficeService officeService;
	private Office office;

	/**
	 * Classe base di descrizione di un report in GreenTone.
	 * 
	 * @param reportKey
	 * @param type
	 */
	public AbstractReportDescriptor(String reportKey, ExtensionType type)
	{
		this.type = type;
		this.reportKey = reportKey;
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		reportParameters = new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> getParameters()
	{
		if(office == null)
		{
			office = officeService.loadOffice();
			/* nome dello studio */
			reportParameters.put(PARAMETER_OFFICE_NAME, office.getName() != null
			  ? office.getName()
			  : "");
			/* indirizzo dello studio */
			StringBuffer sb = new StringBuffer("");
			if(office.getAddress() != null)
			{
				sb.append(office.getAddress() + " ");
			}
			if(office.getCity() != null)
			{
				sb.append(office.getCity() + " ");
			}
			if(office.getProvince() != null)
			{
				sb.append("(" + office.getProvince() + ")");
			}
			reportParameters.put(PARAMETER_OFFICE_ADDRESS, sb.toString());
			/* telefoni dello studio */
			sb = new StringBuffer("");
			if(office.getTelephone1() != null)
			{
				sb.append("tel.: " + office.getTelephone1() + " ");
			}
			if(office.getTelephone2() != null)
			{
				sb.append("tel.: " + office.getTelephone2() + " ");
			}
			if(office.getFax() != null)
			{
				sb.append("fax.: " + office.getFax());
			}
			reportParameters.put(PARAMETER_OFFICE_TELEPHONES, sb.toString());
			/* cf e p.iva dello studio */
			sb = new StringBuffer("");
			if(office.getCf() != null)
			{
				sb.append("Cod.Fisc.: " + office.getCf() + " ");
			}
			if(office.getPiva() != null)
			{
				sb.append("P.IVA: " + office.getPiva());
			}
			reportParameters.put(PARAMETER_OFFICE_CF_PIVA, sb.toString());
			/* email dello studio */
			sb = new StringBuffer("");
			if(office.getEmail() != null)
			{
				sb.append("E-mail: " + office.getEmail());
			}
			reportParameters.put(PARAMETER_OFFICE_EMAIL, sb.toString());
			/* logo dello studio */
			reportParameters.put(PARAMETER_OFFICE_LOGO, office.getLogo());
		}
		return reportParameters;
	}

	@Override
	public ExtensionType getExtensionType()
	{
		return type;
	}

	@Override
	public String getLocalizedName()
	{
		return resourceMap.getString(LOC_PREFIX + reportKey + LOC_NAME_SUFFIX);
	}

	@Override
	public InputStream getReportInputStream()
	{
		return getClass().getResourceAsStream(reportKey + REPORT_SOURCE_SUFFIX);
	}

	@Override
	public String toString()
	{
		return getLocalizedName();
	}

	@Override
	public void retrieveParameters()
	{
		if(getReportParametersDialog() != null)
		{
			getReportParametersDialog().setVisible(true);
			Map<String, Object> outputParameters =
			  getReportParametersDialog().getOutputParameters();
			for(String key : outputParameters.keySet())
			{
				reportParameters.put(key, outputParameters.get(key));
			}
		}
	}

	/**
	 * Metodo da sovrascrivere con opportuna finestra di dialogo nel caso in cui
	 * il report necessiti di parametri scelti dall'utente.
	 * 
	 * @return la finestra di dialogo adibita al recupero dei parametri per questo
	 *         report
	 */
	protected ReportParametersDialog getReportParametersDialog()
	{
		return null;
	}

	/**
	 * Restituisce un dataset vuoto per permettere la stampa delle bande che non
	 * verrebbero stampate nel caso di nessun dato.
	 * 
	 * @return un dataset vuoto per permettere la stampa delle bande che non
	 *         verrebbero stampate nel caso di nessun dato
	 */
	protected final Collection<?> getEmptyDataSet()
	{
		Collection<String> emptyDS = new ArrayList<String>();
		emptyDS.add("");
		return emptyDS;
	}
}
