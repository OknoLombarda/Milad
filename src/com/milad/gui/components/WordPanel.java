package com.milad.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordPanel extends JPanel {
	private static final long serialVersionUID = -1452343200950166100L;
	
	private JLabel word;
	private Dimension size;
	private boolean used;
	
	public WordPanel() {
		setBackground(new Color(102, 255, 51));
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		this.word = new JLabel();
		this.word.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		size = new Dimension(1, 1);
		add(this.word);
		used = false;
	}
	
	public WordPanel(String word) {
		this();
		setText(word);
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void setText(String text) {
		word.setText(text);
		size = new Dimension((int) this.word.getPreferredSize().getWidth() + 10, 35);
	}
	
	public String getText() {
		return word.getText();
	}
	
	public Dimension preferredSize() {
		return size;
	}
}
