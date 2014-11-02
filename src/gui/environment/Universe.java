/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package gui.environment;

import java.util.*;


/**
 * The <CODE>Universe</CODE> class serves as a large global "registry" for the
 * active windows and their associated environments.
 */

public class Universe {
	/**
	 * This class needn't have multiple instances, so we disable the main
	 * constructor.
	 */
	private Universe() {
	}

	/**
	 * Registers an environment frame.
	 * 
	 * @param frame
	 *            the environment frame to register
	 * @return an integer for the number of frames that have been registered
	 *         sofar, including this one
	 */
	public static int registerFrame(EnvironmentFrame frame) {
		Environment env = frame.getEnvironment();
		environmentToFrame.put(env, frame);
		
		return ++numberRegistered;
	}

	/**
	 * Unregisters an environment frame.
	 * 
	 * @param frame
	 *            the environment frame to unregister
	 */
	public static void unregisterFrame(EnvironmentFrame frame) {

		environmentToFrame.remove(frame.getEnvironment());
	
	}

	/**
	 * Given an environment, this returns the frame associated with that
	 * environment.
	 * 
	 * @param environment
	 *            an environment that may have some frame
	 * @return the environment frame associated with this environment, or <CODE>null</CODE>
	 *         if there is no frame associated with this environment
	 */
	public static EnvironmentFrame frameForEnvironment(Environment environment) {
		return (EnvironmentFrame) environmentToFrame.get(environment);
	}

	/**
	 * Returns a list of the registered environment frames.
	 * 
	 * @return an array containing all registered environment frames
	 */
	public static EnvironmentFrame[] frames() {
		return (EnvironmentFrame[]) environmentToFrame.values().toArray(
				new EnvironmentFrame[0]);
	}

	/**
	 * Returns the number of currently open frames that hold a representation of
	 * a structure (i.e. automaton, grammar, or regular expression).
	 * 
	 * @return the number of currently open frames
	 */
	public static int numberOfFrames() {
		return environmentToFrame.size();
	}

	/** The mapping of environments to frames. */
	private static Map environmentToFrame = new HashMap();

	/**
	 * The number of frames that have been registered... this is used to
	 * describe the untitled frames with something unique.
	 */
	private static int numberRegistered = 0;
	
	public static Profile curProfile = new Profile();
}
