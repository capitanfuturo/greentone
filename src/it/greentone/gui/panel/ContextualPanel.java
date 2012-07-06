package it.greentone.gui.panel;

import it.greentone.GreenToneUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

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
 * Pannello generico da estendere per poter avere delle facilities utili. Un
 * pannello contestuale è composto da un pannello di intestazione contenente le
 * informazioni dell'oggetto selezionato nella tabella generale sottostante.
 * 
 * @author Giuseppe Caliendo
 * @param <T>
 *          Oggetto base del pannello contestuale
 */
@SuppressWarnings("serial")
public abstract class ContextualPanel<T> extends AbstractPanel
{
	private JXTable contentTable;
	private JToolBar contextualToolBar;
	private JPanel headerPanel;
	private T selectedItem;
	Set<JComponent> registeredComponents;


	/**
	 * Pannello generico da estendere per poter avere delle facilities utili. Un
	 * pannello contestuale è composto da un pannello di intestazione contenente
	 * le informazioni dell'oggetto selezionato nella tabella generale
	 * sottostante.
	 */
	public ContextualPanel()
	{
		super();
		registeredComponents = new HashSet<JComponent>();
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(getContextualToolBar(), BorderLayout.NORTH);
		northPanel.add(getHeaderPanel(), BorderLayout.CENTER);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(new JScrollPane(getContentTable()), BorderLayout.CENTER);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(northPanel, BorderLayout.NORTH);
		if(getContentTable() != null)
		{
			mainPanel.add(contentPanel, BorderLayout.CENTER);
		}
		mainPanel.setPreferredSize(new Dimension(800, 600));
		setLayout(new BorderLayout());
		JScrollPane mainScrollPane = new JScrollPane(mainPanel);
		add(mainScrollPane);
	}

	/**
	 * Restituisce il pannello superiore contenente tipicamente le informazioni
	 * dettagliate della tabella sottostante.
	 * 
	 * @return il pannello superiore contenente tipicamente le informazioni
	 *         dettagliate della tabella sottostante
	 */
	public JPanel getHeaderPanel()
	{
		if(headerPanel == null)
			headerPanel = createHeaderPanel();
		return headerPanel;
	}

	/**
	 * Costruisce il pannello superiore contenente tipicamente le informazioni
	 * dettagliate della tabella sottostante.
	 * 
	 * @return il pannello superiore contenente tipicamente le informazioni
	 *         dettagliate della tabella sottostante
	 */
	protected JPanel createHeaderPanel()
	{
		return new JPanel(new BorderLayout());
	}

	/**
	 * Restituisce la tabella di tutti gli elementi dell'oggetto di modello.
	 * 
	 * @return la tabella di tutti gli elementi dell'oggetto di modello
	 */
	public JXTable getContentTable()
	{
		if(contentTable == null)
		{
			contentTable = GreenToneUtilities.createJXTable();
			contentTable.getSelectionModel().addListSelectionListener(
			  new ListSelectionListener()
				  {
					  @Override
					  public void valueChanged(ListSelectionEvent e)
					  {
						  if(!e.getValueIsAdjusting())
						  {
							  SwingUtilities.invokeLater(new Runnable()
								  {
									  @Override
									  public void run()
									  {
										  int selectedRow = getContentTable().getSelectedRow();
										  /* disabilito la tabella */
										  contentTable.setEnabled(false);
										  /* gestisco la selezione */
										  if(selectedRow > -1)
										  {
											  int rowIndexToModel =
											    getContentTable().convertRowIndexToModel(
											      getContentTable().getSelectedRow());
											  setSelectedItem(getItemFromTableRow(rowIndexToModel));
											  initializeForEditing();
											  tableSelectionHook();
										  }
										  else
										  {
											  tableSelectionLostHook();
										  }
										  /* riabilito la tabella */
										  contentTable.setEnabled(true);
									  }

								  });
						  }
					  }
				  });
		}
		return contentTable;
	}

	/**
	 * Restituisce la barra degli strumenti delle azioni disponibili per il
	 * pannello corrente.
	 * 
	 * @return la barra degli strumenti delle azioni disponibili per il pannello
	 *         corrente
	 */
	public JToolBar getContextualToolBar()
	{
		if(contextualToolBar == null)
		{
			contextualToolBar = new JToolBar();
			contextualToolBar.setFloatable(false);
		}
		return contextualToolBar;
	}

	/**
	 * Inizializza o re-inizializza il pannello intero
	 */
	@Override
	public void setup()
	{
		super.setup();
		getContextualToolBar().removeAll();
		initializeToolBar();
		populateModel();
		clearForm();
		setHeaderPanelEnabled(false);
	}

