package com.milad.gui.components.trainings;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.milad.MiladTools;
import com.milad.Word;

public class WordConstructorTraining extends JPanel {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private JLabel word;
	private JLabel transcription;
	private JButton check;
	private JButton next;
	
	private ArrayList<String> results;
	private ArrayList<Word> words;
	private Iterator<Word> wordIter;
	
	private boolean isWrong;
	
	public WordConstructorTraining(JFrame parentFrame, JDialog ancestor) {
		setLayout(new GridBagLayout());
		words = new ArrayList<>();
	}
	
	public void updatePanel() {
		
	}
	
	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	// TODO delete all following
	{
		try {
			MiladTools.readData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JDialog d = new JDialog();
			JFrame f = new JFrame();
			d.add(new WordConstructorTraining(f, d));
			d.pack();
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		});
	}
}
