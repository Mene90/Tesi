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

import automata.fsa.FSAStepByStateSimulator;
import gui.JTableExtender;
import gui.SplitPaneFactory;
import gui.TableTextSizeSlider;
import gui.editor.ArrowDisplayOnlyTool;
import gui.editor.EditorPane;
import gui.environment.AutomatonEnvironment;
import gui.environment.Environment;
import gui.environment.EnvironmentFactory;
import gui.environment.EnvironmentFrame;
import gui.environment.FrameFactory;
import gui.environment.Profile;
import gui.environment.Universe;
import gui.environment.EnvironmentFactory.EditorPermanentTag;
import gui.environment.tag.CriticalTag;
import gui.environment.tag.Tag;
import gui.sim.TraceWindow;
import gui.sim.multiple.InputTableModel;
import gui.viewer.AutomatonPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;

import automata.Automaton;
import automata.AutomatonSimulator;
import automata.Configuration;
import automata.NondeterminismDetector;
import automata.NondeterminismDetectorFactory;
import automata.SimulatorFactory;
import automata.State;


/**
 * This is the action used for the simulation of multiple inputs on an automaton
 * with no interaction. This method can operate on any automaton.
 * 
 * @author Thomas Finley
 * @modified by Kyung Min (Jason) Lee
 */

public class MultipleSimulateAction extends NoInteractionSimulateAction {
	/**
	 * Instantiates a new <CODE>MultipleSimulateAction</CODE>.
	 * 
	 * @param automaton
	 *            the automaton that input will be simulated on
	 * @param environment
	 *            the environment object that we shall add our simulator pane to
	 */
	public MultipleSimulateAction(Automaton automaton, Environment environment) {
		super(automaton, environment);
		putValue(NAME, "Multiple Run");
        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M,
               // MAIN_MENU_MASK));
	}

	
	/**
	 * Returns the title for the type of compontent we will add to the
	 * environment.
	 * 
	 * @return in this base class, returns "Multiple Inputs"
	 */
	public String getComponentTitle() {
		return "Multiple Run";
	}
	
	/**
	 * This will search configurations for an accepting configuration.
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param simulator
	 *            the automaton simulator for this automaton
	 * @param configs
	 *            the initial configurations generated from a single input
	 * @param initialInput
	 *            the object that represents the initial input;
	 * @param associatedConfigurations
	 *            the first accepting configuration encountered will be added to
	 *            this list, or the last configuration considered if there was
	 *            no accepted configuration
	 * @return <CODE>0</CODE> if this was an accept, <CODE>1</CODE> if
	 *         reject, and <CODE>2</CODE> if the user cancelled the run
	 */
	protected int handlematching(Automaton automaton, AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput) {

		if(simulator.simulate(configs[0])){
			System.out.println("ACCEPT: " + (String)initialInput);
			return 0;
		}
		
		System.out.println("REJECT: " + (String)initialInput);

		return 1;
	}

	/**
	 * This will search configurations for an accepting configuration.
	 * 
	 * @param automaton
	 *            the automaton input is simulated on
	 * @param simulator
	 *            the automaton simulator for this automaton
	 * @param configs
	 *            the initial configurations generated from a single input
	 * @param initialInput
	 *            the object that represents the initial input;
	 * @param associatedConfigurations
	 *            the first accepting configuration encountered will be added to
	 *            this list, or the last configuration considered if there was
	 *            no accepted configuration
	 * @return <CODE>0</CODE> if this was an accept, <CODE>1</CODE> if
	 *         reject, and <CODE>2</CODE> if the user cancelled the run
	 */
	protected int handleInput(Automaton automaton, AutomatonSimulator simulator, Configuration[] configs,
			Object initialInput, List associatedConfigurations) {
		
		JFrame frame = Universe.frameForEnvironment(getEnvironment());
		// How many configurations have we had?
		int numberGenerated = 0;
		// When should the next warning be?
		int warningGenerated = WARNING_STEP;
		Configuration lastConsidered = configs[configs.length - 1];
		while (configs.length > 0) {
			numberGenerated += configs.length;
			// Make sure we should continue.
			if (numberGenerated >= warningGenerated) {
				if (!confirmContinue(numberGenerated, frame)) {
					associatedConfigurations.add(lastConsidered);
					return 2;
				}
				while (numberGenerated >= warningGenerated)
					warningGenerated *= 2;
			}
			
			// Get the next batch of configurations.
			ArrayList next = new ArrayList();
			for (int i = 0; i < configs.length; i++) {
			
				lastConsidered = configs[i];
				if (configs[i].isAccept()) {
					associatedConfigurations.add(configs[i]);
					return 0;
				} else {					
					next.addAll(simulator.stepConfiguration(configs[i]));
				}
			}
			configs = (Configuration[]) next.toArray(new Configuration[0]);
		}
		associatedConfigurations.add(lastConsidered);
		return 1;
	}

