package com.milad.gui;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.milad.MiladTools;
import com.milad.ResourceLoader;
import com.milad.gui.components.VocabularyWordPanel;

public class VocabularyFrame extends JDialog {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	public VocabularyFrame(JFrame parent) {
		super(parent, "Vocabulary", true);
		setSize(WIDTH, HEIGHT);
	}
	
	public void showFrame() {
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// TODO delete all following
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		MiladTools.readData();
		ResourceLoader.loadResources();
		
		EventQueue.invokeLater(() -> {
			VocabularyFrame vf = new VocabularyFrame(null);
			vf.showFrame();
		});
	}
}
