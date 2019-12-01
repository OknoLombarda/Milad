package com.milad.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.milad.gui.components.trainings.*;

public class Training extends JDialog {
	private static final long serialVersionUID = 1L;

	private JFrame parentFrame;
	private int type;

	public Training(JFrame parent, int type) {
		super(parent, true);
		this.parentFrame = parent;
		this.type = type;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		JDialog d = this;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				int input = JOptionPane.showConfirmDialog(d, "Are you sure?", "Training", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION)
					dispose();
			}
		});
	}

	public void showDialog() {
		if (type == TrainingChooser.WT) {
			setTitle("Word — Translation");
			add(new WordTranslationTraining(parentFrame, this), BorderLayout.CENTER);
		} else if (type == TrainingChooser.TW) {
			setTitle("Translation — Word");
			add(new TranslationWordTraining(parentFrame, this), BorderLayout.CENTER);
		} else if (type == TrainingChooser.WC) {
			setTitle("Word Constructor");
			add(new WordConstructorTraining(parentFrame, this), BorderLayout.CENTER);
		} else if (type == TrainingChooser.PC) {
			setTitle("Phrase Constructor");
			add(new PhraseConstructorTraining(parentFrame, this), BorderLayout.CENTER);
		} else if (type == TrainingChooser.WCA) {
			setTitle("Word Cards");
			add(new WordCardsTraining(parentFrame, this), BorderLayout.CENTER);
		}

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}