	/**
	 * Provides an initialized multiple input table object.
	 * 
	 * @param obj
	 *            the automaton to provide the multiple input table for
	 * @return a table object for this automaton
	 * @see gui.sim.multiple.InputTableModel
	 */
	protected JTableExtender initializeTable(Object obj) {
		//System.out.println("In regular multiple initialize");
		boolean multiple = false;
		int inputCount = 0;
        if(this.getEnvironment().myObjects!=null){
        	multiple = true;
        	inputCount = 1;
        }
        //System.out.println("In initialize:" + multiple);
        InputTableModel model = null;
        if(getObject() instanceof Automaton){
        	model = InputTableModel.getModel((Automaton)getObject(), multiple);
        }
		JTableExtender table = new JTableExtender(model, this);
		// In this regular multiple simulate pane, we don't care about
		// the outputs, so get rid of them.
		TableColumnModel tcmodel = table.getColumnModel();
		
		inputCount += model.getInputCount();
		for (int i = model.getInputCount(); i > 0; i--) {
			tcmodel.removeColumn(tcmodel.getColumn(inputCount));
		}
		if(multiple){
            ArrayList autos  = this.getEnvironment().myObjects;
            //System.out.println("In initialize: " + autos.size());
            ArrayList strings = this.getEnvironment().myTestStrings;
            int offset = strings.size();
            int row = 0;
            for(int m = 0; m < autos.size(); m++){      
                for(int k = 0; k < strings.size(); k++){
                    row = k+offset*m;
                    Object currentObj = autos.get(m);
                    if(currentObj instanceof Automaton){
                    	model.setValueAt(((Automaton)currentObj).getFileName(), row, 0); 
                    	 model.setValueAt((String)strings.get(k), row, 1);                    	
                    }          
                }
                
            }
            while((model.getRowCount()-1) > (autos.size()*strings.size())){
            	model.deleteRow(model.getRowCount()-2);
            }
		}
		// Set up the last graphical parameters.
		table.setShowGrid(true);
		table.setGridColor(Color.lightGray);
		return table;
	}
	
