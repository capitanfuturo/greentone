package it.greentone.report;

import it.greentone.persistence.PersonService;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

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
 * </code>
 * <br>
 * <br>
 * Report di informativa sulla privacy.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class InformativaPrivacyReport implements ReportDescriptorInterface
{
	@Inject
	private PersonService personService;
	/** Chiave del parametro della persona selezionata */
	public static final String PERSON = "person";
	Map<String, Object> commonParameters;

	@Override
	public void setup(Map<String, Object> commonParameters)
	{
		this.commonParameters = commonParameters;
	}

	@Override
	public Collection<?> getDataSet()
	{
		// TODO
		return personService.getAllPersons();
	}

	@Override
	public Map<String, Object> getParams()
	{
		return new HashMap<String, Object>();
	}

	@Override
	public InputStream getReportInputStream()
	{
		return getClass().getResourceAsStream("report.jasper");
	}

	@Override
	public ExtensionType getExtensionType()
	{
		return ExtensionType.PDF;
	}

	@Override
	public String toString()
	{
		return "Informativa sulla privacy";
	}
}
