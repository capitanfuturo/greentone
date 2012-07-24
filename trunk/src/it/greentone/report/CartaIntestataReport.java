package it.greentone.report;

import it.greentone.GreenToneUtilities;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.springframework.stereotype.Component;

import ca.odell.glazedlists.swing.EventComboBoxModel;

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
 * Stampa carta intestata dello studio.
 * 
 * @author Giuseppe Caliendo
 */
@Component
public class CartaIntestataReport extends AbstractReportDescriptor {
	private static final String REPORT_KEY = "CartaIntestata";
	/** Chiave del parametro persona */
	public static final String PARAM_PERSON = "person";
	/** Chiave del parametro id di persona */
	public static final String PARAM_PERSON_ID = "personId";
	/** Chiave del parametro testo */
	public static final String PARAM_MESSAGE = "message";
	/** Finestra di dialogo per i parametri di lancio del report */
	public static CartaIntestataParametersDialog parameterDialog;

	@Inject
	private PersonService personService;

	/**
	 * Stampa carta intestata dello studio.
	 */
	public CartaIntestataReport() {
		super(REPORT_KEY, ExtensionType.PDF);
	}

	@Override
	public Collection<?> getDataSet() {
		Collection<Person> personList = new ArrayList<Person>();
		Person person = (Person) getParameters().get(PARAM_PERSON);
		personList.add(person);
		return personList;
	}

	@Override
	protected ReportParametersDialog getReportParametersDialog() {
		if (parameterDialog == null) {
			parameterDialog = new CartaIntestataParametersDialog();
		}
		return parameterDialog;
	}

	/**
	 * Finestra di dialogo per la raccolta dei parametri.
	 * 
	 * @author Giuseppe Caliendo
	 * 
	 */
	@SuppressWarnings("serial")
	private class CartaIntestataParametersDialog extends ReportParametersDialog {

		private JComboBox personsCB;
		private JTextArea textArea;

		public CartaIntestataParametersDialog() {
			getOkButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getOutputParameters().put(PARAM_PERSON, getPersonsCB().getSelectedItem());
					getOutputParameters().put(PARAM_PERSON_ID, ((Person) getPersonsCB().getSelectedItem()).getId());
					getOutputParameters().put(PARAM_MESSAGE, GreenToneUtilities.getText(getTextArea()));
					setVisible(false);
				}
			});
		}

		@Override
		protected JPanel getParametersPanel() {
			JPanel panel = new JPanel(new BorderLayout(5, 5));
			panel.add(getPersonsCB(), BorderLayout.NORTH);
			panel.add(getTextArea(), BorderLayout.CENTER);
			return panel;
		}

		public JComboBox getPersonsCB() {
			if (personsCB == null) {
				personsCB = new JComboBox();
				personsCB.setModel(new EventComboBoxModel<Person>(personService.getAllPersons()));
				personsCB.setSelectedIndex(0);
			}
			return personsCB;
		}

		public JTextArea getTextArea() {
			if (textArea == null) {
				textArea = new JTextArea(5, 10);
			}
			return textArea;
		}

		@Override
		protected String getParametersPanelTitle() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
