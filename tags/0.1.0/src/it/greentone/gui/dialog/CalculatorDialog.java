package it.greentone.gui.dialog;

import it.greentone.GreenTone;
import it.greentone.GreenToneUtilities;
import it.greentone.gui.FontProvider;
import it.greentone.gui.panel.OperationsPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Stack;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

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
 * Calcolatrice applicativa.
 * 
 * @author Giuseppe Caliendo
 */
@Component
@SuppressWarnings("serial")
public class CalculatorDialog extends JDialog
{
	@Inject
	private OperationsPanel operationsPanel;
	private final ResourceMap resourceMap;
	private JTextField displayField;
	private JButton equalButton;
	private JButton clearButton;
	private JButton applyButton;

	/**
	 * Calcolatrice applicativa.
	 */
	public CalculatorDialog()
	{
		resourceMap =
		  Application.getInstance(GreenTone.class).getContext().getResourceMap();
		setTitle(resourceMap.getString("viewCalc.Dialog.title"));
		setIconImage(resourceMap.getImageIcon("Application.icon").getImage());
		setModal(true);

		JPanel mainPanel = new JPanel(new MigLayout("fill", "[80%][20%]"));

		JPanel numberPanel = new JPanel(new MigLayout("wrap 3", "[33%][33%][33%]"));
		String[] numbers =
		  new String[] {"7", "8", "9", "4", "5", "6", "1", "2", "3", "0", ","};
		for(final String number : numbers)
		{
			JButton numberButton = new JButton(number);
			numberButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						updateDisplayField(number);
					}
				});
			numberButton.setFont(FontProvider.CODE_BIG);
			numberPanel.add(numberButton, "growx");
		}

		JPanel symbolPanel = new JPanel(new MigLayout("fill, wrap 1"));
		String[] operators = new String[] {"/", "*", "-", "+"};
		for(final String operator : operators)
		{
			JButton operatorButton = new JButton(operator);
			operatorButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						updateDisplayField(operator);
					}
				});
			operatorButton.setFont(FontProvider.CODE_BIG);
			symbolPanel.add(operatorButton, "growx");
		}

		/* assemblo il pannello */
		mainPanel.add(getDisplayField(), "growx");
		mainPanel.add(getClearButton(), "growx, wrap");
		mainPanel.add(numberPanel, "growx");
		mainPanel.add(symbolPanel, "growx, wrap");
		mainPanel.add(getEqualButton(), "growx");
		mainPanel.add(getApplyButton(), "growx");

		getRootPane().setLayout(new BorderLayout());
		getRootPane().add(mainPanel, BorderLayout.CENTER);
		setSize(300, 300);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	/**
	 * Restituisce il display della calcolatrice.
	 * 
	 * @return il display della calcolatrice
	 */
	private JTextField getDisplayField()
	{
		if(displayField == null)
		{
			displayField = new JTextField();
			displayField.setHorizontalAlignment(JTextField.RIGHT);
			displayField.setFont(FontProvider.CODE_BIG);
			displayField.addKeyListener(new KeyAdapter()
				{
					@Override
					public void keyReleased(KeyEvent e)
					{
						if(e.getKeyCode() == KeyEvent.VK_ENTER)
						{
							equalAction();
						}
					}
				});
		}
		return displayField;
	}

	/**
	 * Restituisce il bottone di uguale che calcola il risultato.
	 * 
	 * @return il bottone di uguale che calcola il risultato
	 */
	private JButton getEqualButton()
	{
		if(equalButton == null)
		{
			equalButton = new JButton("=");
			equalButton.setFont(FontProvider.CODE_BIG);
			equalButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						equalAction();
					}
				});
		}
		return equalButton;
	}

	/**
	 * Esegue il calcolo a partire dal testo contenuto in
	 * {@link #getDisplayField()}
	 */
	private void equalAction()
	{
		/*
		 * eseguo il parsing della stringa, per farlo utilizzo la notazione polacca
		 * inversa RPN
		 */
		String polishNotation = GreenToneUtilities.getText(getDisplayField());
		try
		{
			getDisplayField().setText(evalrpn(infix2RPN(polishNotation)) + "");
		}
		catch(Exception e1)
		{
			getDisplayField().setText(resourceMap.getString("viewCalc.Dialog.error"));
		}
	}

	/**
	 * Restituisce il bottone che cancella il display
	 * 
	 * @return il bottone che cancella il display
	 */
	public JButton getClearButton()
	{
		if(clearButton == null)
		{
			clearButton =
			  new JButton(resourceMap.getIcon("viewCalc.Dialog.clearIcon"));
			clearButton
			  .setToolTipText(resourceMap.getString("viewCalc.Dialog.clear"));
			clearButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						getDisplayField().setText("");
					}
				});
		}
		return clearButton;
	}

	/**
	 * Restituisce il bottone che applica il risultato nel pannello delle
	 * operazioni.
	 * 
	 * @return il bottone che applica il risultato nel pannello delle operazioni
	 */
	public JButton getApplyButton()
	{
		if(applyButton == null)
		{
			applyButton =
			  new JButton(resourceMap.getIcon("viewCalc.Dialog.applyIcon"));
			applyButton
			  .setToolTipText(resourceMap.getString("viewCalc.Dialog.apply"));
			applyButton.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						operationsPanel.getAmountTextField().setText(
						  getDisplayField().getText().replace('.', ','));
						getDisplayField().setText("");
						setVisible(false);
					}
				});
		}
		return applyButton;
	}

	private void updateDisplayField(String symbol)
	{
		String text = getDisplayField().getText();
		getDisplayField().setText(text.concat(symbol).replace(',', '.'));
	}

	/**
	 * A partire da una notazione infissa ne calcola la rappresentazione in
	 * polacca inversa.
	 * 
	 * @param input
	 *          stringa in notazione infissa
	 * @return stack rappresentante la notazione polacca inversa risultante
	 */
	private Stack<String> infix2RPN(String input)
	{
		Stack<String> result = new Stack<String>();
		if(input == null)
			return result;
		LinkedList<String> stack = new LinkedList<String>();
		String number = "";

		for(int i = 0; i < input.length(); i++)
		{
			char tmp = input.charAt(i);
			switch(tmp)
			{
				case '+':
				case '-':
				case '*':
				case '/':
				case '(':
					stack.push(tmp + "");
					stack.push(number);
					number = "";
				case ' ':
					break;
				case ')':
					break;
				default:
					number += tmp + "";
					break;
			}
		}
		stack.push(number);
		result.addAll(stack);

		return result;
	}

	/**
	 * A partire da una notazione polacca inversa rappresentata da uno stack ne
	 * calcola il risultato.
	 * 
	 * @param tks
	 *          stack dei tokens da valutare
	 * @return il risultato del calcolo dell'espressione
	 * @throws Exception
	 *           in caso di errore di formattazione o altro
	 */
	private double evalrpn(Stack<String> tks) throws Exception
	{
		String tk = tks.pop();
		double x, y;
		try
		{
			x = Double.parseDouble(tk);
		}
		catch(Exception e)
		{
			y = evalrpn(tks);
			x = evalrpn(tks);
			if(tk.equals("+"))
				x += y;
			else
				if(tk.equals("-"))
					x -= y;
				else
					if(tk.equals("*"))
						x *= y;
					else
						if(tk.equals("/"))
							x /= y;
						else
							throw new Exception();
		}
		return x;
	}

	/**
	 * Inizializza la calcolatrice
	 * 
	 * @param amount
	 *          importo iniziale per il calcolo
	 */
	public void setup(String amount)
	{
		getDisplayField().setText(amount);
	}
}
