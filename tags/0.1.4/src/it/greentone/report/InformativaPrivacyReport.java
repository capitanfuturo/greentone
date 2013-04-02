package it.greentone.report;

import it.greentone.persistence.PersonService;

import java.util.Collection;

import javax.inject.Inject;

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
 * Report di informativa sulla privacy.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class InformativaPrivacyReport extends AbstractReportDescriptor
{
	private static final String REPORT_KEY = "InformativaPrivacy";
	@Inject
	private PersonService personService;

	/**
	 * Report di informativa sulla privacy.
	 */
	public InformativaPrivacyReport()
	{
		super(REPORT_KEY, ExtensionType.PDF);
	}

	@Override
	public Collection<?> getDataSet()
	{
		return personService.getAllPersons();
	}
}
