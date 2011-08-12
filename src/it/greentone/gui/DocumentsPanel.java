package it.greentone.gui;

import it.greentone.gui.action.ActionProvider;
import it.greentone.persistence.Person;
import it.greentone.persistence.PersonService;

import java.util.Calendar;

import javax.inject.Inject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.springframework.stereotype.Component;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;

@SuppressWarnings("serial")
@Component
public class DocumentsPanel extends ContextualPanel
{
	private static final String LOCALIZATION_PREFIX = "viewDocuments.Panel.";
	private final String panelTitle;
	@Inject
	private ActionProvider actionProvider;
	@Inject
	private PersonService personService;

	private JTextField protocolTextField;
	private JTextField descriptionTextField;
	private JComboBox jobComboBox;
	private JComboBox recipientComboBox;
	private JCheckBox isDigitalCheckBox;
	private JTextField fileTextField;
	private JCheckBox incomingCheckBox;
	private JXDatePicker releaseDateDatePicker;
	private JTextArea notesTextArea;

	public DocumentsPanel()
	{
		super();
		panelTitle = getResourceMap().getString(LOCALIZATION_PREFIX + "title");
	}

	@Override
	protected JPanel createHeaderPanel()
	{
		JLabel protocolLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "protocol"));
		JLabel descriptionLabel =
		  new JLabel(getResourceMap()
		    .getString(LOCALIZATION_PREFIX + "description"));
		JLabel jobLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "job"));
		JLabel recipientLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "recipient"));
		JLabel isDigitalLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "isDigital"));
		JLabel fileLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "file"));
		JLabel incomingLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "incoming"));
		JLabel releaseDateLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "date"));
		JLabel notesLabel =
		  new JLabel(getResourceMap().getString(LOCALIZATION_PREFIX + "notes"));

		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.add(protocolLabel, "gap para");
		headerPanel.add(getProtocolTextField(), "growx, wrap");
		headerPanel.add(descriptionLabel, "gap para");
		headerPanel.add(getDescriptionTextField(), "growx, wrap");
		headerPanel.add(jobLabel, "gap para");
		headerPanel.add(getJobComboBox(), "growx, wrap");
		headerPanel.add(recipientLabel, "gap para");
		headerPanel.add(getRecipientComboBox(), "growx, wrap");
		headerPanel.add(isDigitalLabel, "gap para");
		headerPanel.add(getIsDigitalCheckBox());
		headerPanel.add(fileLabel, "gap para");
		headerPanel.add(getFileTextField(), "growx, wrap");
		headerPanel.add(incomingLabel, "gap para");
		headerPanel.add(getIncomingCheckBox());
		headerPanel.add(releaseDateLabel, "gap para");
		headerPanel.add(getReleaseDateDatePicker(), "growx, wrap");
		headerPanel.add(notesLabel, "gap para");
		headerPanel.add(getNotesTextArea(), "span, growx");
		return headerPanel;
	}

	public JTextField getProtocolTextField()
	{
		if(protocolTextField == null)
			protocolTextField = new JTextField(15);
		return protocolTextField;
	}

	public JTextField getDescriptionTextField()
	{
		if(descriptionTextField == null)
			descriptionTextField = new JTextField(20);
		return descriptionTextField;
	}

	public JComboBox getJobComboBox()
	{
		if(jobComboBox == null)
			jobComboBox = new JComboBox();
		return jobComboBox;
	}

	public JComboBox getRecipientComboBox()
	{
		if(recipientComboBox == null)
			recipientComboBox = new JComboBox();
		return recipientComboBox;
	}

	public JCheckBox getIsDigitalCheckBox()
	{
		if(isDigitalCheckBox == null)
			isDigitalCheckBox = new JCheckBox();
		return isDigitalCheckBox;
	}

	public JTextField getFileTextField()
	{
		if(fileTextField == null)
			fileTextField = new JTextField(25);
		return fileTextField;
	}

	public JCheckBox getIncomingCheckBox()
	{
		if(incomingCheckBox == null)
			incomingCheckBox = new JCheckBox();
		return incomingCheckBox;
	}

	public JXDatePicker getReleaseDateDatePicker()
	{
		if(releaseDateDatePicker == null)
			releaseDateDatePicker =
			  new JXDatePicker(Calendar.getInstance().getTime());
		return releaseDateDatePicker;
	}

	public JTextArea getNotesTextArea()
	{
		if(notesTextArea == null)
			notesTextArea = new JTextArea(5, 50);
		return notesTextArea;
	}

	@Override
	public void setup()
	{
		/* pulisco e ricostruisco la toolbar */
		getContextualToolBar().removeAll();
		getContextualToolBar().add(actionProvider.getAddDocument());
		getContextualToolBar().add(actionProvider.getSaveDocument());
		getContextualToolBar().add(actionProvider.getDeleteDocument());

		/* carico destinatari */
		EventList<Person> allPersonsEventList = new BasicEventList<Person>();
		allPersonsEventList.addAll(personService.getAllPersons());
		getRecipientComboBox().setModel(
		  new EventComboBoxModel<Person>(allPersonsEventList));

	}

	@Override
	public String getPanelName()
	{
		return panelTitle;
	}

}
