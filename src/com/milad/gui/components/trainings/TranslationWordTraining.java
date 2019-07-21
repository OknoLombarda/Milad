package com.milad.gui.components.trainings;

import java.awt.Color;
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
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.milad.MiladTools;
import com.milad.Word;
import com.milad.gui.GBC;
import com.milad.gui.components.InertTextArea;

public class TranslationWordTraining extends AbstractTraining {
	private static final long serialVersionUID = 5784941329301774967L;
	private static final int WIDTH = 650;
	private static final int HEIGHT = 370;
	private static final Dimension buttonSize = new Dimension(270, 40);
	
	private InertTextArea word;
	private JButton[] options;
	private JButton next;
	
	private List<Word> words;
	private Iterator<Word> wordIter;
	
	private Random rand;
	
	private JPanel rightPanel;
	private InputMap imap;
	private ActionMap amap;
	
	private Word currentWord;
	private String answer;
	private String wordTranslation;
	private int ansIndex;
	
	public TranslationWordTraining(JFrame parentFrame, JDialog ancestor) {
		super(parentFrame, ancestor);
		answer = "";
		rand = new Random();
		setLayout(new GridBagLayout());
		
		JPanel leftPanel = new JPanel(new GridBagLayout());
		word = new InertTextArea();
		word.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		leftPanel.add(word, new GBC(0, 0).setAnchor(GBC.SOUTH).setInsets(10).setWeight(0, 0));
		
		rightPanel = new JPanel(new GridBagLayout());
		options = new JButton[5];
		for (int i = 0; i < options.length; i++) {
			options[i] = new JButton();
			options[i].setPreferredSize(buttonSize);
			options[i].setMinimumSize(buttonSize);
			options[i].setBackground(Color.WHITE);
			options[i].setHorizontalAlignment(SwingConstants.LEFT);
			rightPanel.add(options[i], new GBC(0, i).setAnchor(GBC.EAST).setInsets(i == 0 ? 10 : 0, 10, 10, 10).setWeight(0, 0));
		}
		
		JPanel bottomPanel = new JPanel();
		next = new JButton("Next");
		next.addActionListener(event -> {
			for (int i = 0; i < options.length; i++)
				options[i].setBackground(Color.WHITE);
			if (options[0].isEnabled())
				results.add("<p color=\"red\">".concat(wordTranslation).concat("</p>"));
			
			if (wordIter.hasNext())
				updatePanel();
			else
				showResultDialog();
		});
		SwingUtilities.getRootPane(ancestor).setDefaultButton(next);
		bottomPanel.add(next);
		
		add(leftPanel, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(10).setWeight(100, 0));
		add(rightPanel, new GBC(1, 0).setAnchor(GBC.EAST).setInsets(10, 0, 10, 10).setWeight(0, 0));
		add(bottomPanel, new GBC(1, 1, 2, 1).setAnchor(GBC.CENTER).setInsets(20, 10, 10, 10).setWeight(0, 0));
		
		updatePanel();
	}
	
	@Override
	public void updatePanel() {
		StringBuilder sb = new StringBuilder();
		
		currentWord = wordIter.next();
		answer = currentWord.getWord();
		ArrayList<String> transl = currentWord.getTranslations();
		wordTranslation = transl.get(rand.nextInt(transl.size()));
		
		word.setText("<center>".concat(wordTranslation).concat("</center>"));
		
		if (imap == null) {
			imap = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
			amap = this.getActionMap();
		}
		
		ansIndex = rand.nextInt(options.length);
		for (int i = 0; i < options.length; i++) {
			sb.append(i + 1).append(". ").append(i == ansIndex ? answer : wordIter.next().getWord());
			OptionAction optAct = new OptionAction(sb.toString(), i);
			options[i].setAction(optAct);
			sb.delete(0, sb.length());
			options[i].setEnabled(true);
			
			String key = "option" + (i + 1);
			int numOne = 97;
			imap.put(KeyStroke.getKeyStroke(numOne + i, 0), key);
			amap.put(key, optAct);
		}
	}
	
	@Override
	public void initialize(boolean firstTime) {
		if (firstTime) {
			results = new ArrayList<>();
			results.add("<html>");
		} else {
			results.removeIf(r -> !r.equals("<html>"));
		}
		words = MiladTools.getWords(50);
		Collections.shuffle(words);
		
		wordIter = words.iterator();
		
		if (!firstTime) {
			updatePanel();
		}
	}
	
	public Dimension preferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	private class OptionAction extends AbstractAction {
		private static final long serialVersionUID = -4747004989771399786L;

		public OptionAction(String name, int index) {
			putValue(Action.NAME, name);
			putValue("index", index);
		}
		
		public void actionPerformed(ActionEvent event) {
			int index = (int) getValue("index");
			boolean isCorrect = index == ansIndex;
			results.add("<p color=\"" + (isCorrect ? "green" : "red") + "\">" + wordTranslation + " — "
						+ ((String) getValue(Action.NAME)).substring(3) + "</p>");
			
			for (int i = 0; i < options.length; i++)
				options[i].setEnabled(false);
			
			if (isCorrect) {
				currentWord.updatePracticed();
				options[index].setBackground(Color.GREEN);
			}
			else {
				options[index].setBackground(Color.RED);
				options[ansIndex].setBackground(Color.GREEN);
			}
		}
	}
}
