package com.milad.gui.components.trainings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class ResultDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public static final int OK_OPTION = 0;
	public static final int REPEAT_OPTION = 1;
	
	private JLabel resultLabel;
	private JButton ok;
	private JButton repeat;
	private int option;
	
	private ResultDialog(JDialog parent, String message) {
		super(parent, "Results", true);
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		setLayout(layout);
		option = OK_OPTION;
		
		JPanel labelPanel = new JPanel();
		resultLabel = new JLabel(message);
		labelPanel.add(resultLabel);
		
		JPanel buttonPanel = new JPanel();
		ok = new JButton("OK");
		repeat = new JButton("Repeat");
		ok.addActionListener(event -> dispose());
		repeat.addActionListener(event -> {
			option = REPEAT_OPTION;
			dispose();
		});
		buttonPanel.add(repeat);
		buttonPanel.add(ok);
		SwingUtilities.getRootPane(this).setDefaultButton(ok);
		
		InputMap imap = buttonPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		imap.put(KeyStroke.getKeyStroke("R"), "repeat");
		ActionMap amap = buttonPanel.getActionMap();
		amap.put("repeat", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) {
				option = REPEAT_OPTION;
				dispose();
			}
		});
		
		add(labelPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	public static int showDialog(JDialog parent, String message) {
		ResultDialog dialog = new ResultDialog(parent, message);
		dialog.setVisible(true);
		return dialog.option;
	}
}
