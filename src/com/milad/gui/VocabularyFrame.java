package com.milad.gui;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.milad.MiladTools;

public class VocabularyFrame extends JDialog {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	
	public VocabularyFrame(JFrame parent) {
		super(parent, "Vocabulary", true);
		setSize(WIDTH, HEIGHT);
	}
	
	// TODO delete all following
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		MiladTools.readData();
		EventQueue.invokeLater(() -> {
			VocabularyFrame vf = new VocabularyFrame(null);
			vf.setLocationRelativeTo(null);
			vf.setVisible(true);
		});
	}
}
