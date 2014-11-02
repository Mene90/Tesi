package gui.action;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import automata.fsa.dag.AbstractGraph;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.environment.Environment;
import automata.Automaton;

public class SubStringMatchingAction extends SimulateAction{

	public SubStringMatchingAction(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "SubString matching..");
		putValue(ACCELERATOR_KEY, null);
		this.environment = environment;	
	}
	
	/**
	 * Handles the input
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		performAction((Component)e.getSource());		
	}
	
	public void performAction(Component source){
		if(getObject() instanceof AbstractGraph){
			if (((Automaton)getObject()).getInitialState() == null) {
				JOptionPane.showMessageDialog(source,
						"Simulation requires an automaton\n"
								+ "with an initial state!", "No Initial State",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	
	/**
	 * Opens pop-up GUI for taking input. Now JFLAP can take file as an input.
	 * @param component
	 * @param title
	 * @return
	 */
	private Object openInputGUI(final Component component, String title, final int tapes) {
		// TODO Auto-generated method stub
		JPanel panel;
		JTextField[] fields;
		
		//for FA, PDA
		if (tapes==0)
		{
			panel = new JPanel(new GridLayout(3, 1));
			fields = new JTextField[1];
			for (int i = 0; i < 1; i++) {
				panel.add(new JLabel(title + " "));
				panel.add(fields[i] = new JTextField());
			}
		}
		else
		{
			panel = new JPanel(new GridLayout(tapes*2+1, 2));
			fields = new JTextField[tapes];
			for (int i = 0; i < tapes; i++) {
				panel.add(new JLabel(title + " "+(i+1)));
				panel.add(fields[i] = new JTextField());
			}
		}
		JButton jb=new JButton("Click to Open Input File");
		jb.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser ourChooser=new JFileChooser (System.getProperties().getProperty("user.dir"));
				int retval=ourChooser.showOpenDialog(null);
				File f=null;
				if (retval==JFileChooser.APPROVE_OPTION)
				{
					f=ourChooser.getSelectedFile();
					try {
						Scanner sc=new Scanner(f);
						if (tapes!=0)
						{
							String[] input = new String[tapes];
				    		for (int i = 0; i < tapes; i++)
				    		{
				    			if (sc.hasNext())
				    				input[i] = sc.next();
				    			else
				    			{
				    				JOptionPane.showMessageDialog(component, "Input file does not have enough input for all tapes", "File read error"
				    						, JOptionPane.ERROR_MESSAGE);
				    				return;
				    			}
				    		}
							JOptionPane.getFrameForComponent(component).dispose();
							handleInputFile(input);
						}
						else
						{
							String tt=sc.next();
							JOptionPane.getFrameForComponent(component).dispose();
							handleInputFile(tt);
						}
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generate catch block
						e1.printStackTrace();
					}
					
				}
				
			}
			
		});
		panel.add(jb);
		int result = JOptionPane.showOptionDialog(component, panel, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, null, null);
		if (result != JOptionPane.YES_OPTION && result != JOptionPane.OK_OPTION)
			return null;
		if (tapes==0)
		{
			String input = fields[0].getText();
			return input;
		}
		else
		{
    		String[] input = new String[tapes];
    		for (int i = 0; i < tapes; i++)
    			input[i] = fields[i].getText();
    		return input;
		}
	}
	
	/** The environment. */
	private Environment environment = null;
}
