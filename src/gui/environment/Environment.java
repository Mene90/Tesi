





package gui.environment;


import gui.action.MultipleSimulateAction.MultiplePane;
import gui.environment.tag.EditorTag;
import gui.environment.tag.Tag;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * The environment class is the central view that manages various "hangers on"
 * of an object. By "hanger on" I mean a component that has some relevance to
 * the object that this environment contains. For example, each <CODE>Environment</CODE>
 * instance has, at minimum some sort of component whereby this structure can be
 * edited. The <CODE>Environment</CODE> instance keeps track of and displays
 * these various components.
 * 
 * @see gui.environment.EnvironmentFrame
 * @see gui.environment.tag
 */

public abstract class Environment extends JPanel {


    /**
	 * Instantiates a new environment for the given object. This environment is
	 * assumed to have no native file to which to save the object. One should
	 * use the <CODE>setFile</CODE> object if this environment should have
	 * one.
	 * 
	 * @param object
	 *            assumed to be some sort of object that this environment holds;
	 *            subclasses may provide more stringent requirements for this
	 *            kind of object
	 */
	public Environment(Serializable object) {
		theMainObject = object;
		clearDirty();
		initView();
	}

	/**
	 * Returns the main object for this environment. This is the object that was
	 * passed in for the constructor.
	 * 
	 * @return the main object for this environment
	 */
	public Serializable getObject() {
		return theMainObject;
	}


	/**
	 * A helper function to set up the GUI components.
	 */
	private void initView() {
		this.setLayout(new BorderLayout());
		tabbed = new JTabbedPane();
		super.add(tabbed, BorderLayout.CENTER);
		// So that when the user changes the view by clicking in the
		// tabbed pane, this knows about it.
		tabbed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				distributeChangeEvent();
			}
		});
	}

	/**
	 * Adds a new component to the environment. Presumably this added component
	 * has some relevance to the current automaton or grammar held by the
	 * environment, though this is not strictly required.
	 * 
	 * @param component
	 *            the component to add, which should be unique for this
	 *            environment
	 * @param name
	 *            the name this component should be labeled with, which is not
	 *            necessarily a unique label
	 * @param tags
	 *            the tags associated with the component, or just a raw <CODE>Tag</CODE>
	 *            implementor if this component has no special tags associated
	 *            with it
	 * @see gui.environment.tag
	 */
	public void add(Component component, String name, Tag tags) {
		componentTags.put(component, tags);
		tabbed.addTab(name, component);

		// Takes care of the deactivation of EditorTag tagged
		// components in the event that such action is appropriate.
		if (tags instanceof gui.environment.tag.CriticalTag) {
			criticalObjects++;
			if (criticalObjects == 1)
				setEnabledEditorTagged(false);
		}

		distributeChangeEvent();
	}

	/**
	 * Returns if a particular component is part of this environment, as through
	 * addition through one of the <CODE>add</CODE> methods
	 * 
	 * @param component
	 *            the component to check for membership in this environment
	 * @see #add
	 * @see #remove
	 */
	public boolean contains(Component component) {
		return tabbed.indexOfComponent(component) != -1;
	}

	/**
	 * Deactivates or activates editor tagged objects in this environment.
	 * 
	 * @param enabled
	 *            <CODE>true</CODE> if editor tagged objects should be
	 *            enabled, <CODE>false</CODE> if editor tagged objects should
	 *            be disabled
	 */
	public void setEnabledEditorTagged(boolean enabled) {
		for (int i = 0; i < tabbed.getTabCount(); i++) {
			Component c = tabbed.getComponentAt(i);
			if (((Tag) componentTags.get(c)) instanceof EditorTag)
				tabbed.setEnabledAt(i, enabled);
		}
	}

	/**
	 * Programmatically sets the currently active component in this environment.
	 * 
	 * @param component
	 *            the component to make active
	 * @see #getActive
	 */
	public void setActive(Component component) {
		tabbed.setSelectedComponent(component);
		// The change event should be automatically distributed by the
		// model of the tabbed pane
	}

	/**
	 * Returns the currently active component in this environment.
	 * 
	 * @return the currently active component in this environment
	 * @see #setActive
	 */
	public Component getActive() {
		return tabbed.getSelectedComponent();
	}


	/**
	 * Adds a change listener to this object. The listener will receive events
	 * whenever the active component changes, or when components are made
	 * enabled or disabled, or when components are added or removed.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	/**
	 * Removes a change listener from this object.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeChangeListener(ChangeListener listener) {
		changeListeners.remove(listener);
	}

	/**
	 * Distributes a change event to all listeners.
	 */
	protected void distributeChangeEvent() {
		
		ChangeEvent e = new ChangeEvent(this);
		Iterator it = (new HashSet(changeListeners)).iterator();
		while (it.hasNext())
			((ChangeListener) it.next()).stateChanged(e);
	}
	
	/**
	 * Removes a component from this environment.
	 * 
	 * @param component
	 *            the component to remove
	 */
	public void remove(Component component) {
		tabbed.remove(component);
		Tag tag = (Tag) componentTags.remove(component);

		// Takes care of the deactivation of EditorTag tagged
		// components in the event that such action is appropriate.
		if (tag instanceof gui.environment.tag.CriticalTag) {
			criticalObjects--;
			if (criticalObjects == 0)
				setEnabledEditorTagged(true);
		}

		distributeChangeEvent();
	}

	/**
	 * Returns the tag for a given component, provided this tag is in the
	 * component.
	 * 
	 * @param component
	 *            the component to get the tag for
	 * @return the tag for the component
	 */
	public Tag getTag(Component component) {
		return (Tag) componentTags.get(component);
	}


	/**
	 * Returns if this environment dirty. An environment is called dirty if the
	 * object it holds has been modified since the last save.
	 * 
	 * @return <CODE>true</CODE> if the environment is dirty, <CODE>false</CODE>
	 *         otherwise
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the dirty bit. This should be called if the object is changed.
	 */
	public void setDirty() {
		dirty = true;
	}

	/**
	 * Clears the dirty bit. This should be called when the object is saved to a
	 * file, or is in some other such state that a save is not required.
	 */
	public void clearDirty() {
		dirty = false;
	}
	
	
	public void resizeSplit(){
		if(myObjects != null && this.tabbed != null){
			if(myObjects.size() > 0 && this.tabbed.getTabCount() == 1){
				Component cur = this.getActive();
				if(cur instanceof MultiplePane){
					MultiplePane mult = (MultiplePane)cur;
					mult.mySplit.setDividerLocation(.5);
				}
			}
		}
		
	}
	
	
    /**For Testing multiple objects*/
    public ArrayList myObjects;
    public ArrayList myTestStrings;
    public ArrayList myTransducerStrings;

	/** The mapping of components to their respective tag objects. */
	private HashMap componentTags = new HashMap();

	/** The tabbed pane for this environment. */
	public JTabbedPane tabbed;

	/** The collection of change listeners for this object. */
	private transient HashSet changeListeners = new HashSet();

	/** The object that this environment centers on. */
	private Serializable theMainObject;

	/**
	 * The number of "CriticalTag" tagged components. Hokey but fast.
	 */
	private int criticalObjects = 0;

	/** The dirty bit. */
	private boolean dirty = false;
}
