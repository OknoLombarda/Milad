package com.milad.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class TrainingChooser extends JDialog {
	private static final long serialVersionUID = -958097680672382397L;
	private static final int WIDTH = 250;
	private static final int HEIGHT = 250;
	public static final int WT = 1;
	public static final int TW = 2;
	public static final int WC = 3;
	public static final int PC = 4;
	public static final int WCA = 5;
	
	private JButton wordTranslation;
	private JButton translationWord;
	private JButton wordConstructor;
	private JButton phraseConstructor;
	private JButton wordCards;
	private int choice;
	private boolean isOk;
	
	public TrainingChooser(JFrame parent) {
		super(parent, "Trainings", true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		wordTranslation = makeButton("Word — Translation", WT);
		translationWord = makeButton("Translation — Word", TW);
		wordConstructor = makeButton("Word Constructor", WC);
		phraseConstructor = makeButton("Phrase Constructor", PC);
		wordCards = makeButton("Word Cards", WCA);
		
		Dimension buttonSize = wordTranslation.getMinimumSize();
		wordTranslation.setPreferredSize(buttonSize);
		translationWord.setPreferredSize(buttonSize);
		wordConstructor.setPreferredSize(buttonSize);
		wordCards.setPreferredSize(buttonSize);
		
		add(wordTranslation, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(10).setWeight(0, 0));
		add(translationWord, new GBC(0, 1).setAnchor(GBC.CENTER).setInsets(0, 10, 10, 10).setWeight(0, 0));
		add(wordConstructor, new GBC(0, 2).setAnchor(GBC.CENTER).setInsets(0, 10, 10, 10).setWeight(0, 0));
		add(phraseConstructor, new GBC(0, 3).setAnchor(GBC.CENTER).setInsets(0, 10, 0, 10).setWeight(0, 0));
		add(wordCards, new GBC(0, 4).setAnchor(GBC.CENTER).setInsets(10).setWeight(0, 0));
	}
	
	public void showDialog() {
		choice = 0;
		isOk = false;
		setVisible(true);
	}
	
	public boolean isChosen() {
		return isOk;
	}
	
	public int getChoice() {
		return choice;
	}
	
	private JButton makeButton(String name, int option) {
		JButton button = new JButton(name);
		button.addActionListener(event -> {
			choice = option;
			isOk = true;
			setVisible(false);
		});
		return button;
	}
}
