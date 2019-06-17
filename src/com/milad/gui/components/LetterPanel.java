package com.milad.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LetterPanel extends JPanel {
	private static final long serialVersionUID = 505216566888925403L;
	private static final int SIZE = 30;
	
	private JLabel letter;
	
	public LetterPanel() {
		letter = new JLabel();
		letter.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		add(letter);
		setEmpty();
	}
	
	public LetterPanel(String letter) {
		this();
		setLetter(letter);
	}
	
	public void setLetter(String letter) {
		setBackground(Color.GREEN);
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		String text = letter.contains("<html>") ? letter : "<html><p color=\"darkgreen\">" + letter + "</p></html>";
		this.letter.setText(text);
	}
	
	public String getLetter() {
		return letter.getText();
	}
	
	public void setEmpty() {
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		letter.setText("");
	}
	
	public Dimension preferredSize() {
		return new Dimension(SIZE, SIZE);
	}
}
