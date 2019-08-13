package com.milad.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

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

	private Word word;

	public VocabularyWordPanel(Word word) {
		this.word = word;
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
			toLearning.setEnabled(false);
		});

		remove = new JButton((ImageIcon) ResourceLoader.getProperty("bin"));
		remove.setFocusPainted(false);
		remove.setBackground(Color.WHITE);
		remove.setToolTipText("Remove this word from vocabulary");
		remove.addActionListener(event -> {
			MiladTools.remove(word);
			JPanel parent = (JPanel) getParent();
			parent.remove(this);
			parent.repaint();
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(toLearning);
		buttonPanel.add(remove);

		add(selection, new GBC(0, 0, 1, 2).setInsets(0, 5, 0, 5).setAnchor(GBC.WEST).setWeight(0, 100));
		add(wordLabel, new GBC(1, 0).setInsets(5, 5, 0, 0).setAnchor(GBC.NORTHWEST).setWeight(100, 100));
		add(translations, new GBC(1, 1).setInsets(0, 5, 5, 0).setAnchor(GBC.NORTHWEST).setWeight(100, 100));
		add(buttonPanel, new GBC(2, 0, 1, 2).setAnchor(GBC.EAST).setWeight(100, 100));

		selection.setOpaque(false);
		selection.addActionListener(event -> {
			Color color = selection.isSelected() ? new Color(153, 255, 102) : Color.WHITE;
			setBackground(color);
			repaint();
		});

		updatePanel();
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

		translations.setText(sb.toString());
		wordLabel.setText("<font color=\"#009900\">".concat(word.getWord().concat("</font>")));
	}
}
