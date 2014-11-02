package gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JOptionPane;

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
		environment.add(convert, "DFA to Abstract Graph", new CriticalTag() {
		});
		environment.setActive(convert);
	}

	
	/**
	 * Puts the DFA form in another window.
	 * 
	 * @param e
	 *            the action event
	 */
	/*public void actionPerformed(ActionEvent e) {
		HashSet<State> scc = new HashSet<State>();
		ArrayList sccArray = new ArrayList();
		
		/*if (automaton.getInitialState() == null) {
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
		
		State init = automaton.getInitialState();
		FSAConfiguration config = new FSAConfiguration(init,
									null, null, null);
		
		DFSWalk(config);
		
		// reset all states
		for(State st: automaton.getStates()){
			st.colorWhite();
		}
		
		while (!st.isEmpty()){
			State currentState = (State)st.pop();
			
			if (currentState.isWhite()){
				DFSWalkInvert(currentState ,automaton, scc);	
				sccArray.add(scc.clone());
				scc.clear();
			}
		}
		for(State st: automaton.getStates()){
			st.colorWhite();
		}
		constructDAG(sccArray,config);
		
	}*/
	
	private void constructDAG(ArrayList<HashSet<State>> sccArray,Configuration config){
		
		for(State s : automaton.getStates()){
			s.setName("");			
		}
		
		for (HashSet<State> scc : sccArray){
			if(scc.size()>0){
				for (State currentState : scc){
		
					Transition transition = constructDAGAusiliar(scc,currentState);
				
						if(transition != null){
						
							Transition[] transitionsToState = automaton.getTransitionsToState(currentState);
							
							//sposto tutte le transizioni che vanno in currenteState in ToState cosi
							//da iniziare ad unire gli stati che appartengono alla stessa scc
							
							for(Transition t : transitionsToState){
								automaton.removeTransition(t);
								t.setToState(transition.getToState());
								
								if(t.getToState().equals(t.getFromState())){
									State state = t.getToState();
									String Name = state.getName() + ((FSATransition) t).getLabel();
									state.setName(Name);									

								}else{
									automaton.addTransition(t);
								}
			
							}
							
							Transition[] transitionsFromState = automaton.getTransitionsFromState(currentState);
							
							for(Transition t : transitionsFromState){
								
								if(t.equals(transition)){
									String Label =((FSATransition) transition).getLabel();
									String Name = transition.getFromState().getName() + transition.getToState().getName() + Label; 
									transition.getToState().setName(Name);
									
								}
								else
								{
									automaton.removeTransition(t);
									t.setFromState(transition.getToState());
									automaton.addTransition(t);
								}
							}
						
							if(automaton.isFinalState(currentState)){
								automaton.addFinalState(transition.getToState());
							}
						
							if(automaton.isInitialState(currentState)){
								automaton.setInitialState(transition.getToState());
							}
						
						automaton.removeTransition(transition);
						if(scc.size()>1){
							automaton.removeState(currentState);
						}
					}
				}
				
			}

			}
				
	}
	
	private Transition constructDAGAusiliar(HashSet<State> scc,State currentState){
		
		Transition[] transitionsFromState = automaton.getTransitionsFromState(currentState);
	
		for (int k = 0; k < transitionsFromState.length; k++) {
			
			FSATransition transition = (FSATransition) transitionsFromState[k];		
			if(scc.contains(transition.getToState())){
				return transition;
			}
		}
		
		return null;		
	}
	
	private void DFSWalk(Configuration config){
		FSAConfiguration configuration = (FSAConfiguration) config;
		State currentState = configuration.getCurrentState();
				
		Transition[] transitions = automaton.getTransitionsFromState(currentState);
		
		currentState.colorGrey();
		
		for (int k = 0; k < transitions.length; k++) {
			FSATransition transition = (FSATransition) transitions[k];		
			State toState = transition.getToState();
				
			if(toState.isWhite()){
					FSAConfiguration configurationToAdd = new FSAConfiguration(toState, configuration, null, null);
					DFSWalk(configurationToAdd);
				}	
		}
		currentState.colorBlack();				
		st.push(currentState);
	}

	private void DFSWalkInvert(State currentState, Automaton au,
					HashSet<State> scc){
		
		Transition[] transitions = au.getTransitionsToState(currentState); 
		
		currentState.colorGrey();
		scc.add(currentState);
		
		for (int k = 0; k < transitions.length; k++) {
			FSATransition transition = (FSATransition) transitions[k];
		
			State fromState = transition.getFromState();
			
			if(fromState.isWhite()){
					DFSWalkInvert(fromState,au,scc);
				}	
		}
		currentState.colorBlack();	
	}
	
	/** The automaton. */
	private FiniteStateAutomaton automaton;

	/** The environment. */
	private Environment environment;
	
	/** The Stack for the visit **/
	private Stack <State> st = new Stack<State>();
	
	
}
