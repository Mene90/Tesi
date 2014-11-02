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





package gui.editor;

import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

import java.util.List;


/**
 * The <CODE>DefaultToolBox</CODE> has all the tools for general editing of an
 * automaton.
 */

public class DefaultToolBox implements ToolBox {
	/**
	 * Returns a list of tools including a <CODE>ArrowTool</CODE>, <CODE>StateTool</CODE>,
	 * <CODE>TransitionTool</CODE> and <CODE>DeleteTool</CODE>, in that
	 * order.
	 * 
	 * @param view
	 *            the component that the automaton will be drawn in
	 * @param drawer
	 *            the drawer that will draw the automaton in the view
	 * @return a list of <CODE>Tool</CODE> objects.
	 */
	public List tools(AutomatonPane view, AutomatonDrawer drawer) {
		List list = new java.util.ArrayList();
		list.add(new ArrowTool(view, drawer));
		list.add(new StateTool(view, drawer));
		list.add(new TransitionTool(view, drawer));
		list.add(new DeleteTool(view, drawer));
		list.add(new UndoTool(view, drawer));
		list.add(new RedoTool(view, drawer));
		return list;
	}
}
