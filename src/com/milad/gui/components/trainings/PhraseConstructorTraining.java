package com.milad.gui.components.trainings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.milad.MiladTools;

public class PhraseConstructorTraining extends JPanel {
	private final static int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	public PhraseConstructorTraining(JFrame parentFrame, JDialog ancestor) {
		
	}
	
	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	// delete all following
	
	public static void main(String[] args) throws IOException {
		MiladTools.readData();
		
		EventQueue.invokeLater(() -> {
			JFrame f = new JFrame();
			JDialog d = new JDialog();
			d.add(new PhraseConstructorTraining(f, d), BorderLayout.CENTER);
			d.pack();
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		});
	}
}
