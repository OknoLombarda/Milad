package com.milad.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import com.milad.MiladTools;
import com.milad.Word;

public class WordEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 280;
	private static final int DEFAULT_HEIGHT = 220;
	private static final int ADVANCED_WIDTH = 280;
	private static final int ADVANCED_HEIGHT = 330;
	private static final int DEFAULT_STATE = 0;
	private static final int ADVANCED_STATE = 1;
	private static final int EDIT_STATE = 2;
	private static final int ADD_STATE = 4;
	private static final String EDIT_TITLE = "Edit";
	private static final String ADD_TITLE = "Add";

	private JLabel wordLabel;
	private JTextField wordField;
	private JLabel translLabel;
	private JTextField translField;
	private JLabel transcrLabel;
	private JTextField transcrField;
	private JLabel usageLabel;
	private JTextField usageField;
	private JButton stateSwitcher;
	private JButton save;
	private JButton cancel;
	
	private Word word;

	private ActionListener editListener = event -> {
		word.setWord(wordField.getText());
		word.setTranslations(translField.getText().split("&"));
		word.setTranscription(transcrField.getText());
		word.setUsage(usageField.getText());
		write();
	};

	private ActionListener addListener = event -> {
		word = new Word(wordField.getText(), translField.getText().split("&"));
		word.setTranscription(transcrField.getText());
		word.setUsage(usageField.getText());
		MiladTools.add(word);
		((VocabularyFrame) getParent()).updateResults(MiladTools.getVocabulary());
		write();
	};
	
	private int state = DEFAULT_STATE + EDIT_STATE;
	
	public WordEditor(JDialog parent) {
		super(parent, EDIT_TITLE, true);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		
		wordLabel = new JLabel("Word");
		wordField = new JTextField(22);
		translLabel = new JLabel("Translations");
		translField = new JTextField(22);
		translField.setToolTipText("Translations must be separated by '&' character");
		
		transcrLabel = new JLabel("Transcription");
		transcrField = new JTextField(22);
		usageLabel = new JLabel("Usage");
		usageField = new JTextField(22);
		transcrLabel.setVisible(false);
		transcrField.setVisible(false);
		usageLabel.setVisible(false);
		usageField.setVisible(false);
		
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
		
		stateSwitcher = new JButton("<html><u><font color=\"#0645AD\">Advanced</font></u></html>");
		stateSwitcher.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseExited(MouseEvent event) {
				setCursor(Cursor.getDefaultCursor());
			}
		});
		stateSwitcher.addActionListener(event -> setState((state & 1) == DEFAULT_STATE ? ADVANCED_STATE : DEFAULT_STATE));
		stateSwitcher.setBackground(UIManager.getColor("Label.background"));
		stateSwitcher.setBorder(null);

		add(wordLabel, new GBC(0, 0).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(wordField, new GBC(0, 1).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(translLabel, new GBC(0, 2).setAnchor(GBC.NORTHWEST).setInsets(15, 5, 0, 0).setWeight(100, 0));
		add(translField, new GBC(0, 3).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(transcrLabel, new GBC(0, 4).setAnchor(GBC.NORTHWEST).setInsets(15, 5, 0, 0).setWeight(100, 0));
		add(transcrField, new GBC(0, 5).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(usageLabel, new GBC(0, 6).setAnchor(GBC.NORTHWEST).setInsets(15, 5, 0, 0).setWeight(100, 0));
		add(usageField, new GBC(0, 7).setAnchor(GBC.NORTHWEST).setInsets(5, 5, 0, 0).setWeight(100, 0));
		add(stateSwitcher, new GBC(0, 8).setAnchor(GBC.SOUTHWEST).setInsets(5).setWeight(100, 100));
		add(buttonPanel, new GBC(0, 9).setFill(GBC.HORIZONTAL).setAnchor(GBC.SOUTH).setWeight(100, 100));
	}
	
	public void showEditor(Word word) {
		this.word = word;

		wordField.setText(word.getWord());

		List<String> transl = word.getTranslations();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < transl.size(); i++) {
			sb.append(transl.get(i));
			if (i != transl.size() - 1) {
				sb.append("&");
			}
		}
		translField.setText(sb.toString());

		if (word.hasTranscription()) {
			transcrField.setText(word.getTranscription());
		}

		if (word.hasUsage()) {
			usageField.setText(word.getUsage());
		}

		if (state > (ADVANCED_STATE + EDIT_STATE)) {
			state = state - ADD_STATE + EDIT_STATE;
			setTitle(EDIT_TITLE);
			save.removeActionListener(addListener);
			save.addActionListener(editListener);
		}
		if ((state & 1) == ADVANCED_STATE) {
			setState(DEFAULT_STATE);
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void showEditor() {
		this.word = null;

		wordField.setText("");
		translField.setText("");
		transcrField.setText("");
		usageField.setText("");

		if (state <= ADVANCED_STATE + EDIT_STATE) {
			state = state - EDIT_STATE + ADD_STATE;
			setTitle(ADD_TITLE);
			save.removeActionListener(editListener);
			save.addActionListener(addListener);
		}
		if ((state & 1) == ADVANCED_STATE) {
			setState(DEFAULT_STATE);
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void setState(int state) {
		if (state != DEFAULT_STATE && state != ADVANCED_STATE) {
			throw new IllegalArgumentException("Illegal state value");
		}

		boolean flag = state == ADVANCED_STATE;
		this.state += flag ? 1 : -1;
		int width = flag ? ADVANCED_WIDTH : DEFAULT_WIDTH;
		int height = flag ? ADVANCED_HEIGHT : DEFAULT_HEIGHT;
		String s1 = "Simplified";
		String s2 = "Advanced";
		if (flag) {
			String tmp = s1;
			s1 = s2;
			s2 = tmp;
		}
		
		setSize(width, height);
		stateSwitcher.setText(stateSwitcher.getText().replace(s1, s2));
		transcrLabel.setVisible(flag);
		transcrField.setVisible(flag);
		usageLabel.setVisible(flag);
		usageField.setVisible(flag);

		repaint();
	}

	private void write() {
		new Thread(() -> {
			try {
				MiladTools.writeData();
			} catch (IOException e) {
				System.err.println("Error appeared while writing data. (WordEditor)\n".concat(e.getMessage()));
				System.exit(1);
			}
		}).start();
	}
}
