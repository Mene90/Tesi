package automata.fsa.dag;

import java.awt.Point;

import automata.State;
import automata.fsa.FiniteStateAutomaton;

public class AbstractGraph extends FiniteStateAutomaton {
	
	public AbstractGraph () {
		super();
	}
	
	/**
	 * Creates AGstate, inserts it in this AbstractGraph, and returns that AGstate.
	 * The ID for the AGstate is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public AGState createState(Point point) {
		int i = 0;
		while (getStateWithID(i) != null)
			i++;
		AGState agstate = new AGState(i, point, this);
		addState(agstate);
		return agstate;
	}

}
