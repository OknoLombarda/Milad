package com.milad.gui;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.milad.Word;

public class WordEditor extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 280;
	private static final int DEFAULT_HEIGHT = 220;
	private static final int ADVANCED_WIDTH = 280;
	private static final int ADVANCED_HEIGHT = 330;
	
	public static final int DEFAULT_STATE = 0;
	public static final int ADVANCED_STATE = 1;
	
	private JLabel wordLabel;
	private JTextField wordField;
	private JLabel translLabel;
	private JTextField translField;
	private JLabel transcrLabel;
	private JTextField transcrField;
	private JLabel usageLabel;
	private JTextField usageField;
	private JLabel stateSwitcher;
	private JButton save;
	private JButton cancel;
	
	private Word word;
	
	private int state = DEFAULT_STATE;
	
	public WordEditor(JDialog parent) {
		super(parent, "Edit", true);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
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
		save.addActionListener(event -> {
			word.setWord(wordField.getText());
			word.setTranslations(translField.getText().split("&"));
			word.setTranscription(transcrField.getText());
			word.setUsage(usageField.getText());
			try {
				MiladTools.printData();		// TODO print in a separate thread (?)
			} catch (IOException e) {
				e.printStackTrace(); // TODO print it somewhere else idk
			}
			setVisible(false);
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
		
		stateSwitcher = new JLabel("<html><u><font color=\"#0645AD\">Advanced</font></u></html>");
		stateSwitcher.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent event) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseExited(MouseEvent event) {
				setCursor(Cursor.getDefaultCursor());
			}
			
			public void mouseClicked(MouseEvent event) {
				setState(state == DEFAULT_STATE ? ADVANCED_STATE : DEFAULT_STATE);
			}
		});
		
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
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public Word getContent() {
		return word;
	}
	
	public void setState(int state) {
		if (state != DEFAULT_STATE && state != ADVANCED_STATE) {
			throw new IllegalArgumentException("Illegal state value");
		}
		
		this.state = state;
		boolean flag = state == ADVANCED_STATE;
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
	}
}
