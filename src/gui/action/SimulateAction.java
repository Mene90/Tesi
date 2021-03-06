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





package gui.action;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.SimulatorFactory;
import automata.State;
import gui.environment.Environment;
import gui.environment.Universe;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import gui.sim.SimulatorPane;
import gui.environment.tag.CriticalTag;
import javax.swing.*;

import java.awt.GridLayout;
import java.awt.Component;
import java.util.*;
import java.io.*;



/**
 * This is the action used for the stepwise simulation of data. This method can
 * operate on any automaton. It uses a special exception for the two tape case.
 * 
 * @author Thomas Finley
 */

public class SimulateAction extends AutomatonAction {


	/**
	 * Instantiates a new <CODE>SimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public SimulateAction(Automaton automaton, Environment environment) {
		super("Step...", null);
		this.automaton = automaton;
		this.environment = environment;
	}
	
	

	/**
	 * Returns the simulator for this automaton.
	 * 
	 * @param automaton
	 *            the automaton to get the simulator for
	 * @return a simulator for this automaton
	 */
	protected AutomatonSimulator getSimulator(Automaton automaton) {
		return SimulatorFactory.getSimulator(automaton);
	}

	/**
	 * Given initial configurations, the simulator, and the automaton, takes any
	 * further action that may be necessary. In the case of stepwise operation,
	 * which is the default, an additional tab is added to the environment
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param simulator
	 *            the automaton simulator for this automaton
	 * @param configurations
	 *            the initial configurations generated
	 * @param initialInput
	 *            the object that represents the initial input; this is a String
	 *            object in most cases, but may differ for multiple tape turing
	 *            machines
	 */
	public void handleInteraction(Automaton automaton,
			AutomatonSimulator simulator, Configuration[] configurations,
			Object initialInput) {
		SimulatorPane simpane = new SimulatorPane(automaton, simulator,
				configurations, environment, false);
		if (initialInput instanceof String[])
			initialInput = java.util.Arrays.asList((String[]) initialInput);
		environment.add(simpane, "Simulate: " + initialInput,
				new CriticalTag() {
				});
		environment.setActive(simpane);
	}

	/**
	 * This returns an object that encapsulates the user input for the starting
	 * configuration. In most cases this will be a string, except in the case of
	 * a multiple tape Turing machine. This method will probably involve some
	 * prompt to the user. By default this method prompts the user using a
	 * dialog box and returns the result from that dialog.
	 * 
	 * @param component
	 *            a parent for whatever dialogs are brought up
	 * @return the object that represents the initial input to the machine, or
	 *         <CODE>null</CODE> if the user elected to cancel
	 */
	protected Object initialInput(Component component, String title) {
        if(title.equals("")) title = "Input";
      
            if (title.equals("")){
            	return openInputGUI(component, "Input?", 0);
                //return JOptionPane.showInputDialog(component, "Input?");
                
            }
            else
            {
            	return openInputGUI(component, title, 0);
            	//return JOptionPane.showInputDialog(component, title+ "?!!!!");
            }
        
	}

	/**
	 * Opens pop-up GUI for taking input. Now JFLAP can take file as an input.
	 * @param component
	 * @param title
	 * @return
	 */
	private Object openInputGUI(final Component component, String title, final int tapes) {
		// TODO Auto-generated method stub
		JPanel panel;
		JTextField[] fields;
		
		//for FA, PDA
		if (tapes==0)
		{
			panel = new JPanel(new GridLayout(3, 1));
			fields = new JTextField[1];
			for (int i = 0; i < 1; i++) {
				panel.add(new JLabel(title + " "));
				panel.add(fields[i] = new JTextField());
			}
		}
		else
		{
			panel = new JPanel(new GridLayout(tapes*2+1, 2));
			fields = new JTextField[tapes];
			for (int i = 0; i < tapes; i++) {
				panel.add(new JLabel(title + " "+(i+1)));
				panel.add(fields[i] = new JTextField());
			}
		}
		
		int result = JOptionPane.showOptionDialog(component, panel, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.YES_OPTION && result != JOptionPane.OK_OPTION)
			return null;
		if (tapes==0)
		{
			String input = fields[0].getText();
			return input;
		}
		else
		{
    		String[] input = new String[tapes];
    		for (int i = 0; i < tapes; i++)
    			input[i] = fields[i].getText();
    		return input;
		}
	}
	
	private void handleInputFile(Object input)
	{
		Configuration[] configs = null;
		AutomatonSimulator simulator = getSimulator(automaton);

		if (input == null)
			return;
		
		String s = (String) input;
		configs = simulator.getInitialConfigurations(s); 
			
		handleInteraction(automaton, simulator, configs, input);

	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		boolean blockEdit = false;
	
		if (!automatonActionPermissible((Component) e.getSource()))
			return;
		Object input = initialInput((Component) e.getSource(), "");
		Configuration[] configs = null;
		AutomatonSimulator simulator = getSimulator(automaton);
	
		if (input == null)
			return;
		
		
		String s = (String) input;
		configs = simulator.getInitialConfigurations(s); 
		
		handleInteraction(automaton, simulator, configs, input);
	}
	
	/**
	 * Returns whether the current automaton can legally be simulated.  If not,
	 * a dialog pops up.  Will true if <code>automaton</code> = <i>null</i>.
	 * 
	 * @param source
	 * 		the source of this action
	 * @return whether the current automaton can legally be simulated
	 */
	protected boolean automatonActionPermissible(Component source) {
		if (!(getObject() instanceof Automaton))
			return true;
		if (automaton.getInitialState() == null) {
			JOptionPane.showMessageDialog(source,
					"Simulation requires an automaton\n"
							+ "with an initial state!", "No Initial State",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
        
        return true;
	}

	/**
	 * Simulate actions are applicable to every automaton which accepts a single
	 * string of input, i.e., every automaton except for dual tape turing
	 * machines.
	 * 
	 * @param object
	 *            to object to test for applicability
	 */
	public static boolean isApplicable(Object object) {
		return object instanceof Automaton;
	}

	/**
	 * Returns the automaton.
	 * 
	 * @return the automaton
	 */
	protected Object getObject() {
		return automaton;
	}

	/**
	 * Returns the environment.
	 * 
	 * @return the environment
	 */
	protected Environment getEnvironment() {
		return environment;
	}
	
	protected void setEnvironment(Environment newEnv) {
		environment = newEnv;
	}

	/** The automaton this simulate action runs simulations on! */
	protected Automaton automaton;

	/** The environment that the simulation pane will be put in. */
	private Environment environment;
	
}
