package com.milad.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.milad.gui.components.trainings.WordTranslationTraining;

public class Training extends JDialog {
	private static final long serialVersionUID = -2087016999805920494L;

	private JFrame parentFrame;
	private int type;
	
	public Training(JFrame parent, int type) {
		super(parent, true);
		this.parentFrame = parent;
		this.type = type;
		setSize(400, 400);
	}
	
	public void showDialog() {
		for (Component comp : getComponents())
			if (comp.getClass().isAssignableFrom(JPanel.class))
				remove(comp);
		
		if (type == TrainingChooser.WT) {
			setTitle("Word — Translation");
			add(new WordTranslationTraining(parentFrame, this), BorderLayout.CENTER);
		}
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void setType(int type) {
		this.type = type;
	}
}