	public void performAction(Component source){
		if(getObject() instanceof Automaton){
			if (((Automaton)getObject()).getInitialState() == null) {
				JOptionPane.showMessageDialog(source,
						"Simulation requires an automaton\n"
								+ "with an initial state!", "No Initial State",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

        table = initializeTable(getObject());

        
		if(((InputTableModel)table.getModel()).isMultiple){
			getEnvironment().remove(getEnvironment().getActive());
		}
		
		JPanel panel = new JPanel(new BorderLayout());
		JToolBar bar = new JToolBar();
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(bar, BorderLayout.SOUTH);
		panel.add(new TableTextSizeSlider(table), BorderLayout.NORTH);
	
		//Load inputs
    	bar.add(new AbstractAction("string matching"){
    		public void actionPerformed(ActionEvent arg0) {
    			try {
					// Make sure any recent changes are registered.
					table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
				InputTableModel model = (InputTableModel) table.getModel();
				
				if(getObject() instanceof Automaton){
	                Automaton currentAuto = (Automaton)getObject();
					AutomatonSimulator simulator = SimulatorFactory.getSimulator(currentAuto);
					
					String[][] inputs = model.getInputs();
	                int uniqueInputs = inputs.length;
	                int tapes = 1;
	                if(model.isMultiple){
	                	uniqueInputs = getEnvironment().myTestStrings.size()/tapes;
	                }
					for (int r = 0; r < inputs.length; r++) {
	                    if(r>0){
	                        if(r%uniqueInputs==0){
	                            currentAuto = (Automaton)getEnvironment().myObjects.get(r/uniqueInputs);
	                           
	                            simulator = SimulatorFactory.getSimulator(currentAuto);                         
	                        }
	                    }
						Configuration[] configs = null;
						Object input = null;
						
						configs = simulator.getInitialConfigurations(inputs[r][0]);
						input = inputs[r][0];
						
						int result = handlematching(currentAuto, simulator, configs, input);

						}
						
					}		
            }
        	});
		// Add the running input thing.
		bar.add(new AbstractAction("Run Inputs") {
			public void actionPerformed(ActionEvent e) {
				try {
					// Make sure any recent changes are registered.
					table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
				InputTableModel model = (InputTableModel) table.getModel();
				
				if(getObject() instanceof Automaton){
	                Automaton currentAuto = (Automaton)getObject();
					AutomatonSimulator simulator = SimulatorFactory
							.getSimulator(currentAuto);
					//raccolgo le stringe inserite nel model
					String[][] inputs = model.getInputs();
	                int uniqueInputs = inputs.length;
	                int tapes = 1;
	                if(model.isMultiple){
	                	uniqueInputs = getEnvironment().myTestStrings.size()/tapes;
	                }
					for (int r = 0; r < inputs.length; r++) {
	                    if(r>0){
	                        if(r%uniqueInputs==0){
	                            currentAuto = (Automaton)getEnvironment().myObjects.get(r/uniqueInputs);
	                           
	                            simulator = SimulatorFactory.getSimulator(currentAuto);                         
	                        }
	                    }
						Configuration[] configs = null;
						Object input = null;
						
						configs = simulator.getInitialConfigurations(inputs[r][0]);
						input = inputs[r][0];
						
						List associated = new ArrayList();
						
						//manipolo l'automa per controllare l'ultima configuraizone
						
						int result = handleInput(currentAuto, simulator, configs, input, associated);
						Configuration c = null;
						if (associated.size() != 0)
							c = (Configuration) associated.get(0);

                        model.setResult(r, RESULT[result], c, getEnvironment().myTransducerStrings, (r%(uniqueInputs))*(tapes+1));
					}
				}
			}
			
		});
		if(!((InputTableModel)table.getModel()).isMultiple){
		// Add the clear button.
		bar.add(new AbstractAction("Clear") {
			public void actionPerformed(ActionEvent e) {
				try {
					// Make sure any recent changes are registered.
					table.getCellEditor().stopCellEditing();
				} catch (NullPointerException exception) {
					// We weren't editing anything, so we're OK.
				}
				InputTableModel model = (InputTableModel) table.getModel();              
				model.clear();
			}
		});
		
        /*
         * So that it will show up as Lambday or Epsilon, depending on the
         * profile. Sorry about the cheap hack. 
         * 
         * Jinghui Lim
         */
        String empty = "Lambda";
        if(Universe.curProfile.getEmptyString().equals(Profile.LAMBDA))
            empty = "Lambda";
        else if(Universe.curProfile.getEmptyString().equals(Profile.EPSILON))
            empty = "Epsilon";
		bar.add(new AbstractAction("Enter " + empty/*"Enter Lambda"*/) {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row == -1)
					return;
				for (int column = 0; column < table.getColumnCount() - 1; column++)
					table.getModel().setValueAt("", row, column);
			}
		});
		}
		if(getObject() instanceof Automaton) {
			bar.add(new AbstractAction("View Trace") {
			public void actionPerformed(ActionEvent e) {
				int[] rows = table.getSelectedRows();
				InputTableModel tm = (InputTableModel) table.getModel();
				List nonassociatedRows = new ArrayList();
				for (int i = 0; i < rows.length; i++) {
					if (rows[i] == tm.getRowCount() - 1)
						continue;
					Configuration c = tm
							.getAssociatedConfigurationForRow(rows[i]);
					if (c == null) {
						nonassociatedRows.add(new Integer(rows[i] + 1));
						continue;
					}
					TraceWindow window = new TraceWindow(c);
					window.setVisible(true);
					window.toFront();
				}
				// Print the warning message about rows without
				// configurations we could display.setValueAt
				if (nonassociatedRows.size() > 0) {
					StringBuffer sb = new StringBuffer("Row");
					if (nonassociatedRows.size() > 1)
						sb.append("s");
					sb.append(" ");
					sb.append(nonassociatedRows.get(0));
					for (int i = 1; i < nonassociatedRows.size(); i++) {
						if (i == nonassociatedRows.size() - 1) {
							// Last one!
							sb.append(" and ");
						} else {
							sb.append(", ");
						}
						sb.append(nonassociatedRows.get(i));
					}
					sb.append("\ndo");
					if (nonassociatedRows.size() == 1)
						sb.append("es");
					sb.append(" not have end configurations.");
					JOptionPane.showMessageDialog((Component) e.getSource(), sb
							.toString(), "Bad Rows Selected",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		}
		if(((InputTableModel)table.getModel()).isMultiple){
		    
		    bar.add(new AbstractAction("Edit File"){
		        public void actionPerformed(ActionEvent arg0) {		            
		            int k = getMachineIndexBySelectedRow(table);
		            if(k>=0 && k < getEnvironment().myObjects.size()){
		                if(getObject() instanceof Automaton){
		                    Automaton cur = (Automaton)getEnvironment().myObjects.get(k);
		                    EditorPane ep = new EditorPane(cur);
		                    ep.setName(cur.getFileName());
		                    getEnvironment().add(ep, "Edit", new CriticalTag() {
		                    });
		                    getEnvironment().setActive(ep);          
		                }
                    }	            
		        }
		    });
		    
        	bar.add(new AbstractAction("Add input string"){
        		public void actionPerformed(ActionEvent arg0) {
                    //add input
                    int inputsNeeded = 1;
                    boolean turing = false;
        			
                        Object input = initialInput((Component) getEnvironment().getActive(), "Input");
    
            			if(input instanceof String){
            				String s = (String)input;
            				((ArrayList)getEnvironment().myTestStrings).add(s);
            			}
            			else if(input instanceof String[]){
            				String[] s = (String[]) input;
                            for(int k = 0; k < s.length; k++){
                                ((ArrayList)getEnvironment().myTestStrings).add(s[k]);
                            }
            			}
                        else return;
                    
                    //add expected output
                    if(turing){
                            Object output = initialInput((Component) getEnvironment().getActive(), "Expected Output?");
        
    
                            if(output instanceof String){
                                String s = (String)output;
                                ((ArrayList)getEnvironment().myTransducerStrings).add(s);
                            }
                            else if(output instanceof String[]){
                                String[] s = (String[]) output;
                                for(int k = 0; k < s.length; k++){
                                    ((ArrayList)getEnvironment().myTransducerStrings).add(s[k]);
                                }
                            }
                            else{
                                getEnvironment().myTestStrings.remove(getEnvironment().myTestStrings.size()-1);
                                return;
                            }
                        
                    }
                    //add expected result
                    Object result = initialInput((Component) getEnvironment().getActive(), "Expected Result? (Accept or Reject)");

                    if(result instanceof String){
                        String s = (String)result;
                        ((ArrayList)getEnvironment().myTransducerStrings).add(s);
                    }
                    else if(result instanceof String[]){
                        String[] s = (String[]) result;
                        ((ArrayList)getEnvironment().myTransducerStrings).add(s[0]);
                    }
                    else {
                        getEnvironment().myTestStrings.remove(getEnvironment().myTestStrings.size()-1);
                        getEnvironment().myTransducerStrings.remove(getEnvironment().myTestStrings.size()-1);
                        return;
                    }
                    
        			getEnvironment().remove(getEnvironment().getActive());
                    performAction(getEnvironment().getActive());
                    
                }
            	});
        	      	
        
        }
        
        myPanel = panel;
		// Set up the final view.
        Object finObject = getObject();
        if(finObject instanceof Automaton){
        	AutomatonPane ap = new AutomatonPane((Automaton)finObject);
        	ap.addMouseListener(new ArrowDisplayOnlyTool(ap, ap.getDrawer()));
        	JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
				0.5, ap, panel);
        	MultiplePane mp = new MultiplePane(split);
        	getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
    		});
    		getEnvironment().setActive(mp);
        }
        		
	}
	
	private int getMachineIndexBySelectedRow(JTable table){
		InputTableModel model = (InputTableModel) table.getModel();
        int row = table.getSelectedRow();
        if(row < 0) return -1;
        String machineFileName = (String)model.getValueAt(row, 0);
        return getMachineIndexByName(machineFileName);
	}
	
	public int getMachineIndexByName(String machineFileName){
	        ArrayList machines = getEnvironment().myObjects;
	        if(machines == null) return -1;
	        for(int k = 0; k < machines.size(); k++){            
	            Object current = machines.get(k);
	            if(current instanceof Automaton){
	            	Automaton cur = (Automaton)current;
	            	if(cur.getFileName().equals(machineFileName)){
	            		return k;
	                }
	            }
	        }
	        return -1;
	}
	
	public void viewAutomaton(JTableExtender table){
		InputTableModel model = (InputTableModel) table.getModel();
		 if(model.isMultiple){        	 			
	         int row = table.getSelectedRow();
	         if(row < 0) return;
	         String machineFileName = (String)model.getValueAt(row, 0);           
	         updateView(machineFileName, (String)model.getValueAt(row, 1), table); 
		 }
       else if(this.getEnvironment().getObject() instanceof Automaton){
       updateView(((Automaton)this.getEnvironment().getObject()).getFileName(), (String)model.getValueAt(table.getSelectedRow(), 1), table);
   }
		
	}
	
	/**
	 * Handles the creation of the multiple input pane.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		performAction((Component)e.getSource());		
	}

	/**
	 * @param machineFileName
     * 
     */
    protected void updateView(String machineFileName, String input, JTableExtender table) {
        ArrayList machines = this.getEnvironment().myObjects;
        Object current = null;
        if(machines != null) current = machines.get(0);
        else current = this.getEnvironment().getObject();
            if(current instanceof Automaton && ((InputTableModel)table.getModel()).isMultiple){
            	int spot = this.getMachineIndexBySelectedRow(table);
            	Automaton cur = null;
            	if(spot != -1) cur = (Automaton)machines.get(spot);
            	else cur = (Automaton)this.getEnvironment().getObject();
                
                    AutomatonPane newAP = new AutomatonPane(cur);
                    newAP.addMouseListener(new ArrowDisplayOnlyTool(newAP, newAP.getDrawer()));
                    JSplitPane split = SplitPaneFactory.createSplit(getEnvironment(), true,
                            0.5, newAP, myPanel);
                    MultiplePane mp = new MultiplePane(split);
                    
                    EnvironmentFrame frame = Universe.frameForEnvironment(getEnvironment());
                    String newTitle = cur.getFileName();
                    if(newTitle != "") frame.setTitle(newTitle);
                    getEnvironment().remove(getEnvironment().getActive());
     
                    
                    getEnvironment().add(mp, getComponentTitle(), new CriticalTag() {
                    });
                    getEnvironment().setActive(mp);
                
            }
    }

    /**
	 * This auxillary class is convenient so that the help system can easily
	 * identify what type of component is active according to its class.
	 */
	public class MultiplePane extends JPanel {
		public MultiplePane(JSplitPane split) {
			super(new BorderLayout());
			add(split, BorderLayout.CENTER);
			mySplit = split;
		}
		public JSplitPane mySplit = null;
	}
	protected JTable table = null;
    
	private static String[] RESULT = { "Accept", "Reject", "Cancelled" };
    
    protected JPanel myPanel = null;

	private static Color[] RESULT_COLOR = { Color.green, Color.red, Color.black };
}
