package gui.abstractgraph;

import gui.SplitPaneFactory;
import gui.TooltipAction;
import gui.editor.ArrowNontransitionTool;
import gui.editor.EditorPane;
import gui.editor.ToolBox;
import gui.environment.Environment;
import gui.viewer.AutomatonDraggerPane;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;
import gui.viewer.SelectionDrawer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import automata.fsa.FiniteStateAutomaton;
import automata.fsa.dag.AbstractGraph;

/**
 * This is the pane where the user defines all that is needed for the conversion
 * of an NFA to a DFA.
 * 
 * @author Thomas Finley
 */

public class GraphPane extends JPanel {
	/**
	 * Instantiates a new <CODE>ConversionPane</CODE>.
	 * 
	 * @param nfa
	 *            the NFA we are converting to a DFA
	 * @param environment
	 *            the environment this pane will be added to
	 */
	public GraphPane(FiniteStateAutomaton dfa, Environment environment) {
		super(new BorderLayout());
		AbstractGraph abstractgraph = new AbstractGraph();
		convert = new GraphConversionController(dfa, abstractgraph, this);
		// Create the left view of the original DFA.
		AutomatonPane dfaPane = new AutomatonDraggerPane(dfa);
		// Put it all together.
		JSplitPane split = SplitPaneFactory.createSplit(environment, true, .25,
				dfaPane, createEditor(abstractgraph));
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
				//tools.add(new ArrowNontransitionTool(view, drawer));
				//tools.add(new TransitionExpanderTool(view, drawer, controller));
				//tools.add(new StateExpanderTool(view, drawer, controller));
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
		toolbar.add(new TooltipAction("Input",null) {
			public void actionPerformed(ActionEvent e) {
				//convert.complete();
			}
		});
	}

	/** The controller object. */
	private GraphConversionController convert;

	/** The editor pane. */
	EditorPane editor;
}