	/**
	 * Abilita o disabilita tutti i componenti registrati
	 * {@link #registerComponent(JComponent)} del pannello di testata
	 * 
	 * @param enable
	 *          stato di abilitazione del pannello di testata
	 */
	protected void setHeaderPanelEnabled(boolean enable)
	{
		for(JComponent component : registeredComponents)
		{
			component.setEnabled(enable);
		}
	}

	/**
	 * Inizializzazione della toolbar. Per accedere alla toolbar usare
	 * {@link #getContextualToolBar()}
	 */
	protected abstract void initializeToolBar();

	/**
	 * Utilizzare questo metodo per popolare il modello dei dati utili per
	 * l'interfaccia grafica
	 */
	protected abstract void populateModel();

	/**
	 * Attività da compiere dopo il salvataggio della persistenza.
	 */
	public void postSaveData()
	{
		SwingUtilities.invokeLater(new Runnable()
			{

				@Override
				public void run()
				{
					clearForm();
					setHeaderPanelEnabled(false);
					if(getContentTable() != null)
					{
						getContentTable().clearSelection();
					}
					setStatus(null);
				}
			});
	};

	/**
	 * Azioni da intraprendere per l'inserimento di una nuova riga della
	 * {@link #getContentTable()}.
	 */
	public void initializeForInsertion()
	{
		setStatus(EStatus.NEW);
		setHeaderPanelEnabled(true);
		clearForm();
		getContentTable().clearSelection();
	}

	/**
	 * Azioni da intraprendere per la modifica di una riga della
	 * {@link #getContentTable()}.
	 */
	protected void initializeForEditing()
	{
		setStatus(EStatus.EDIT);
		setHeaderPanelEnabled(true);
		clearForm();
	}

	/**
	 * Porzione di codice che viene eseguita a seguito della selezione di un
	 * elemento nella tabella {@link #getContentTable()}, in coda a
	 * {@link #initializeForEditing()}
	 */
	protected void tableSelectionHook()
	{
		// vuoto di default;
	}

	/**
	 * Porzione di codice che viene eseguita a seguito della perdita di selezione
	 * nella tabella {@link #getContentTable()}
	 */
	protected void tableSelectionLostHook()
	{
		// vuoto di default;
	}

	/**
	 * Ripulisce il pannello {@link #getHeaderPanel()} di intestazione.
	 */
	protected void clearForm()
	{
		for(JComponent component : registeredComponents)
		{
			if(component instanceof JTextComponent)
			{
				JTextComponent textComponent = (JTextComponent) component;
				textComponent.setText(null);
			}
			else
				if(component instanceof JComboBox)
				{
					JComboBox comboBox = (JComboBox) component;
					comboBox.setSelectedItem(null);
				}
				else
					if(component instanceof JCheckBox)
					{
						JCheckBox checkBox = (JCheckBox) component;
						checkBox.setSelected(false);
					}
					else
						if(component instanceof JXDatePicker)
						{
							JXDatePicker datePicker = (JXDatePicker) component;
							datePicker.setDate(null);
						}
		}
	}

	/**
	 * Registra un componente per la gestione automatizzata.
	 * 
	 * @param component
	 *          componente da aggiungere
	 */
	public void registerComponent(JComponent component)
	{
		registeredComponents.add(component);
	}

	/**
	 * Deregistra un componente per la gestione automatizzata.
	 * 
	 * @param component
	 *          componente da rimuovere
	 */
	public void deregisterComponent(JComponent component)
	{
		registeredComponents.remove(component);
	}

	/**
	 * Restituisce il nome del bundle del pannello.
	 * 
	 * @return il nome del bundle del pannello
	 */
	@Override
	public abstract String getBundleName();

	/**
	 * Restituisce l'elemento correntemente selezionato dalla tabella contestuale.
	 * 
	 * @return l'elemento correntemente selezionato dalla tabella contestuale
	 * @see ContextualPanel#getContentTable()
	 */
	public T getSelectedItem()
	{
		if(selectedItem == null && getContentTable().getSelectedRow() > -1)
		{
			int rowIndexToModel =
			  getContentTable().convertRowIndexToModel(
			    getContentTable().getSelectedRow());
			setSelectedItem(getItemFromTableRow(rowIndexToModel));
		}
		return selectedItem;
	}

	/**
	 * Imposta l'elemento correntemente selezionato dalla tabella contestuale.
	 * 
	 * @param selectedItem
	 *          l'elemento correntemente selezionato dalla tabella contestuale
	 * @see ContextualPanel#getContentTable()
	 */
	public void setSelectedItem(T selectedItem)
	{
		this.selectedItem = selectedItem;
	}

	/**
	 * Restituisce a partire dalla riga di modello della tabella dei contenuti
	 * {@link #getContentTable()} un oggetto di modello.
	 * 
	 * @param rowIndex
	 * @return oggetto di modello
	 */
	public abstract T getItemFromTableRow(int rowIndex);
}
