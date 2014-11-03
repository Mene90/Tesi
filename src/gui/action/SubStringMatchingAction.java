package gui.action;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import automata.fsa.dag.AbstractGraph;
import automata.fsa.dag.AbstractGraphSimulator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.environment.Environment;
import gui.environment.Universe;
import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.SimulatorFactory;

public class SubStringMatchingAction extends SimulateAction{

	public SubStringMatchingAction(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Input");
		putValue(ACCELERATOR_KEY, null);
		this.environment = environment;	
	}
	
	/**
	 * Reports a configuration that accepted.
	 * 
	 * @param configuration
	 *            the configuration that accepted
	 * @param component
	 *            the parent component of dialogs brought up
	 * @return <CODE>true</CODE> if we should continue searching, or <CODE>false</CODE>
	 *         if we should halt
	 */
	protected boolean reportConfiguration(Configuration configuration,
			Component component) {
		JComponent past = (JComponent) gui.sim.TraceWindow
				.getPastPane(configuration);
		past.setPreferredSize(new java.awt.Dimension(300, 400));
		String[] options = { "Keep looking", "I'm done" };
		int result = JOptionPane.showOptionDialog(component, past,
				"Accepting configuration found!", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, null);
		return result == 0;
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		boolean blockEdit = false;
	
		if (!automatonActionPermissible((Component) e.getSource()))
			return;
		Object input = initialInput((Component) e.getSource(), "");
		AbstractGraphSimulator simulator = new AbstractGraphSimulator((AbstractGraph) automaton);
	
		if (input == null)
			return;
		
		
		String s = (String) input;
		Configuration config = simulator.getInitialConfigurations(s); 
		
		handleInteraction(automaton, simulator, config, input);
	}
	
	/**
	 * This will search configurations for an accepting configuration.
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param abstractGraphSimulator
	 *            the automaton simulator for this automaton
	 * @param configs
	 *            the initial configurations generated
	 * @param initialInput
	 *            the object that represents the initial input; this is a String
	 *            object in most cases, but may differ for multiple tape turing
	 *            machines
	 */
	public void handleInteraction(Automaton automaton,
			AbstractGraphSimulator abstractGraphSimulator, Configuration config,
			Object initialInput) {
		
		JFrame frame = Universe.frameForEnvironment(environment);
		
		if(abstractGraphSimulator.simulate(config)){
			JOptionPane.showMessageDialog(frame," configuration"
										+ " accepted, and\nother possibilities are exhausted.");
			return;
		}
		
			JOptionPane.showMessageDialog(frame, "The input was rejected.");
			return;
		
		
	}

	/** The environment. */
	private Environment environment = null;

}
