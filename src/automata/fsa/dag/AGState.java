package automata.fsa.dag;

import java.awt.Point;
import java.util.HashSet;

import automata.Automaton;
import automata.State;

public class AGState  extends State{
	
	public AGState(int id, Point point, Automaton automaton){
		super(id, point, automaton);
		isSuperNode = false;
		
	}

	public void setAlphabet(HashSet<String> alphabet) {
		
		if (alphabet.size()>0){
			isSuperNode = true;
			setLabel(alphabet.toString());
			this.alphabet=alphabet;
			
		}
		
	}
	
	public HashSet<String> getAphabet(){
		return alphabet;		
	}
	
	public boolean isSuperNode(){
		return isSuperNode;
	}
	
	private boolean isSuperNode;
	private HashSet<String> alphabet;

}
