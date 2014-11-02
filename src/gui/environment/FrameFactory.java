package gui.environment;


import automata.Automaton;

import java.io.Serializable;
import java.awt.Dimension;

public class FrameFactory {
	
	/**
	 * This creates an environment frame for a new item.
	 * 
	 * @param object
	 *            the object that we are to edit
	 * @return the environment frame for this new item, or <CODE>null</CODE>
	 *         if an error occurred
	 */
	
	public static EnvironmentFrame createFrame(Serializable object) {
		Environment environment = EnvironmentFactory.getEnvironment(object);
		if (environment == null)
			return null; // No environment could be found.
		EnvironmentFrame frame = new EnvironmentFrame(environment);
		if (object instanceof Automaton) {
		
			((Automaton) object).setEnvironmentFrame(frame);
		}
		frame.pack();

		// Make sure that the size of the frame is above a certain
		// threshold.
		int width = 600, height = 400;
        
		width = Math.max(width, frame.getSize().width);
		height = Math.max(height, frame.getSize().height);
		frame.setSize(new Dimension(width, height));
		frame.setVisible(true);

		return frame;
	}
    

}
