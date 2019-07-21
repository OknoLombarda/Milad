package com.milad.gui.components.trainings;

import java.awt.Frame;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class AbstractTraining extends JPanel {
	private static final long serialVersionUID = -3371833241885383061L;
	
	protected List<String> results;
	private JFrame parentFrame;
	private JDialog ancestor;
	
	public AbstractTraining(JFrame parentFrame, JDialog ancestor) {
		this.parentFrame = parentFrame;
		this.ancestor = ancestor;
		initialize(true);
	}
	
	public final String getResults() {
		StringBuilder sb = new StringBuilder();
		for (String s : results)
			sb.append(s);
		sb.append("</html>");
		return sb.toString();
	}
	
	public void showResultDialog() {
		int input = ResultDialog.showDialog(ancestor, getResults());
		if (input == ResultDialog.OK_OPTION) {
			completeTraining();
		}
		else if (input == ResultDialog.REPEAT_OPTION) {
			initialize(false);
		}
	}
	
	public void completeTraining() {
		ancestor.dispose();
		parentFrame.setState(Frame.NORMAL);
	}
	
	public abstract void initialize(boolean firstTime);
	
	public abstract void updatePanel();
}
