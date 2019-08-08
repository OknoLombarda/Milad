package com.milad.gui.components.trainings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.milad.MiladTools;
import com.milad.Phrase;
import com.milad.ResourceLoader;
import com.milad.gui.GBC;
import com.milad.gui.components.InertTextArea;
import com.milad.gui.components.WordPanel;

public class PhraseConstructorTraining extends AbstractTraining {
	private static final long serialVersionUID = 5935921881443912546L;
	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private static final int WORDS_LINE = 230;
	private static final int ANSWER_LINE = 155;

	private List<Phrase> phrases;
	private Iterator<Phrase> phraseIter;
	private LinkedList<WordPanel> words;
	private LinkedList<WordPanel> answer;

	private InertTextArea translation;
	private JButton check;
	private JButton next;
	private JPanel phrasePanel;
	private JLabel line;

	private Phrase currentPhrase;
	private boolean isCorrect;

	public PhraseConstructorTraining(JFrame parentFrame, JDialog ancestor) {
		super(parentFrame, ancestor);
		words = new LinkedList<>();
		answer = new LinkedList<>();

		setLayout(new GridBagLayout());

		translation = new InertTextArea();
		translation.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

		line = new JLabel();
		line.setIcon((ImageIcon) ResourceLoader.getResource("line"));

		phrasePanel = new JPanel(null);
		phrasePanel.setOpaque(false);

		JPanel buttonPanel = new JPanel();
		check = new JButton("Check");
		check.addActionListener(event -> {
			checkAnswer();
			showCheckResult();
		});
		next = new JButton("Next");
		next.addActionListener(event -> {
			checkAnswer();
			if (isCorrect) {
				results.add("<p color=\"green\">" + currentPhrase.getPhrase() + "</p>");
				currentPhrase.updatePracticed();
			} else {
				results.add("<p color=\"red\">" + currentPhrase.getPhrase() + "</p>");
			}

			if (phraseIter.hasNext()) {
				updatePanel();
			} else {
				showResultDialog();
			}
		});
		next.setPreferredSize(check.getMinimumSize());
		buttonPanel.add(check);
		buttonPanel.add(next);
		SwingUtilities.getRootPane(ancestor).setDefaultButton(next);

		add(translation, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(20, 10, 10, 10).setWeight(0, 0));
		add(line, new GBC(0, 1).setAnchor(GBC.CENTER).setWeight(0, 100));
		add(phrasePanel, new GBC(0, 1).setAnchor(GBC.CENTER).setFill(GBC.BOTH).setWeight(100, 100));
		add(buttonPanel, new GBC(0, 2).setAnchor(GBC.CENTER).setInsets(10).setWeight(0, 0));

		updatePanel();
	}

	@Override
	public void initialize(boolean firstTime) {
		if (firstTime) {
			results = new ArrayList<>();
			results.add("<html>");
		} else {
			results.removeIf(r -> !r.equals("<html>"));
		}
		int amount = MiladTools.getAmountOfPhrases();
		phrases = MiladTools.getPhrases(amount >= 5 ? 5 : amount);
		Collections.shuffle(phrases);
		phraseIter = phrases.iterator();

		if (!firstTime) {
			updatePanel();
		}
	}

	@Override
	public void updatePanel() {
		currentPhrase = phraseIter.next();
		phrasePanel.setBorder(null);

		answer.forEach(w -> phrasePanel.remove(w));
		answer.removeAll(answer);

		List<String> translations = currentPhrase.getTranslations();
		translation.setText(translations.get(ThreadLocalRandom.current().nextInt(translations.size())));

		List<String> list = Arrays.stream(currentPhrase.getPhrase().split(" ")).collect(Collectors.toList());
		while (words.size() > list.size()) {
			phrasePanel.remove(words.removeLast());
		}
		while (words.size() < list.size()) {
			WordPanel wp = new WordPanel();
			wp.addMouseListener(new WordListener(wp));
			words.add(wp);
			phrasePanel.add(wp);
		}

		Collections.shuffle(list);
		Iterator<WordPanel> iter = words.iterator();
		for (String s : list) {
			WordPanel wp = iter.next();
			wp.setText(s);
			Dimension ps = wp.getPreferredSize();
			wp.setBounds(0, 0, ps.width, ps.height);
		}
		placeWords();
		repaint();
	}

	private void placeWords() {
		placeWordPanels(words, WORDS_LINE);
		placeWordPanels(answer, ANSWER_LINE);
	}

	private void checkAnswer() {
		StringBuilder sb = new StringBuilder();
		for (WordPanel wp : answer) {
			sb.append(wp.getText()).append(" ");
		}
		String ans = sb.toString().trim();
		isCorrect = ans.equals(currentPhrase.getPhrase().trim());
	}

	private void showCheckResult() {
		phrasePanel.setBorder(BorderFactory.createLineBorder(isCorrect ? Color.GREEN : Color.RED, 4, true));
	}

	private void placeWordPanels(List<WordPanel> list, int y) {
		int length = 5;
		for (WordPanel wp : list) {
			length += wp.getPreferredSize().width + 5;
		}
		int xcenter = length / 2;
		int x = WIDTH / 2 - xcenter + 5;
		for (WordPanel wp : list) {
			wp.setLocation(x, y);
			x += wp.getPreferredSize().width + 5;
		}
	}

	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	private class WordListener extends MouseAdapter {
		private WordPanel wp;

		public WordListener(WordPanel wp) {
			this.wp = wp;
		}

		public void mouseClicked(MouseEvent event) {
			if (wp.isUsed()) {
				wp.setUsed(false);
				answer.remove(wp);
				words.add(wp);
				placeWords();
			} else {
				wp.setUsed(true);
				words.remove(wp);
				answer.add(wp);
				placeWords();
			}
		}
	}
}
