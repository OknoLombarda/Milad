package com.milad.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.milad.MiladTools;
import com.milad.Word;

public class MiladMainFrame extends JFrame {
	private static final long serialVersionUID = 3064265991964266671L;
	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 400;
	
	private JButton practice;
	private JButton vocabulary;
	private JButton settings;
	private JButton about;
	private JButton exit;
	private InertTextArea title;
	private InertTextArea randomWord;
	private InertTextArea transcription;
	private InertTextArea translation;
	private InertTextArea usage;
	
	public MiladMainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		practice = new JButton("Practice");
		vocabulary = new JButton("Vocabulary");
		settings = new JButton("Settings");
		about = new JButton("About");
		exit = new JButton("Exit");
		
		Dimension buttonSize = vocabulary.getPreferredSize();
		practice.setPreferredSize(buttonSize);
		settings.setPreferredSize(buttonSize);
		about.setPreferredSize(buttonSize);
		exit.setPreferredSize(buttonSize);
		
		exit.addActionListener(event -> System.exit(0));
		about.addActionListener(event -> {
			JOptionPane.showMessageDialog(this, "<html><h1>TEST</h1></html>", "About", JOptionPane.PLAIN_MESSAGE);
		});

		buttonPanel.add(practice, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(5).setWeight(0, 0));
		buttonPanel.add(vocabulary, new GBC(0, 1).setAnchor(GBC.CENTER).setInsets(5).setWeight(0, 0));
		buttonPanel.add(settings, new GBC(0, 2).setAnchor(GBC.CENTER).setInsets(5).setWeight(0, 0));
		buttonPanel.add(about, new GBC(0, 3).setAnchor(GBC.CENTER).setInsets(5).setWeight(0, 0));
		buttonPanel.add(exit, new GBC(0, 4).setAnchor(GBC.CENTER).setInsets(5).setWeight(0, 0));
		
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new GridBagLayout());
		
		wordPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				updateRandomWord();
			}
			
			public void mouseEntered(MouseEvent event) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseExited(MouseEvent event) {
				setCursor(Cursor.getDefaultCursor());
			}
		});
		
		title = new InertTextArea(this);
		title.setText("Random word", "gray", true);
		randomWord = new InertTextArea(this);
		transcription = new InertTextArea(this);
		translation = new InertTextArea(this);
		usage = new InertTextArea(this);
		
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		randomWord.setFont(new Font("Georgia", Font.ITALIC + Font.BOLD, 30));
		transcription.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		translation.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		usage.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		
		updateRandomWord();
		
		wordPanel.add(randomWord, new GBC(0, 2).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setInsets(5, 10, 0, 10).setWeight(100, 100));
		wordPanel.add(transcription, new GBC(0, 3).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setInsets(0, 10, 0, 10).setWeight(100, 100));
		wordPanel.add(translation, new GBC(0, 4).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setInsets(10).setWeight(100, 100));
		wordPanel.add(usage, new GBC(0, 5).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setInsets(10).setWeight(100, 100));
		
		add(title, new GBC(0, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setInsets(90, 10, 0, 10).setWeight(100, 0));
		add(wordPanel, new GBC(0, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setInsets(30, 10, 10, 10).setWeight(100, 100));
		add(buttonPanel, new GBC(1, 0, 1, 2).setAnchor(GBC.EAST).setInsets(10, 0, 10, 15).setWeight(0, 0));
	}
	
	public void updateRandomWord() {
		Word word = MiladTools.getRandomWord();

		randomWord.setText(word.getWord(), "", false);
		
		if (word.hasTranscription())
			transcription.setText(word.getTranscription(), "", false);
		else
			transcription.setText("<br>", "", false);
		
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = word.getTranslations().iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			if (iter.hasNext())
				sb.append(", ");
		}
		translation.setText(sb.toString(), "", false);
		
		if (word.hasUsage())
			usage.setText(word.getUsage(), "", false);
		else
			usage.setText("<br>", "", false);
	}
}
