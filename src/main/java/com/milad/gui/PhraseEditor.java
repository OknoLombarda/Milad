package com.milad.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.milad.MiladTools;
import com.milad.Phrase;

public class PhraseEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 615;
	private static final int DEFAULT_HEIGHT = 200;
	private static final String EDIT_TITLE = "Edit";
	private static final String ADD_TITLE = "Add";
	private static final int EDIT_STATE = 0;
	private static final int ADD_STATE = 1;

	private int state = EDIT_STATE;
	
	private JLabel phraseLabel;
	private JTextField phraseField;
	private JLabel translLabel;
	private JTextField translField;
	private JButton save;
	private JButton cancel;

	private Phrase phrase;

	private final ActionListener editListener = event -> {
		phrase.setPhrase(phraseField.getText());
		phrase.setTranslations(translField.getText().split("&"));
		write();
	};

	private final ActionListener addListener = event -> {
		phrase = new Phrase(phraseField.getText(), translField.getText().split("&"));
		MiladTools.add(phrase);
		((VocabularyFrame) getParent()).updateResults(MiladTools.getVocabulary());
		write();
	};

	public PhraseEditor(JDialog parent) {
		super(parent, EDIT_TITLE, true);
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
		save.addActionListener(editListener);
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

		if (state == ADD_STATE) {
			save.removeActionListener(addListener);
			save.addActionListener(editListener);
			setTitle(EDIT_TITLE);
			state = EDIT_STATE;
		}
		
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

	public void showEditor() {
		this.phrase = null;

		phraseField.setText("");
		translField.setText("");

		if (state == EDIT_STATE) {
			save.removeActionListener(editListener);
			save.addActionListener(addListener);
			setTitle(ADD_TITLE);
			state = ADD_STATE;
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void write() {
		new Thread(() -> {
			try {
				MiladTools.writeData();
			} catch (IOException e) {
				System.err.println("Error appeared while writing data. (PhraseEditor)\n".concat(e.getMessage()));
			}
		}).start();
	}
}
