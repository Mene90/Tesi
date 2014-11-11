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





package gui.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import automata.Automaton;
import automata.State;

import gui.editor.EditorPane;
import gui.environment.Environment;
import gui.environment.tag.PermanentTag;
import gui.environment.tag.Tag;

/**
 * The <code>CloseButton</code> is a button for removing tabs in
 * an environment. It automatically detects changes in the activation 
 * of panes in the environment, and changes its enabledness whether 
 * a pane in the environment is permanent (i.e. should not be closed).
 * 
 * @see CloseAction
 * @author Jinghui Lim
 *
 */
public class CloseButton extends javax.swing.JButton 
{
	 /**
     * The environment to handle closing tabs for.
     */
    private Environment env;
    
    /**
     * Instantiates a <code>CloseButton</code>, and sets its values
     * with {@link #setDefaults()}.
     * 
     * @param environment the environment to handle the closing for
     */
    public CloseButton(Environment environment) 
    {
        super();
        setDefaults();
        env = environment;
        env.addChangeListener(new ChangeListener() 
            {
                public void stateChanged(ChangeEvent e) 
                { 
                    checkEnabled(); 
                }
            });
        addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent e) 
                {
                    boolean editor = false;
                    Automaton inside = null;
                    State block = null;
                                      
                    env.remove(env.getActive());
                    if(editor) 
                    {
                        EditorPane higherEditor = (EditorPane) env.getActive();
                    }
                }
            });
        checkEnabled();
    }
    
    /**
     * A convenience method that sets the button with certian values. 
     * The icon, size, and tooltip are set.
     *
     */
    public void setDefaults() 
    {
        setIcon(new ImageIcon(getClass().getResource("/ICON/x.gif")));
        setPreferredSize(new Dimension(22, 22));
        setToolTipText("Dismiss Tab");
    }

    /**
     * Checks the environment to see if the currently active object
     * has the <CODE>PermanentTag</CODE> associated with it, and if it
     * does, disables this action; otherwise it makes it activate.
     */
    private void checkEnabled() 
    {
        Tag tag = env.getTag(env.getActive());
//        setEnabled(!(tag instanceof PermanentTag));
        System.out.println("E': \n");
        if(env.tabbed.getTabCount() == 1)
            setEnabled(false);
        else 
            setEnabled(!(tag instanceof PermanentTag));
    }
}
