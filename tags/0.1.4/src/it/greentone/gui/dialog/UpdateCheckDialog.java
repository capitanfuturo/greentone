package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.GreenToneLogProvider;
import it.greentone.GreenToneUtilities;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

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
 * Finestra di dialogo per la gestione dell'avviso di un aggiornamento rilevato.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
public class UpdateCheckDialog extends JDialog {

	private final ResourceMap resourceMap;
	private final JLabel messageLabel;
	private final JEditorPane webContent;

	public UpdateCheckDialog(final GreenToneUtilities utilities, final GreenToneLogProvider logger) {
		super();
		resourceMap = Application.getInstance(GreenTone.class).getContext().getResourceMap();

		setTitle(resourceMap.getString("viewPersons.Panel.infoTitle"));
		setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
		setModal(true);

		/* icona */
		JLabel logoLabel = new JLabel(resourceMap.getIcon("viewAbout.Action.largeIcon"));
		/* messaggio */
		messageLabel = new JLabel();

		webContent = new JEditorPane("text/html", "");
		webContent.setEditable(false);
		webContent.setOpaque(false);
		webContent.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent hle) {
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
					try {
						utilities.browse(hle.getURL().toURI());
					} catch (URISyntaxException e) {
						logger.getLogger().log(Level.WARNING, resourceMap.getString("ErrorMessage.cannotOpenURL"), e);
					}
				}
			}
		});
		// bottone di chiusura
		JButton closeButton = new JButton(resourceMap.getString("MainPanel.close"));
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout());
		contentPanel.add(logoLabel);
		contentPanel.add(messageLabel, "wrap");
		contentPanel.add(webContent, "spanx 2, center, wrap");
		contentPanel.add(closeButton, "spanx 2, right");

		getRootPane().setLayout(new BorderLayout());
		getRootPane().add(contentPanel, BorderLayout.CENTER);
	}

	/**
	 * Visualizza la dialog
	 * 
	 * @param remoteVersion
	 *            versione presente nei repositories remoti
	 */
	public void showDialog(String remoteVersion) {
		String currentVersion = resourceMap.getString("Application.version");
		String message = MessageFormat.format(resourceMap.getString("Application.newVersionAvaible"), currentVersion, remoteVersion);
		messageLabel.setText(message);

		String linkUrl = MessageFormat.format(resourceMap.getString("Application.downloadPage"), remoteVersion);
		String downloadLabel = resourceMap.getString("MainPanel.download");

		webContent.setText("<a href='" + linkUrl + "'>" + downloadLabel + "</a>");

		pack();
		setVisible(true);
	}
}
