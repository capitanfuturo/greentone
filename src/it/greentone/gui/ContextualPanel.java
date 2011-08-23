package it.greentone.gui;

import it.greentone.GreenTone;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
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
 */
@SuppressWarnings("serial")
public abstract class ContextualPanel extends JPanel
{
	private JXTable contentTable;
	private JToolBar contextualToolBar;
	private JPanel headerPanel;
	private final ResourceMap resourceMap;

	/**
	 * Pannello generico da estendere per poter avere delle facilities utili. Un
	 * pannello contestuale è composto da un pannello di intestazione contenente
	 * le informazioni dell'oggetto selezionato nella tabella generale
	 * sottostante.
	 */
	public ContextualPanel()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(getContextualToolBar(), BorderLayout.NORTH);
		northPanel.add(getHeaderPanel(), BorderLayout.CENTER);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(new JScrollPane(getContentTable()), BorderLayout.CENTER);
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
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
			contentTable = createContentTable();
		return contentTable;
	}

	/**
	 * Costruisce la tabella di tutti gli elementi dell'oggetto di modello.
	 * 
	 * @return la tabella di tutti gli elementi dell'oggetto di modello
	 */
	protected JXTable createContentTable()
	{
		JXTable table = new JXTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setColumnControlVisible(true);
		return table;
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

	protected ResourceMap getResourceMap()
	{
		return resourceMap;
	}

	/**
	 * Inizializza o re-inizializza il pannello intero
	 */
	public abstract void setup();

	/**
	 * Ripulisce il pannello {@link #getHeaderPanel()} di intestazione.
	 */
	public void clearForm()
	{
		// non fa nulla
	}

	/**
	 * Restituisce il nome localizzato del pannello.
	 * 
	 * @return il nome localizzato del pannello
	 */
	public abstract String getPanelName();
}
