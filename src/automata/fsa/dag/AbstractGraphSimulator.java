package automata.fsa.dag;

import java.util.HashSet;

import automata.Configuration;
import automata.State;
import automata.Transition;
import automata.fsa.FSAConfiguration;
import automata.fsa.FSATransition;

public class AbstractGraphSimulator {
	
	public AbstractGraphSimulator(AbstractGraph graph) {
		this.graph = graph;
		
	}
	
public boolean simulate (Configuration config){
		
		FSAConfiguration configuration = (FSAConfiguration) config;
		String unprocessedInput = configuration.getUnprocessedInput();
		String totalInput = configuration.getInput();
		AGState currentState = (AGState) configuration.getCurrentState();
		Transition[] transitions = graph.getTransitionsFromState(currentState);
		
		
		if(unprocessedInput.length() == 0)			
			return true;		
		else if ((configuration.getParent() == null && currentState.isSuperNode()) || (
					currentState.isSuperNode() && configuration.getParent() != null && 
					!configuration.getParent().getCurrentState().equals(currentState))){
			
			HashSet<String> alphabet = currentState.getAphabet();
			String c = unprocessedInput.substring(0, 1);
			while(alphabet.contains(c)){
				unprocessedInput = unprocessedInput.substring(1);
				if(unprocessedInput.length() == 0)
					return true;
				c = unprocessedInput.substring(0,1);				
			}
			
			String input = unprocessedInput;
			FSAConfiguration configurationToAdd = new FSAConfiguration(
					currentState, configuration, totalInput, input);
			if (simulate(configurationToAdd)){
				return true;
			}
			
		}		
		else{
		for (int k = 0; k < transitions.length; k++) {
			
			FSATransition transition = (FSATransition) transitions[k];
			String transLabel = transition.getLabel();
						
			if (unprocessedInput.startsWith(transLabel)) {
				String input = "";
				
				//System.out.println("Unprocessed input start with: " + transLabel);
				
				input = unprocessedInput.substring(transLabel.length());
				
				//System.out.println("Input: " + input);
				
				if(input.length() == 0){
					return true;
				}
				
				State toState = transition.getToState();
				FSAConfiguration configurationToAdd = new FSAConfiguration(
						toState, configuration, totalInput, input);
				if(simulate(configurationToAdd))
					return true;
				}
			else{
				State toState = transition.getToState();
				FSAConfiguration configurationToAdd = new FSAConfiguration(
						toState, configuration, totalInput, unprocessedInput);
				if(simulate(configurationToAdd))
					return true;				
			}
			
			}
		}
		
		return false;
	}

/**
 * Returns an Configuration object that represents the initial
 * configuration of the AbstractGraph, before any input has been processed.
 * 
 * @param input
 *            the input string.
 */
public Configuration getInitialConfigurations(String input) {
	Configuration config = new FSAConfiguration(graph.getInitialState(),null,input,input);
	
	return config;
}
 private AbstractGraph graph;

}
