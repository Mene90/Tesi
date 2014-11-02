package gui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import automata.fsa.dag.AbstractGraph;

import javax.swing.JOptionPane;

import gui.environment.Environment;
import automata.Automaton;

public class SubStringMatchingAction extends SimulateAction{

	public SubStringMatchingAction(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "SubString matching..");
		putValue(ACCELERATOR_KEY, null);
		this.environment = environment;	
	}
	
	/**
	 * Handles the input
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		performAction((Component)e.getSource());		
	}
	
	public void performAction(Component source){
		if(getObject() instanceof AbstractGraph){
			if (((Automaton)getObject()).getInitialState() == null) {
				JOptionPane.showMessageDialog(source,
						"Simulation requires an automaton\n"
								+ "with an initial state!", "No Initial State",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	/** The environment. */
	private Environment environment = null;
}
