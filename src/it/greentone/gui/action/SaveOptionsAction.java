package it.greentone.gui.action;

import it.greentone.ConfigurationProperties;
import it.greentone.GreenTone;
import it.greentone.GreenToneLogProvider;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.panel.OptionsPanel;
import it.greentone.persistence.JobService;
import it.greentone.persistence.Office;
import it.greentone.persistence.OfficeService;

import java.text.ParseException;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.jdesktop.application.AbstractBean;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 * Azione di salvataggio delle opzioni.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class SaveOptionsAction extends AbstractBean {
	@Inject
	private OptionsPanel optionsPanel;
	@Inject
	private ConfigurationProperties properties;
	@Inject
	private JobService jobService;
	@Inject
	private OfficeService officeService;
	@Inject
	private GreenToneLogProvider logger;
	private final ResourceMap resourceMap;

	/**
	 * Azione di salvataggio delle opzioni.
	 */
	public SaveOptionsAction() {
		resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();
	}

	/**
	 * Azione di salvataggio delle opzioni.
	 */
	@Action
	public void saveOptions() {
		String vacazione = GreenToneUtilities.getText(optionsPanel.getVacazioneTextField());
		if (vacazione == null) {
			JOptionPane.showMessageDialog(optionsPanel, resourceMap.getString("saveOptions.Action.priceNotNull"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
			optionsPanel.getVacazioneTextField().requestFocus();
		} else {
			try {
				/* salvo i dati relativi allo studio professionale */
				Office office = officeService.loadOffice();
				office.setAddress(GreenToneUtilities.getText(optionsPanel.getAddressTextField()));
				office.setCap(GreenToneUtilities.getText(optionsPanel.getCapTextField()));
				office.setCf(GreenToneUtilities.getText(optionsPanel.getCfTextField()));
				office.setCity(GreenToneUtilities.getText(optionsPanel.getCityTextField()));
				office.setEmail(GreenToneUtilities.getText(optionsPanel.getEmailTextField()));
				office.setFax(GreenToneUtilities.getText(optionsPanel.getFaxTextField()));
				office.setName(GreenToneUtilities.getText(optionsPanel.getNameTextField()));
				office.setPiva(GreenToneUtilities.getText(optionsPanel.getPivaTextField()));
				office.setProvince(GreenToneUtilities.getText(optionsPanel.getProvinceTextField()));
				office.setTelephone1(GreenToneUtilities.getText(optionsPanel.getTelephone1TextField()));
				office.setTelephone2(GreenToneUtilities.getText(optionsPanel.getTelephone2TextField()));
				if (optionsPanel.getLogoPreviewPanel().getImage() != null) {
					office.setLogo(GreenToneUtilities.toBufferedImage(optionsPanel.getLogoPreviewPanel().getImage()));
				} else {
					office.setLogo(null);
				}
				officeService.storeOffice(office);

				/* recupero i dati dal pannello e li salvo */
				properties.setConfirmClosureActivated(optionsPanel.getConfirmOnCloseCheckBox().isSelected());

				properties.setCheckUpdateActivated(optionsPanel.getCheckUpdateCheckBox().isSelected());

				optionsPanel.getVacazioneTextField().commitEdit();
				Object value = optionsPanel.getVacazioneTextField().getValue();
				properties.setVacazionePrice(GreenToneUtilities.roundTwoDecimals(new Double(value.toString())));

				value = optionsPanel.getVacazioneAiutanteTextField().getValue();
				properties.setVacazioneHelperPrice(GreenToneUtilities.roundTwoDecimals(new Double(value.toString())));

				boolean useYears = properties.getUseYearsInJobsProtocol();
				if ((optionsPanel.getUseYearInJobProtocolCheckBox().isSelected() && !useYears)
						|| (!optionsPanel.getUseYearInJobProtocolCheckBox().isSelected() && useYears)) {
					/*
					 * se l'utente è sicuro di voler migrare i protocolli degli
					 * incarichi allora procedo
					 */
					int confirm = JOptionPane.showConfirmDialog(optionsPanel, resourceMap.getString("saveOptions.Action.jobProtocolMigrationMessage"));
					if (confirm == JOptionPane.OK_OPTION) {
						if (optionsPanel.getUseYearInJobProtocolCheckBox().isSelected()) {
							jobService.addYearToAllJobProtocols();
						} else {
							try {
								jobService.removeYearToAllJobProtocols();
							} catch (Exception e) {
								JOptionPane.showMessageDialog(optionsPanel, resourceMap.getString("saveOptions.Action.tooJobs"), resourceMap.getString("ErrorMessage.title"), JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				properties.setUseYearsInJobsProtocol(optionsPanel.getUseYearInJobProtocolCheckBox().isSelected());

				/* salvo */
				properties.store();
			} catch (ParseException e) {
				logger.getLogger().log(Level.SEVERE, resourceMap.getString("ErrorMessage.saveOptions"), e);
			}
		}
	}
}
