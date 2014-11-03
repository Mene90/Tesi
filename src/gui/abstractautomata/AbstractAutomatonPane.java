package gui.abstractautomata;

import gui.SplitPaneFactory;
import gui.TooltipAction;
import gui.abstractgraph.GraphConversionController;
import gui.action.NoInteractionSimulateAction;
import gui.action.SubStringMatchingAction;
import gui.editor.EditorPane;
import gui.editor.ToolBox;
import gui.environment.Environment;
import gui.viewer.AutomatonDraggerPane;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;
import gui.viewer.SelectionDrawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import automata.fsa.FiniteStateAutomaton;
import automata.fsa.dag.AbstractGraph;

public class AbstractAutomatonPane extends JPanel {
	
	/**
	 * Instantiates a new <CODE>ConversionPane</CODE>.
	 * 
	 * @param nfa
	 *            the NFA we are converting to a DFA
	 * @param environment
	 *            the environment this pane will be added to
	 */
	public AbstractAutomatonPane(FiniteStateAutomaton dfa, Environment environment) {
		super(new BorderLayout());
		this.environment=environment;
		abstractautomaton = (FiniteStateAutomaton) dfa.clone();
		controller = new AbstractAutomatonConversionController(dfa, abstractautomaton, this);
		// Create the left view of the original DFA.
		AutomatonPane dfaPane = new AutomatonDraggerPane(dfa);
		// Put it all together.
		JSplitPane split = SplitPaneFactory.createSplit(environment, true, .25,
				dfaPane, createEditor(abstractautomaton));
		add(split, BorderLayout.CENTER);

	}
	
	/**
	 * Creates the editor pane for the DFA.
	 * 
	 * @param dfa
	 *            the dfa to create the editor pane for
	 */
	private EditorPane createEditor(FiniteStateAutomaton dfa) {
		SelectionDrawer drawer = new SelectionDrawer(dfa);
		editor = new EditorPane(drawer, new ToolBox() {
			public java.util.List tools(AutomatonPane view,
					AutomatonDrawer drawer) {
				java.util.List tools = new java.util.LinkedList();
				return tools;
			}
		});
		addExtras(editor.getToolBar());
		return editor;
	}

	/**
	 * Adds the extra controls to the toolbar for the editorpane.
	 * 
	 * @param toolbar
	 *            the tool bar to add crap to
	 */
	private void addExtras(JToolBar toolbar) {
		toolbar.addSeparator();
		toolbar.add(new NoInteractionSimulateAction(abstractautomaton,environment));
		toolbar.add(new TooltipAction("Convert to DFA",
				"This will convert to DFA.") {
			public void actionPerformed(ActionEvent e) {
				controller.transform();
			}
		});
	}
	
	/** */
	
	private FiniteStateAutomaton abstractautomaton;
	
	/** */
	
	private Environment environment;

	/** The controller object. */
	private AbstractAutomatonConversionController controller;

	/** The editor pane. */
	EditorPane editor;

}
