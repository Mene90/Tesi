package automata.fsa.dag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import automata.Automaton;
import automata.Configuration;
import automata.State;
import automata.StatePlacer;
import automata.Transition;
import automata.fsa.FSAConfiguration;
import automata.fsa.FSATransition;
import automata.fsa.FiniteStateAutomaton;

public class DFAtoAG {

	
	public DFAtoAG (){
	}
	
	public void createAbstractGraph(FiniteStateAutomaton dfa ,AbstractGraph graph){
		
		HashMap <State,HashSet> SCCofState;
		
		/** create the set of strongly connected component SetOfSCC. */
		ArrayList<HashSet> setOfScc = new ArrayList<HashSet>();
		setOfScc = ConstructSetOfSCC(dfa);
		
		/** create the abstract graph starting from strongly connected components */
		constructAbstractGraph(graph,dfa,setOfScc);	
	}
	
	public AbstractGraph createAbstractGraph(FiniteStateAutomaton dfa){
			
		HashMap <State,HashSet> SCCofState;
		
		/** create new Abstract graph. */
		AbstractGraph graph = new AbstractGraph();
		
		createAbstractGraph(dfa,graph);
			
		
		return graph;
		
	}

	private void constructAbstractGraph(AbstractGraph graph, FiniteStateAutomaton dfa,
													ArrayList<HashSet> setOfScc){
		
		/** construct an abstract node for each SCC starting from the initial state of 
		 * the dfa*/
		for(HashSet<State> scc : setOfScc){
			StatePlacer sp = new StatePlacer();
			AGState node = graph.createState(sp.getPointForState(graph));
			if (hasInitiaState(scc,dfa)){
				graph.setInitialState(node);
			}
			SCCofAGstates.put(node, scc);
			AGStateOfSCC.put(scc,node);
		}
		
		/** constructs the edge of the AbstractGraph starting
		 *  fron the initial state of the graph  */
				
		for (HashSet<State> scc : setOfScc){
			
			AGState currentAGState = (AGState) AGStateOfSCC.get(scc);
			
			for (State currentState : scc){
				
				currentAGState.setAlphabet(getAlphabetOfSCC(scc));
				FSATransition[] transitionFromAGState = getTransitionFromSCC(scc);
				for(FSATransition t : transitionFromAGState){
					
					AGState ToAGState = (AGState) AGStateOfSCC.get(SCCofState.get(t.getToState()));
					Transition transition = new FSATransition(currentAGState,ToAGState, t.getLabel());
					graph.addTransition(transition);
					
					}
				
						
				}
			}
		
		
		}
		
		
	private FSATransition[] getTransitionFromSCC (HashSet<State> scc){
		ArrayList<Transition> transitions =  new ArrayList<Transition>();
		
		for(State s : scc){
			Transition[] fromtransitions = s.getAutomaton().getTransitionsFromState(s);
			for(Transition t : fromtransitions){
				if(!scc.contains(t.getToState())){
					transitions.add(t);					
				}
			}
		}
		
		return transitions.toArray(new FSATransition[0]);	
	}
		
		
	private HashSet<String> getAlphabetOfSCC(HashSet<State> scc){
		
		HashSet<String> alphabet = new HashSet<String>();
		
		for(State s : scc){
			Transition[] transitions = s.getAutomaton().getTransitionsFromState(s);
			for(Transition t : transitions){
				if(scc.contains(t.getToState())){
					alphabet.add(((FSATransition) t).getLabel());
				}
			}
		}
		
		return alphabet;
		
	}
	
	private boolean hasInitiaState(HashSet<State> scc, FiniteStateAutomaton dfa){
		for (State s : scc){
			if (dfa.isInitialState(s))
				return true;
		}
		return false;
	}
	
	private ArrayList<HashSet> ConstructSetOfSCC(FiniteStateAutomaton dfa){
		/* initial state of dfa*/
		State init = dfa.getInitialState();
		
		ArrayList<HashSet> setOfScc = new ArrayList<HashSet>();
		HashSet<State> scc = new HashSet<State>();
		
		FSAConfiguration config = new FSAConfiguration(init,
									null, null, null);
		
		//first visit
		DFS(config);
		
		// reset all states
		for(State st: dfa.getStates()){
			st.colorWhite();
		}
		
		//second visit
		while (!st.isEmpty()){
			State currentState = (State)st.pop();
			
			if (currentState.isWhite()){
				DFS(currentState ,dfa, scc);//cambia il nome di sta roba
				setOfScc.add(scc);
				System.out.println("giro");
				if(scc.size()>0)
				for (State s : scc)
					SCCofState.put(s, scc);
				scc = new HashSet<State>();
			}
		}
		
		// reset all states
		for(State st: dfa.getStates()){
			st.colorWhite();
		}
		
		return setOfScc;
		
	}
	
	private void DFS(Configuration config){
		
		FSAConfiguration configuration = (FSAConfiguration) config;
		State currentState = configuration.getCurrentState();
		FiniteStateAutomaton dfa = (FiniteStateAutomaton) configuration.getCurrentState().getAutomaton();
				
		Transition[] transitions = dfa.getTransitionsFromState(currentState);
		
		currentState.colorGrey();
		
		for (int k = 0; k < transitions.length; k++) {
			FSATransition transition = (FSATransition) transitions[k];		
			State toState = transition.getToState();
				
			if(toState.isWhite()){
					FSAConfiguration configurationToAdd = new FSAConfiguration(toState, configuration, null, null);
					DFS(configurationToAdd);
				}	
		}
		currentState.colorBlack();				
		st.push(currentState);
	}
	
	private void DFS(State currentState, Automaton au,
			HashSet<State> scc){

		Transition[] transitions = au.getTransitionsToState(currentState); 
		currentState.colorGrey();
		scc.add(currentState);

		
			
		for (int k = 0; k < transitions.length; k++) {
				FSATransition transition = (FSATransition) transitions[k];

				State fromState = transition.getFromState();
	
				if(fromState.isWhite()){
					DFS(fromState,au,scc);
				}	
		}
	
		currentState.colorBlack();	
	}
		
	
	private Stack <State> st = new Stack<State>();
	
	private HashSet stateOnSCC;
	private HashMap <State,HashSet> SCCofAGstates = new HashMap <State,HashSet>();
	private HashMap <HashSet,State> AGStateOfSCC = new HashMap <HashSet,State>();
	private HashMap <State,HashSet> SCCofState = new HashMap <State,HashSet>();
	
	
	
}
