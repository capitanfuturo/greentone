package it.greentone.report;

import it.greentone.GreenToneLogProvider;
import it.greentone.GreenToneUtilities;
import it.greentone.persistence.PersonService;
import it.greentone.report.ReportDescriptorInterface.ExtensionType;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.swing.SwingWorker;

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
	@Inject
	GreenToneUtilities utilities;
	@Inject
	ReportsListDialog reportsListDialog;
	@Inject
	GreenToneLogProvider logProvider;

	/**
	 * Genera un report a partire da un descrittore di report
	 * 
	 * @param reportDescriptor
	 *          descrittore di report
	 */
	public void generate(ReportDescriptorInterface reportDescriptor)
	{
		try
		{
			final JRBeanCollectionDataSource dataSource =
			  new JRBeanCollectionDataSource(reportDescriptor.getDataSet());
			final JasperPrint print =
			  JasperFillManager.fillReport(reportDescriptor.getReportInputStream(),
			    reportDescriptor.getParams(), dataSource);
			File tempFile = null;
			try
			{
				tempFile =
				  File.createTempFile(print.getName(), reportDescriptor
				    .getExtensionType().getExtension());
				if(tempFile != null)
				{
					final File fileToOpen = tempFile;
					if(reportDescriptor.getExtensionType() == ExtensionType.PDF)
					{
						JasperExportManager.exportReportToPdfFile(print,
						  fileToOpen.getPath());
					}
					else
						if(reportDescriptor.getExtensionType() == ExtensionType.XML)
						{
							JasperExportManager.exportReportToXmlFile(print,
							  fileToOpen.getPath(), false);
						}
						else
							if(reportDescriptor.getExtensionType() == ExtensionType.HTML)
							{
								JasperExportManager.exportReportToHtmlFile(print,
								  fileToOpen.getPath());
							}
					utilities.open(fileToOpen);
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

	/**
	 * Mostra il pannello di dialogo per la selezione del report
	 * 
	 * @param category
	 *          categoria dei report
	 */
	public void showDialog(ReportsCategoryInterface category)
	{
		/* imposta la lista di report sul pannello di selezione delle stampe */
		logProvider.getLogger().info("Report dialog setup");
		reportsListDialog.setup(category);
		/* mostra il pannello di selezione */
		reportsListDialog.setVisible(true);
		/* recupera il report selezionato */
		final ReportDescriptorInterface selectedReportDescriptor =
		  reportsListDialog.getSelectedReportDescriptor();
		if(selectedReportDescriptor != null)
		{
			/* imposta i parametri comuni della categoria */
			logProvider.getLogger().info("Report common parameters setup");
			selectedReportDescriptor.setup(category.getCommonParameters());
			/*
			 * TODO chiama il metodo di impostazione dei parametri di lancio
			 * attraverso una dialog
			 */
			/* TODO recupera i parametri immessi dall'utente */
			/* esegue il report in un processo ad-hoc */
			new SwingWorker<Void, Void>()
				{

					@Override
					protected Void doInBackground() throws Exception
					{
						logProvider.getLogger().info(
						  "Generating: " + selectedReportDescriptor);
						generate(selectedReportDescriptor);
						return null;
					}
				}.execute();
		}
	}
}