package com.milad.gui.components;

import javax.swing.JEditorPane;
import javax.swing.UIManager;

public class InertTextArea extends JEditorPane {
	private static final long serialVersionUID = -1586542179899235222L;

	public InertTextArea() {
		setHighlighter(null);
		setEditable(false);
		setBackground(UIManager.getColor("Label.background"));
		setBorder(null);
		setContentType("text/html");
		setFocusable(false);
		putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
	}
	
	public InertTextArea(String text) {
		this();
		setText(text);
	}
}
