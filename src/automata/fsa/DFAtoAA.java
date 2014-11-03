package automata.fsa;

import java.util.ArrayList;
import java.util.Iterator;

import automata.Automaton;
import automata.AutomatonChecker;
import automata.Configuration;
import automata.State;
import automata.Transition;

public class DFAtoAA extends NFAToDFA {

	
	public void setEpsilonTransictions (Automaton dfa){
		Transition[] transitions = dfa.getTransitions();
		for(Transition t : transitions){
			FSATransition epsilonTran = new FSATransition
					(t.getFromState(),t.getToState(),"");
			dfa.addTransition(epsilonTran);
		}
		
	}
	
	/**
	 * Returns a deterministic finite state automaton equivalent to <CODE>automaton</CODE>.
	 * <CODE>automaton</CODE> is not at all affected by this conversion.
	 * 
	 * @param automaton
	 *            the automaton to convert to a dfa.
	 * @return a deterministic finite state automaton equivalent to <CODE>automaton</CODE>.
	 */
	public void convertDFA(Automaton abstractautomaton) {
		/** check if actually nfa. */
		AutomatonChecker ac = new AutomatonChecker();
		if (!ac.isNFA(abstractautomaton)) {
			return;
		}
		/** create new finite state automaton. */
		FiniteStateAutomaton automaton = (FiniteStateAutomaton) abstractautomaton.clone();
		abstractautomaton.clear();
		State initialState = createInitialState(automaton, abstractautomaton);
		/**
		 * get initial state and add to list of states that need to be expanded.
		 */
		ArrayList list = new ArrayList();
		list.add(initialState);
		/** while still more states to be expanded. */
		while (!list.isEmpty()) {
			ArrayList statesToExpand = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				State state = (State) it.next();
				/** expand state. */
				statesToExpand.addAll(expandState(state, automaton, abstractautomaton));
				it.remove();
			}
			list.addAll(statesToExpand);
		}
	}
}
