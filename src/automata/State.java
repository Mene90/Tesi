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





package automata;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import automata.event.AutomataStateEvent;

/**
 * This class represents a single state in an automaton. This class is intended
 * to act as nothing more than a simple placeholder.
 * 
 * @author Thomas Finley
 * @version 1.0
 */

public class State implements Serializable {
	/**
	 * Instantiates a new state.
	 * 
	 * @param id
	 *            the state id, used for generating
	 * @param point
	 *            the point that the center of the state will be at in the
	 *            canvas
	 * @param automaton
	 *            the automaton this belongs to
	 */
	public State(int id, Point point, Automaton automaton) {
		this.point = point;
		this.id = id;
		this.automaton = automaton;
		this.myColor = WHITE;
	}


	/**
	 * Returns the point this state is centered on in the canvas.
	 * 
	 * @see #setPoint(Point)
	 * @return the point this state is centered on in the canvas
	 */
	public Point getPoint() {
		return point;
	}

	public void setAutomaton(Automaton auto) {
		this.automaton = auto;
	}

	/**
	 * Returns the automaton that this state belongs to.
	 * 
	 * @return the automaton that this state belongs to
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/**
	 * Sets the point for this state.
	 * 
	 * @param point
	 *            the point this state is moving to
	 * @see #getPoint()
	 */
	public void setPoint(Point point) {
		this.point = point;

		getAutomaton()
				.distributeStateEvent(
						new AutomataStateEvent(getAutomaton(), this, false,
								true, false));
	}
	


	/**
	 * Returns the state ID for this state.
	 * 
	 * @return the ID of the state
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the ID for this state.
	 * 
	 * @param id
	 *            the new ID for this state.
	 */
	protected void setID(int id) {
		if (("q" + this.id).equals(name))
			name = null;
		this.id = id;
		getAutomaton()
				.distributeStateEvent(
						new AutomataStateEvent(getAutomaton(), this, false,
								false, true));
	}

	/**
	 * Returns a string representation of this object. The string representation
	 * contains the ID and the point of the state. If the ID is <CODE>5</CODE>
	 * and the point object is at <CODE>(50,80)</CODE>, then the string
	 * representation will be </CODE>"q_5 at (50,80)"</CODE>
	 */
	public String toString() {
		return "q_" + Integer.toString(getID()) + " at ("
				+ Integer.toString(getPoint().x) + ","
				+ Integer.toString(getPoint().y) + ")" + " label: "
				+ getLabel();
	}

	/**
	 * Sets the name for this state. A parameter of <CODE>null</CODE> will
	 * reset this to the default.
	 * 
	 * @param name
	 *            the new name for the state
	 */
	public void setName(String name) {
		this.name = name;
		getAutomaton()
				.distributeStateEvent(
						new AutomataStateEvent(getAutomaton(), this, false,
								false, true));
	}

	/**
	 * Returns the simple "name" for this state. By default this will simply be
	 * "qd", where d is the ID number.
	 * 
	 * @return the name for this state
	 */
	public String getName() {
		if (name == null) {
			name = "q" + Integer.toString(getID());
		}
		return name;
	}

	private String digitizer(int number) {
		if (number == 0)
			return "\u2080";
		String s = digitizer(number / 10, 1);
		return s + (SS + (char) (number % 10));
	}

	private String digitizer(int number, int supp) {
		if (number == 0)
			return "";
		String s = digitizer(number / 10, 1);
		return s + (SS + (char) (number % 10));
	}

	/**
	 * Sets the "label" for a state, an optional description for the state.
	 * 
	 * @param label
	 *            the new descriptive label, or <CODE>null</CODE> if the user
	 *            wishes to specify that there is no label
	 */
	public void setLabel(String label) {
		this.label = label;
		if (label == null) {
			labels = new String[0];
		} else {
			StringTokenizer st = new StringTokenizer(label, "\n");
			ArrayList lines = new ArrayList();
			while (st.hasMoreTokens())
				lines.add(st.nextToken());
			labels = (String[]) lines.toArray(new String[0]);
		}
		getAutomaton()
				.distributeStateEvent(
						new AutomataStateEvent(getAutomaton(), this, false,
								false, true));
	}

	/**
	 * Returns the label for the state.
	 * 
	 * @return the descriptive label of the state, or <CODE>null</CODE> if
	 *         this state has no label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the label for the state, broken across newlines if there are
	 * newlines in it.
	 * 
	 * @return an array of all label elements, or an empty array if this state
	 *         has no labels
	 */
	public String[] getLabels() {
		return labels;
	}

	
    public int specialHash(){
    	
         return point.hashCode() 
         + (myNote == null? -1 : myNote.specialHash()) 
         + (getLabel() == null ? -1 
        		 : getLabel().hashCode());
    }
	
	
	public void setNote(Note note){
		myNote = note;
	}
	
	/**
	 * Returns the color of the Node object.
	 * 
	 * @return the color of the Node object.
	 */
	public String getColor() {
		return myColor;
	}

	/**
	 * Colors the node white.
	 */
	public void colorWhite() {
		myColor = WHITE;
	}

	/**
	 * Colors the Node grey.
	 */
	public void colorGrey() {
		myColor = GREY;
	}

	/**
	 * Colors the Node black.
	 */
	public void colorBlack() {
		myColor = BLACK;
	}

	/**
	 * Returns true if the Node is white.
	 * 
	 * @return true if the Node is white.
	 */
	public boolean isWhite() {
		if (myColor.equals(WHITE))
			return true;
		return false;
	}

	/**
	 * Returns true if the Node is grey.
	 * 
	 * @return true if the Node is grey.
	 */
	public boolean isGrey() {
		if (myColor.equals(GREY))
			return true;
		return false;
	}

	/**
	 * Returns true if the Node is black.
	 * 
	 * @return true if the Node is black.
	 */
	public boolean isBlack() {
		if (myColor.equals(BLACK))
			return true;
		return false;
	}
	
	private Note myNote;

	String internalName = null;

	/** The point where this state is to be drawn. */
	private Point point;

	/** The state ID. */
	int id;

	/** The name of the state. */
	String name = null;

	/** The subscript unicode start point. */
	private static final char SS = '\u2080';

	/** The automaton this state belongs to. */
	private Automaton automaton = null;

	/** The label for the state. */
	private String label;

	/** If there are multiple labels, return those. */
	private String[] labels = new String[0];
	
	/** Color of node. */
	protected String myColor;
	
	/** String for white. */
	protected static final String WHITE = "white";

	/** String for grey. */
	protected static final String GREY = "grey";

	/** String for black. */
	protected static final String BLACK = "black";
	
	private boolean selected = false;

	public void setSelect(boolean select) {
		selected = select;
	}
	
	public boolean isSelected(){
		return selected;
	}

}
