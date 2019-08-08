package com.milad.gui.components.trainings;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.milad.MiladTools;
import com.milad.Word;
import com.milad.gui.GBC;

public class WordCardsTraining extends AbstractTraining {
	private static final long serialVersionUID = 2681081989137704138L;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 500;

	private JLabel word;
	private JLabel transcription;
	private JLabel translation;
	private JButton know;
	private JButton toLearning;
	private JButton next;

	private List<Word> words;
	private Iterator<Word> wordIter;

	private Random rand;

	private Word currentWord;
	private String wordTransl;
	private boolean remember;

	public WordCardsTraining(JFrame parentFrame, JDialog ancestor) {
		super(parentFrame, ancestor);
		setLayout(new GridBagLayout());
		rand = new Random();

		word = new JLabel();
		transcription = new JLabel();
		translation = new JLabel();
		word.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
		transcription.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
		translation.setFont(new Font(Font.SERIF, Font.PLAIN, 20));

		know = new JButton("I know this >");
		toLearning = new JButton("< To learning");
		next = new JButton("Next");

		Dimension buttonSize = toLearning.getMinimumSize();
		know.setPreferredSize(buttonSize);
		next.setPreferredSize(buttonSize);

		next.addActionListener(event -> {
			String color = "";
			if (remember) {
				color = "green";
				currentWord.updatePracticed();
			} else {
				color = "red";
				currentWord.setStrength(currentWord.getStrength() / 2);
			}

			results.add("<p color=\"".concat(color).concat("\">").concat(currentWord.getWord()).concat("</p>"));

			if (wordIter.hasNext())
				updatePanel();
			else
				showResultDialog();
		});
		SwingUtilities.getRootPane(ancestor).setDefaultButton(next);

		SideAction right = new SideAction(know, toLearning, true);
		SideAction left = new SideAction(toLearning, know, false);
		know.addActionListener(right);
		toLearning.addActionListener(left);
		InputMap imap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap amap = getActionMap();
		imap.put(KeyStroke.getKeyStroke("LEFT"), "left");
		imap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		amap.put("left", left);
		amap.put("right", right);

		add(word, new GBC(0, 0, 3, 1).setInsets(30, 0, 5, 0).setAnchor(GBC.SOUTH).setWeight(0, 100));
		add(transcription, new GBC(0, 1, 3, 1).setAnchor(GBC.NORTH).setWeight(0, 100));
		add(translation, new GBC(0, 2, 3, 1).setAnchor(GBC.NORTH).setWeight(0, 100));
		add(toLearning, new GBC(0, 3, 1, 1).setInsets(40, 0, 0, 10).setAnchor(GBC.CENTER).setWeight(0, 100));
		add(next, new GBC(1, 3, 1, 1).setInsets(40, 0, 0, 10).setAnchor(GBC.CENTER).setWeight(0, 100));
		add(know, new GBC(2, 3, 1, 1).setInsets(40, 0, 0, 0).setAnchor(GBC.CENTER).setWeight(0, 100));

		updatePanel();
	}

	@Override
	public void initialize(boolean firstTime) {
		words = MiladTools.getWords(10);
		Collections.shuffle(words);
		wordIter = words.iterator();

		if (firstTime) {
			results = new ArrayList<>();
			results.add("<html>");
		} else {
			results.removeIf(r -> !r.equals("<html>"));
		}

		if (!firstTime) {
			updatePanel();
		}
	}

	@Override
	public void updatePanel() {
		currentWord = wordIter.next();

		word.setText(currentWord.getWord());
		transcription.setText(currentWord.hasTranscription() ? currentWord.getTranscription() : "<html><br></html>");
		translation.setText("<html><br></html>");

		List<String> transl = currentWord.getTranslations();
		wordTransl = transl.get(rand.nextInt(transl.size()));

		if (!know.isEnabled())
			know.setEnabled(true);
		if (!toLearning.isEnabled())
			toLearning.setEnabled(true);
		next.setEnabled(false);
	}

	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	private class SideAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private JButton button;
		private JButton otherButton;
		private boolean flag;

		public SideAction(JButton button, JButton otherButton, boolean flag) {
			this.button = button;
			this.flag = flag;
			this.otherButton = otherButton;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			translation.setText(wordTransl);
			next.setEnabled(true);
			button.setEnabled(false);
			if (!otherButton.isEnabled())
				otherButton.setEnabled(true);

			if (flag && !remember)
				remember = true;
			else if (!flag && remember)
				remember = false;
		}
	}
}
