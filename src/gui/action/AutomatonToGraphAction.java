package gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JOptionPane;

import gui.abstractautomata.AbstractAutomatonPane;
import gui.abstractgraph.GraphPane;
import gui.environment.Environment;
import gui.environment.tag.CriticalTag;
import automata.Automaton;
import automata.AutomatonChecker;
import automata.Configuration;
import automata.State;
import automata.Transition;
import automata.fsa.FSAConfiguration;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.dag.AbstractGraph;

public class AutomatonToGraphAction extends FSAAction {
	
	public AutomatonToGraphAction (FiniteStateAutomaton automaton,
						Environment environment) {
			super("Convert to Graph", null);
			this.automaton = automaton;
			this.environment = environment;
	}

	/**
	 * Puts the DFA form in another window.
	 * 
	 * @param e
	 *            the action event
	 */
	
	public void actionPerformed(ActionEvent e) {
		if (automaton.getInitialState() == null) {
			JOptionPane.showMessageDialog(environment,
					"The automaton needs an initial state.",
					"No Initial State", JOptionPane.ERROR_MESSAGE);
			return;
		}

		AutomatonChecker ac = new AutomatonChecker();
		if (ac.isNFA(automaton)) {
			JOptionPane.showMessageDialog(environment, "This is an NFA!",
					"Not an DFA", JOptionPane.ERROR_MESSAGE);
			return;
		}

		GraphPane convert = new GraphPane(
				(FiniteStateAutomaton) automaton.clone(), environment);
		environment.add(convert, "DFA to Abstract Automaton", new CriticalTag() {
		});
		environment.setActive(convert);
	}
	
	
	/** The automaton. */
	private FiniteStateAutomaton automaton;

	/** The environment. */
	private Environment environment;
	
	
	
}
