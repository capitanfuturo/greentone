package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;
import it.greentone.persistence.UserProfile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.joda.time.DateTime;
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
 * Finestra di dialogo dedicata alla gestione della login di una persona in
 * anagrafica.
 * 
 * @author Giuseppe Caliendo
 */
@SuppressWarnings("serial")
@Component
public class EditUserDialog extends JDialog
{
	private static final String LOCALIZATION_PREFIX = "editUser.Dialog.";
	private Person person;
	private JLabel nameContentLabel;
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private JComboBox userProfileComboBox;
	private JCheckBox isActiveCheckBox;
	private JLabel activationDateContentLabel;
	private Action okAction;
	private Action cancelAction;
	@Inject
	private PersonService personService;

	/**
	 * Finestra di dialogo dedicata alla gestione della login di una persona in
	 * anagrafica.
	 */
	public EditUserDialog()
	{
		ResourceMap resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		setModal(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(resourceMap.getString(LOCALIZATION_PREFIX + "title"));
		JPanel mainPanel = new JPanel(new MigLayout());
		JLabel nameLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "name"));
		JLabel usernameLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "username"));
		JLabel passwordLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "password"));
		JLabel isActiveLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "active"));
		JLabel activationDateLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "activationDate"));
		JLabel profileLabel =
		  new JLabel(resourceMap.getString(LOCALIZATION_PREFIX + "profile"));
		mainPanel.add(nameLabel);
		mainPanel.add(getNameContentLabel(), "growx, span, wrap");
		mainPanel.add(usernameLabel);
		mainPanel.add(getUsernameTextField(), "growx, span, wrap");
		mainPanel.add(passwordLabel);
		mainPanel.add(getPasswordField(), "growx, span, wrap");
		mainPanel.add(profileLabel);
		mainPanel.add(getUserProfileComboBox(), "growx, span, wrap");
		mainPanel.add(isActiveLabel);
		mainPanel.add(getIsActiveCheckBox());
		mainPanel.add(activationDateLabel);
		mainPanel.add(getActivationDateContentLabel());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton okButton = new JButton(getOkAction());
		okButton.setText(resourceMap.getString(LOCALIZATION_PREFIX + "ok"));
		JButton cancelButton = new JButton(getCancelAction());
		cancelButton.setText(resourceMap.getString(LOCALIZATION_PREFIX + "cancel"));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setLocationRelativeTo(null);
		pack();
	}

	protected JLabel getNameContentLabel()
	{
		if(nameContentLabel == null)
			nameContentLabel = new JLabel();
		return nameContentLabel;
	}

	protected JTextField getUsernameTextField()
	{
		if(usernameTextField == null)
			usernameTextField = new JTextField(25);
		return usernameTextField;
	}

	protected JPasswordField getPasswordField()
	{
		if(passwordField == null)
			passwordField = new JPasswordField(25);
		return passwordField;
	}

	protected JCheckBox getIsActiveCheckBox()
	{
		if(isActiveCheckBox == null)
			isActiveCheckBox = new JCheckBox();
		return isActiveCheckBox;
	}

	protected JLabel getActivationDateContentLabel()
	{
		if(activationDateContentLabel == null)
			activationDateContentLabel = new JLabel();
		return activationDateContentLabel;
	}

	protected JComboBox getUserProfileComboBox()
	{
		if(userProfileComboBox == null)
		{
			userProfileComboBox = new JComboBox();
			UserProfile[] userProfiles = UserProfile.values();
			for(int i = 0; i < userProfiles.length; i++)
			{
				userProfileComboBox.insertItemAt(userProfiles[i].getLocalizedName(), i);
			}
		}
		return userProfileComboBox;
	}

	protected Action getOkAction()
	{
		if(okAction == null)
		{
			okAction = new AbstractAction()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						person.setUsername(GreenToneUtilities
						  .getText(getUsernameTextField()));
						person.setPassword(GreenToneUtilities.getText(getPasswordField()));
						person.setIsActive(getIsActiveCheckBox().isSelected());
						if(getIsActiveCheckBox().isSelected())
						{
							DateTime dt =
							  new DateTime(Calendar.getInstance().getTimeInMillis());
							person.setActivationDate(dt);
						}
						else
						{
							person.setActivationDate(null);
						}
						int selectedIndex = getUserProfileComboBox().getSelectedIndex();
						if(selectedIndex > -1)
						{
							person.setProfile(UserProfile.values()[selectedIndex]);
						}
						else
						{
							person.setProfile(null);
						}
						personService.storePerson(person);
						setVisible(false);
					}
				};
		}
		return okAction;
	}

	protected Action getCancelAction()
	{
		if(cancelAction == null)
		{
			cancelAction = new AbstractAction()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
					}
				};
		}
		return cancelAction;
	}

	/**
	 * Configura la finestra di dialogo prima che venga visualizzata.
	 * 
	 * @param person
	 *          la persona di cui modificare le credenziali
	 */
	public void setup(Person person)
	{
		this.person = person;
		getNameContentLabel().setText(person.getName());
		getUsernameTextField().setText(person.getUsername());
		getPasswordField().setText(person.getPassword());
		getIsActiveCheckBox().setSelected(person.getIsActive());
		if(person.getActivationDate() != null)
		{
			getActivationDateContentLabel().setText(
			  person.getActivationDate().toString("dd/MM/yyyy"));
		}
		else
		{
			getActivationDateContentLabel().setText(null);
		}
	}
}
