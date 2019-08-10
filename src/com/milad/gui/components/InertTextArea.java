package com.milad.gui.components;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.UIManager;

import com.milad.gui.MiladMainFrame;

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
