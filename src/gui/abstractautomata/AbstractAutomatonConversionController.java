package gui.abstractautomata;

import gui.abstractgraph.GraphPane;
import automata.fsa.DFAtoAA;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.dag.AbstractGraph;
import automata.fsa.dag.DFAtoAG;

public class AbstractAutomatonConversionController {
	
	public AbstractAutomatonConversionController(FiniteStateAutomaton dfa,
			FiniteStateAutomaton abstractautomaton, AbstractAutomatonPane view){
		
		this.abstractautomaton = abstractautomaton;
		this.dfa = dfa;
		this.view = view;
		converter = new DFAtoAA();
		converter.setEpsilonTransictions(abstractautomaton);
		
		
	}
	
	public void transform(){
	    converter.convertDFA(abstractautomaton);
	}
	

	private FiniteStateAutomaton dfa;
	
	private FiniteStateAutomaton abstractautomaton;
	
	private AbstractAutomatonPane view;
	
	private DFAtoAA converter;

}
