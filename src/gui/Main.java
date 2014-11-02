package gui;

import gui.environment.FrameFactory;

import java.io.Serializable;

public class Main {

	public static void main(String[] args) {
		
		createWindow(new automata.fsa.FiniteStateAutomaton());

	}
	
	/**
	 * Called once a type of editable object is choosen. The editable object is
	 * passed in, the dialog is hidden, and the window is created.
	 * 
	 * @param object 
	 *             the object that we are to edit
	 */
	private static void createWindow(Serializable object) {
		FrameFactory.createFrame(object);
		System.out.println("bella");
	}
	

}
