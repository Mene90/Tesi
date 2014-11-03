package gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import gui.abstractautomata.AbstractAutomatonPane;
import gui.abstractgraph.GraphPane;
import gui.environment.Environment;
import gui.environment.tag.CriticalTag;
import automata.AutomatonChecker;
import automata.fsa.FiniteStateAutomaton;


/**
 * This is a simple action for showing the AbstractDFA form of an DFA.
 * 
 */
public class DFAToAbstractAutomatonAction extends FSAAction{
	
	public DFAToAbstractAutomatonAction (FiniteStateAutomaton automaton,
			Environment environment) {
			super("Convert to AbstractAutomaton", null);
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

		AbstractAutomatonPane convert = new AbstractAutomatonPane(
				(FiniteStateAutomaton) automaton.clone(), environment);
		environment.add(convert, "Convert to AbstractAutomaton", new CriticalTag() {
		});
		environment.setActive(convert);
	}
	
	/** The automaton. */
	private FiniteStateAutomaton automaton;

	/** The environment. */
	private Environment environment;

}
