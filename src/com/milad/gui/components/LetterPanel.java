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
	private boolean empty;
	
	public LetterPanel() {
		letter = new JLabel();
		letter.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		letter.setBorder(BorderFactory.createEmptyBorder( -3, 0, 0, 0 ));
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
		this.letter.setText(letter.contains("<html>") ? letter : "<html><p color=\"darkgreen\">" + letter + "</p></html>");
		empty = false;
	}
	
	public String getLetter() {
		return letter.getText();
	}
	
	public void setEmpty() {
		setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		letter.setText(" ");
		empty = true;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	public boolean isIdenticalTo(Object other) {
		if (this == other)
			return true;
		
		if (other == null)
			return false;
		
		if (getClass() != other.getClass())
			return false;
		
		LetterPanel otherPanel = (LetterPanel) other;
		
		if (empty) {
			if (otherPanel.isEmpty())
				return true;
			else
				return false;
		} else {
			return getLetter().equals(otherPanel.getLetter());
		}
	}
	
	public Dimension preferredSize() {
		return new Dimension(SIZE, SIZE);
	}
}
