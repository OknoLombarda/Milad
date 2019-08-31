package com.milad.gui;

import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.milad.MiladTools;
import com.milad.Phrase;

public class PhraseEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 615;
	private static final int DEFAULT_HEIGHT = 200;
	
	private JLabel phraseLabel;
	private JTextField phraseField;
	private JLabel translLabel;
	private JTextField translField;
	private JButton save;
	private JButton cancel;
	
	private Phrase phrase;
	
	public PhraseEditor(JDialog parent) {
		super(parent, "Edit", true);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());
		
		phraseLabel = new JLabel("Phrase");
		phraseField = new JTextField(50);
		translLabel = new JLabel("Translations");
		translField = new JTextField(50);
		translField.setToolTipText("Translations must be separated by '&' character");
		
		save = new JButton("Save");
		save.addActionListener(event -> {
			phrase.setPhrase(phraseField.getText());
			phrase.setTranslations(translField.getText().split("&"));
			try {
				MiladTools.printData();		// TODO print in a separate thread (?)
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	// TODO print it somewhere else idk
			}
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(event -> {
			setVisible(false);
		});
		save.setPreferredSize(cancel.getPreferredSize());
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonPanel.add(save, new GBC(0, 0).setAnchor(GBC.EAST).setInsets(10).setWeight(100, 100));
		buttonPanel.add(cancel, new GBC(1, 0).setAnchor(GBC.EAST).setInsets(10, 5, 10, 10).setWeight(0, 100));
		
		add(phraseLabel, new GBC(0, 0).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(phraseField, new GBC(0, 1).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(translLabel, new GBC(0, 2).setAnchor(GBC.NORTHWEST).setInsets(15, 5, 0, 0).setWeight(100, 0));
		add(translField, new GBC(0, 3).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(buttonPanel, new GBC(0, 4).setAnchor(GBC.SOUTHEAST).setFill(GBC.HORIZONTAL).setInsets(25, 0, 0, 0).setWeight(100, 100));
	}
	
	public void showEditor(Phrase phrase) {
		this.phrase = phrase;
		
		phraseField.setText(phrase.getPhrase());
		
		List<String> transl = phrase.getTranslations();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < transl.size(); i++) {
			sb.append(transl.get(i));
			if (i != transl.size() - 1) {
				sb.append("&");
			}
		}
		translField.setText(sb.toString());
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
