package gui.abstractgraph;

import java.awt.Point;

import gui.deterministic.ConversionPane;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.dag.AbstractGraph;
import automata.fsa.dag.DFAtoAG;

public class GraphConversionController {

	public GraphConversionController(FiniteStateAutomaton dfa,
			AbstractGraph graph, GraphPane view){
		
		this.graph = graph;
		this.dfa = dfa;
		this.view = view;
		converter = new DFAtoAG();
		converter.createAbstractGraph(dfa,graph);
		//graph.getInitialState().setPoint(new Point(50, 50));
		
		
	}
	

	private FiniteStateAutomaton dfa;
	
	private AbstractGraph graph;
	
	private GraphPane view;
	
	private DFAtoAG converter;
}
