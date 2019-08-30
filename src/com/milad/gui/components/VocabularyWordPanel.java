package com.milad.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.milad.MiladTools;
import com.milad.ResourceLoader;
import com.milad.Word;
import com.milad.gui.GBC;

public class VocabularyWordPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private InertTextArea wordLabel;
	private InertTextArea translations;
	private JCheckBox selection;
	private JButton toLearning;
	private JButton remove;
	private JProgressBar strength;

	private Word word;

	public VocabularyWordPanel() {
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);

		selection = new JCheckBox();

		wordLabel = new InertTextArea();
		wordLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		wordLabel.setOpaque(false);
		translations = new InertTextArea();
		translations.setFont(new Font(Font.SERIF, Font.BOLD, 14));
		translations.setOpaque(false);
		selection = new JCheckBox();

		toLearning = new JButton((ImageIcon) ResourceLoader.getProperty("hat"));
		toLearning.setFocusPainted(false);
		toLearning.setBackground(Color.WHITE);
		toLearning.setToolTipText("Send this word to learning");
		toLearning.addActionListener(event -> {
			word.setStrength(0);
			strength.setValue(0);
			toLearning.setEnabled(false);
			try {
				MiladTools.printData();
			} catch (IOException e) {
				e.printStackTrace(); // TODO ???
			}
		});

		remove = new JButton((ImageIcon) ResourceLoader.getProperty("bin"));
		remove.setFocusPainted(false);
		remove.setBackground(Color.WHITE);
		remove.setToolTipText("Remove this word from vocabulary");
		remove.addActionListener(event -> {
			int input = JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove", JOptionPane.YES_NO_OPTION);
			if (input == JOptionPane.YES_OPTION) {
				MiladTools.remove(word);
				try {
					MiladTools.printData();
				} catch (IOException e) {
					e.printStackTrace(); // TODO ???
				}
				JPanel parent = (JPanel) getParent();
				parent.remove(this);
				parent.revalidate();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(toLearning);
		buttonPanel.add(remove);

		strength = new JProgressBar(0, 10);
		strength.setPreferredSize(new Dimension(40, 20));

		add(selection, new GBC(0, 0, 1, 2).setInsets(0, 5, 0, 5).setAnchor(GBC.WEST).setWeight(0, 100));
		add(wordLabel, new GBC(1, 0).setInsets(5, 5, 0, 0).setAnchor(GBC.WEST).setWeight(100, 100));
		add(translations, new GBC(1, 1).setInsets(0, 5, 5, 0).setAnchor(GBC.WEST).setWeight(100, 100));
		add(strength, new GBC(2, 0, 1, 2).setAnchor(GBC.EAST).setWeight(100, 100));
		add(buttonPanel, new GBC(3, 0, 1, 2).setAnchor(GBC.EAST).setWeight(100, 100));

		selection.setOpaque(false);
		selection.addActionListener(event -> {
			Color color = selection.isSelected() ? new Color(153, 255, 102) : Color.WHITE;
			setBackground(color);
			repaint();
		});
	}

	public VocabularyWordPanel(Word word) {
		this();
		setWord(word);
	}

	public void setWord(Word word) {
		this.word = word;
		updatePanel();
	}

	public Word getWord() {
		return word;
	}

	public void setSelected(boolean selected) {
		selection.setSelected(selected);
	}

	public boolean isSelected() {
		return selection.isSelected();
	}

	private void updatePanel() {
		toLearning.setEnabled(word.getStrength() != 0);

		StringBuilder sb = new StringBuilder();
		List<String> transl = word.getTranslations();
		for (int i = 0; i < transl.size(); i++) {
			sb.append(transl.get(i));
			if (i != transl.size() - 1)
				sb.append(", ");
		}

		translations.setPreferredSize(new Dimension(400, sb.toString().length() > 50 ? 40 : 20));
		translations.setText(sb.toString());
		String w = word.getWord();
		if (w.length() > 50) {
			w = w.substring(0, 47).concat("...");
		}
		wordLabel.setText("<font color=\"#009900\">".concat(w.concat("</font>")));
		strength.setValue(word.getStrength());
	}
}
