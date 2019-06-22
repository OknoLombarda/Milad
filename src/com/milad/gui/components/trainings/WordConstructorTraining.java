package com.milad.gui.components.trainings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Predicate;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.milad.MiladTools;
import com.milad.Word;
import com.milad.gui.GBC;
import com.milad.gui.components.LetterPanel;

public class WordConstructorTraining extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 550;
	private static final int HEIGHT = 500;
	private static final Predicate<Word> filter = w -> w.getWord().length() <= 15;

	private JLabel word;
	private JLabel transcription;
	private JButton next;
	private JPanel answerPanel;
	private JPanel lettersPanel;
	private LinkedList<LetterPanel> answer;
	private LinkedList<LetterPanel> letters;
	private Iterator<LetterPanel> ansIter;

	private ArrayList<String> results;
	private ArrayList<Word> words;
	private Iterator<Word> wordIter;

	private Word currentWord;
	private int nextLetterIndex;
	private boolean isCorrect;
	private int mistakeCounter;

	private Object lock;
	private JFrame parentFrame;
	private JDialog ancestor;

	public WordConstructorTraining(JFrame parentFrame, JDialog ancestor) {
		this.parentFrame = parentFrame;
		this.ancestor = ancestor;
		setLayout(new GridBagLayout());
		results = new ArrayList<>();
		results.add("<html>");
		words = new ArrayList<>(MiladTools.getWords(10, filter));
		wordIter = words.iterator();
		Collections.shuffle(words);
		answer = new LinkedList<>();
		letters = new LinkedList<>();
		nextLetterIndex = 0;
		isCorrect = true;
		mistakeCounter = 0;
		lock = new Object();

		InputMap im = (InputMap) UIManager.get("Button.focusInputMap");
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "none");

		word = new JLabel();
		transcription = new JLabel();
		word.setFont(new Font("Georgia", Font.BOLD, 25));
		transcription.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

		answerPanel = new JPanel();
		lettersPanel = new JPanel();
		
		next = new JButton("Next");
		next.addActionListener(event -> addResult());
		SwingUtilities.getRootPane(ancestor).setDefaultButton(next);

		add(word, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(10, 10, 0, 10).setWeight(0, 0));
		add(transcription, new GBC(0, 1).setAnchor(GBC.CENTER).setInsets(5, 10, 0, 10).setWeight(0, 0));
		add(answerPanel, new GBC(0, 2).setAnchor(GBC.CENTER).setInsets(10).setWeight(0, 0));
		add(lettersPanel, new GBC(0, 3).setAnchor(GBC.CENTER).setInsets(0, 10, 10, 10).setWeight(0, 0));
		add(next, new GBC(0, 4).setAnchor(GBC.CENTER).setInsets(10).setWeight(0, 0));

		updatePanel();
	}

	public void updatePanel() {
		currentWord = wordIter.next();
		nextLetterIndex = 0;
		isCorrect = true;
		mistakeCounter = 0;

		ArrayList<String> translations = currentWord.getTranslations();
		word.setText(translations.get((int) Math.random() * translations.size()));
		transcription.setText(currentWord.hasTranscription() ? currentWord.getTranscription() : "<html><br></html>");

		answer.forEach(w -> w.setEmpty());
		letters.forEach(l -> l.setVisible(true));

		int wordLength = currentWord.getWord().length();
		while (answer.size() < wordLength) {
			answer.add(new LetterPanel());
			answerPanel.add(answer.peekLast());
		}
		while (answer.size() > wordLength)
			answerPanel.remove(answer.removeLast());

		while (letters.size() < wordLength) {
			letters.add(new LetterPanel());
			lettersPanel.add(letters.peekLast());
		}
		while (letters.size() > wordLength)
			lettersPanel.remove(letters.removeLast());

		ansIter = answer.iterator();

		InputMap imap = lettersPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap amap = lettersPanel.getActionMap();
		imap.clear();
		amap.clear();

		Collections.shuffle(letters);
		Iterator<LetterPanel> iter = letters.iterator();
		int index = 0;
		for (char c : currentWord.getWord().toCharArray()) {
			LetterPanel lp = iter.next();
			lp.setLetter(String.valueOf(c));
			lp.addMouseListener(new LetterPanelListener(c, lp));

			String key = String.valueOf(index);
			imap.put(KeyStroke.getKeyStroke(c), key);
			amap.put(key, new LetterAction(c, lp));
			index++;
		}
	}

	public void repeat() {
		results = new ArrayList<>();
		results.add("<html>");
		words = new ArrayList<>(MiladTools.getWords(10, filter));
		Collections.shuffle(words);
		wordIter = words.iterator();
		updatePanel();
	}

	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	private class LetterAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public LetterAction(char letter, LetterPanel lp) {
			putValue(Action.NAME, letter);
			putValue("letterPanel", lp);
		}

		public void actionPerformed(ActionEvent event) {
			LetterPanel lp = (LetterPanel) getValue("letterPanel");

			if (currentWord.getWord().charAt(nextLetterIndex) == (char) getValue(Action.NAME)) {
				for (Component c : lettersPanel.getComponents())
					if (lp.isIdenticalTo(c) && c.isVisible()) {
						c.setVisible(false);
						break;
					}

				synchronized (lock) {
					ansIter.next().setLetter(String.valueOf(getValue(Action.NAME)));
					nextLetterIndex++;
				}

				if (nextLetterIndex == answer.size())
					addResult();
			} else {
				Thread t = new Thread(() -> {
					blink(answer.get(nextLetterIndex));
				});
				t.setDaemon(true);
				t.start();

				if (isCorrect && mistakeCounter > 0)
					isCorrect = false;

				mistakeCounter++;
			}
		}
	}

	private class LetterPanelListener extends MouseAdapter {
		private char c;
		private LetterPanel lp;

		public LetterPanelListener(char c, LetterPanel lp) {
			this.c = c;
			this.lp = lp;
		}

		public void mouseClicked(MouseEvent event) {
			if (currentWord.getWord().charAt(nextLetterIndex) == c) {
				lp.setVisible(false);

				synchronized (lock) {
					ansIter.next().setLetter(String.valueOf(c));
					nextLetterIndex++;
				}

				if (nextLetterIndex == answer.size())
					addResult();
			} else {
				Thread t = new Thread(() -> {
					blink(lp);
				});
				t.setDaemon(true);
				t.start();

				if (isCorrect && mistakeCounter > 0)
					isCorrect = false;

				mistakeCounter++;
			}
		}
	}

	public void blink(LetterPanel lp) {
		synchronized (lock) {
			Color bg = lp.getBackground();
			lp.setBackground(Color.RED);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lp.setBackground(bg);
			}
		}
	}

	public void addResult() {
		if (isCorrect && nextLetterIndex == answer.size()) {
			results.add("<p color=\"green\">" + currentWord.getWord() + "</p>");
			currentWord.updatePracticed();
		} else
			results.add("<p color=\"red\">" + currentWord.getWord() + "</p>");

		if (wordIter.hasNext())
			updatePanel();
		else {
			StringBuilder sb = new StringBuilder();
			for (String s : results)
				sb.append(s);
			sb.append("</html>");
			int input = ResultDialog.showDialog(ancestor, sb.toString());
			if (input == ResultDialog.OK_OPTION) {
				ancestor.dispose();
				parentFrame.setState(Frame.NORMAL);
			} else if (input == ResultDialog.REPEAT_OPTION) {
				repeat();
			}
		}
	}
}
