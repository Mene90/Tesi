/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */


package gui.menu;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;


import gui.environment.Environment;
import gui.environment.AutomatonEnvironment;
import gui.environment.EnvironmentFrame;
import gui.environment.Universe;
import gui.action.*;
import automata.Automaton;



/**
 * The <CODE>MenuBarCreator</CODE> is a creator of the menu bars for the FLAP
 * application.
 * 
 */

public class MenuBarCreator {
	/**
	 * Instantiates the menu bar.
	 * 
	 * @param frame
	 *            the environment frame that holds the environment and object
	 * @return the menu bar appropriate for the environment
	 */
	public static JMenuBar getMenuBar(EnvironmentFrame frame) {
		JMenuBar bar = new JMenuBar();
		JMenu menu;
		
		menu = getInputMenu(frame);
		if (menu.getItemCount() > 0)
			bar.add(menu);

		menu = getConvertMenu(frame);
		if (menu.getItemCount() > 0)
			bar.add(menu);
		
        CloseButton dismiss = new CloseButton(frame.getEnvironment());
        bar.add(Box.createGlue());
        bar.add(dismiss);
        
		return bar;
	}
	
	/**
	 * Adds an action to a menu with the accelerator key set.
	 * 
	 * @param menu
	 *            the menu to add the action to
	 * @param a
	 *            the action to create the menu item for
	 */
	public static void addItem(JMenu menu, Action a) {
		JMenuItem item = new JMenuItem(a);
		item.setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
		menu.add(item);
	}

	/**
	 * Instantiates the menu that holds input related menu events.
	 * 
	 * @param frame
	 *            the environment frame that holds the environment and object
	 * @return an input menu
	 */
	private static JMenu getInputMenu(EnvironmentFrame frame) {
		Environment environment = frame.getEnvironment();
		JMenu menu = new JMenu("Input");
		Serializable object = environment.getObject();
		
		if (NoInteractionSimulateAction.isApplicable(object))
			addItem(menu, new NoInteractionSimulateAction((Automaton) object,
					environment));
		if (MultipleSimulateAction.isApplicable(object))
			addItem(menu, new MultipleSimulateAction((Automaton) object,
					environment));
		if	(SubStringMatchingAction.isApplicable(object))
			addItem(menu, new SubStringMatchingAction((Automaton) object,
					environment));

		return menu;
	}

	
	/**
	 * This is the menu for doing conversions.
	 * 
	 * @param frame
	 *            the environment frame that holds the conversion items
	 * @return the conversion menu
	 */
	private static JMenu getConvertMenu(EnvironmentFrame frame) {
		Environment environment = frame.getEnvironment();
		JMenu menu = new JMenu("Convert");
		Serializable object = environment.getObject();
		
		if (NFAToDFAAction.isApplicable(object))
			addItem(menu, new NFAToDFAAction(
					(automata.fsa.FiniteStateAutomaton) object, environment));
		if(AutomatonToGraphAction.isApplicable(object))
			addItem(menu, new AutomatonToGraphAction(
					(automata.fsa.FiniteStateAutomaton) object, environment));
		if (DFAToAbstractAutomatonAction.isApplicable(object))
			addItem(menu, new DFAToAbstractAutomatonAction(
					(automata.fsa.FiniteStateAutomaton) object, environment));
			
		return menu;
	}
	

}
