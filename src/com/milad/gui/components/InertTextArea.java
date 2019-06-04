package com.milad.gui.components;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.UIManager;

import com.milad.gui.MiladMainFrame;

public class InertTextArea extends JEditorPane {
	private static final long serialVersionUID = -1586542179899235222L;

	public InertTextArea(MiladMainFrame frame) {
		setHighlighter(null);
		setEditable(false);
		setBackground(UIManager.getColor("Label.background"));
		setBorder(null);
		setContentType("text/html");
		putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				frame.updateRandomWord();
			}
			
			public void mouseEntered(MouseEvent event) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
	}
	
	public void setText(String text, String color, boolean changeColor) {
		if (changeColor)
			setText("<html><center><span style=\"color:" + color + "\">" + text + "</span></center></html>");
		else
			setText("<html><center>" + text + "</center></html>");
	}
}
