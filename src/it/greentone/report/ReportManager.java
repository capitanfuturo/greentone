package it.greentone.report;

import it.greentone.GreenTone;
import it.greentone.GreenToneLogProvider;
import it.greentone.GreenToneUtilities;
import it.greentone.persistence.PersonService;
import it.greentone.report.ReportDescriptorInterface.ExtensionType;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
 * Manager dei report.
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
	JDialog messageDialog;
	final ResourceMap resourceMap;
	JFrame mainFrame;

	/**
	 * Manager dei report.
	 */
	public ReportManager()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		mainFrame = Application.getInstance(GreenTone.class).getMainFrame();
	}

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
			    reportDescriptor.getParameters(), dataSource);
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
			/*
			 * chiama il metodo di impostazione dei parametri di lancio attraverso una
			 * dialog
			 */
			selectedReportDescriptor.retrieveParameters();
			new SwingWorker<Void, Void>()
				{

					@Override
					protected Void doInBackground() throws Exception
					{
						getMessageDialog().setVisible(true);
						logProvider.getLogger().info(
						  "Generating: " + selectedReportDescriptor);
						generate(selectedReportDescriptor);
						return null;
					}

					@Override
					protected void done()
					{
						getMessageDialog().setVisible(false);
					}
				}.execute();
		}
	}

	private JDialog getMessageDialog()
	{
		if(messageDialog == null)
		{
			messageDialog = new JDialog(mainFrame);
			messageDialog.setTitle(resourceMap.getString("Application.title"));
			messageDialog.getContentPane().setLayout(new BorderLayout(5, 5));
			JPanel panel = new JPanel(new MigLayout());
			panel.add(new JLabel(), "wrap");
			panel
			  .add(new JLabel(resourceMap.getString("viewReports.Dialog.printing")));
			panel.add(new JLabel(), "wrap");
			messageDialog.getContentPane().add(panel, BorderLayout.CENTER);
			messageDialog.pack();
			messageDialog.setLocationRelativeTo(null);
		}
		return messageDialog;
	}
}