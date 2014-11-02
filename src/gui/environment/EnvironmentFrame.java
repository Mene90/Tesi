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


import gui.editor.EditorPane;


import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * The <CODE>EnvironmentFrame</CODE> is the general sort of frame for holding
 * an environment.
 * 
 * @author Thomas Finley
 */

public class EnvironmentFrame extends JFrame {
	/**
	 * Instantiates a new <CODE>EnvironmentFrame</CODE>. This does not fill
	 * the environment with anything.
	 * 
	 * @param environment
	 *            the environment that the frame is created for
	 */
	public EnvironmentFrame(Environment environment) {
		this.environment = environment;
		initMenuBar();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(environment, BorderLayout.CENTER);

		setTitle("Tesi");
		
		this.addWindowListener(new Listener());
		this.setLocation(50, 50);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		resizeWatcher();
	}
	

	/**
	 * Initializes the menu bar for this frame.
	 */
	protected void initMenuBar() {
		this.setJMenuBar(gui.menu.MenuBarCreator.getMenuBar(this));
	}

	/**
	 * Returns the environment for this frame.
	 * 
	 * @return the environment for this frame
	 */
	public Environment getEnvironment() {
		return environment;
	}

	
	/**
	 * close an environment frame.
	 * 
	 * @return <CODE>true</CODE> if the window was successfully closed, <CODE>false</CODE>
	 *         if the window could not be closed at this time (probably user
	 *         intervention)
	 */
	public boolean close() {
		
		dispose();
		return true;
	
	}
	
	public void resizeWatcher(){
		 this.addComponentListener(new java.awt.event.ComponentAdapter()
	                {
	                        public void componentResized(ComponentEvent event)
	                        {
	                                environment.resizeSplit();
	                        }
	                });
	}

	/** The environment that this frame displays. */
	private Environment environment;

	/**
	 * The window listener for this frame.
	 */
	private class Listener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			close();
		}
	}




}
