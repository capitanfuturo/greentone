package it.greentone.report;

import it.greentone.GreenToneUtilities;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
 * Manager dei report
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class ReportManager
{
	@Inject
	PersonService personService;

	/**
	 * Genera un report
	 */
	public void generate()
	{
		try
		{
			Collection<Person> data = personService.getAllPersons();
			InputStream reportDescriptor =
			  getClass().getResourceAsStream("report.jasper");
			Map<String, Object> params = new HashMap<String, Object>();

			final JRBeanCollectionDataSource dataSource =
			  new JRBeanCollectionDataSource(data);
			final JasperPrint print =
			  JasperFillManager.fillReport(reportDescriptor, params, dataSource);
			File tempFile = null;
			try
			{
				tempFile = File.createTempFile(print.getName(), ".pdf");
				if(tempFile != null)
				{
					final File fileToOpen = tempFile;
					JasperExportManager
					  .exportReportToPdfFile(print, fileToOpen.getPath());
					GreenToneUtilities.open(fileToOpen);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		catch(final Exception e)
		{
			e.printStackTrace();
		}
	}
}
