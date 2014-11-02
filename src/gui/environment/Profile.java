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

public class Profile {
    public static String LAMBDA = "\u03BB";     
    public static String EPSILON = "\u03B5";   
	public String lambda = "\u03BB";
	public String epsilon = "\u03B5";
	public String lambdaText = "u03BB";
	public String epsilonText = "u03B5";
	private String emptyString = lambda;
	public int undo_num = 50;
	
	/** The tag bane for the empty string preference. */
	public String EMPTY_STRING_NAME = "empty_string";

	/** The tag name for the root of a structure. */
	public static final String STRUCTURE_NAME = "structure";

	/** The tag name for the type of structure this is. */
	public static final String STRUCTURE_TYPE_NAME = "type";
	
	/** The tag name for the Undo amount preference. */
	public static final String UNDO_AMOUNT_NAME = "undo_amount";

	public String pathToFile = "";		
	
    public void setNumUndo(int nn){
    	undo_num = nn;
    }
	
	public Profile(){
		emptyString = lambda;
	}
	
	/**
	 * Sets the empty string.
	 * 
	 * @param empty the empty string
	 */
	public void setEmptyString(String empty){
		emptyString = empty;
	}
	
	/**
	 * Returns the empty string.
	 * 
	 * @return the empty string
	 */
	public String getEmptyString(){
		return emptyString;
	}
		
}
