package com.milad.gui.components.trainings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.milad.MiladTools;
import com.milad.Word;
import com.milad.gui.GBC;

public class WordTranslationTraining extends JPanel {
	private static final long serialVersionUID = 5784941329301774967L;
	private static final int WIDTH = 450;
	private static final int HEIGHT = 370;
	
	private JLabel word;
	private JLabel transcription;
	private JButton[] options;
	private JButton next;
	
	private ArrayList<Word> words;
	private Iterator<Word> wordIter;
	private ListIterator<Word> wordEditor;
	private ArrayList<String> wrongOptions;
	private Iterator<String> optIter;
	
	private Random rand;
	
	private JPanel rightPanel;
	private InputMap imap;
	private ActionMap amap;
	
	private ArrayList<String> results;
	private Word currentWord;
	private String answer;
	private int ansIndex;
	
	public WordTranslationTraining(JFrame parentFrame, JDialog ancestor) {
		answer = "";
		results = new ArrayList<>();
		words = new ArrayList<>(MiladTools.getWords(10));
		Collections.shuffle(words);
		wrongOptions = new ArrayList<>(MiladTools.getRandomData(MiladTools.PRIMARY, 40));
		Collections.shuffle(wrongOptions);
		rand = new Random();
		setLayout(new GridBagLayout());
		
		JPanel leftPanel = new JPanel(new GridBagLayout());
		word = new JLabel();
		transcription = new JLabel();
		leftPanel.add(word, new GBC(0, 0).setAnchor(GBC.SOUTH).setInsets(10).setWeight(0, 0));
		leftPanel.add(transcription, new GBC(0, 1).setAnchor(GBC.NORTH).setInsets(0, 10, 10, 10).setWeight(0, 0));
		
		rightPanel = new JPanel(new GridBagLayout());
		options = new JButton[5];
		for (int i = 0; i < options.length; i++) {
			options[i] = new JButton();
			options[i].setPreferredSize(new Dimension(250, 30));
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
				results.add("<html><center><p color=\"red\">" + word.getText() + "</p></center></html>");
			
			if (wordIter.hasNext())
				updatePanel();
			else {
				StringBuilder sb = new StringBuilder();
				for (String s : results)
					sb.append(s).append("\n");
				JOptionPane.showMessageDialog(ancestor, sb.toString(), "Results", JOptionPane.PLAIN_MESSAGE);
				ancestor.setVisible(false);
				parentFrame.setState(Frame.NORMAL);
			}
		});
		SwingUtilities.getRootPane(ancestor).setDefaultButton(next);
		bottomPanel.add(next);
		
		add(leftPanel, new GBC(0, 0).setAnchor(GBC.CENTER).setInsets(10).setWeight(100, 0));
		add(rightPanel, new GBC(1, 0).setAnchor(GBC.EAST).setInsets(10, 0, 10, 10).setWeight(0, 0));
		add(bottomPanel, new GBC(1, 1, 2, 1).setAnchor(GBC.CENTER).setInsets(20, 10, 10, 10).setWeight(0, 0));
		
		updatePanel();
	}
	
	public void updatePanel() {
		StringBuilder sb = new StringBuilder();
		
		if (wordIter == null) {
			wordIter = words.iterator();
			wordEditor = words.listIterator();
			optIter = wrongOptions.iterator();
		}
		
		currentWord = wordIter.next();
		ArrayList<String> transl = currentWord.getTranslations();
		answer = transl.get(rand.nextInt(transl.size()));
		
		word.setText(currentWord.getWord());
		
		if (imap == null) {
			imap = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
			amap = this.getActionMap();
		}
		
		ansIndex = rand.nextInt(options.length);
		for (int i = 0; i < options.length; i++) {
			sb.append(i + 1).append(". ").append(i == ansIndex ? answer : optIter.next());
			OptionAction optAct = new OptionAction(sb.toString(), i);
			options[i].setAction(optAct);
			sb.delete(0, sb.length());
			options[i].setEnabled(true);
			
			String key = "option" + (i + 1);
			int num1 = 97;
			imap.put(KeyStroke.getKeyStroke(num1 + i, 0), key);
			amap.put(key, optAct);
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
			results.add("<html><center><p color=\"" + (isCorrect ? "green" : "red") + "\">" + word.getText() + " — "
						+ ((String) getValue(Action.NAME)).substring(3) + "</p></center></html>");
			
			for (int i = 0; i < options.length; i++)
				options[i].setEnabled(false);
			
			wordEditor.next();
			if (isCorrect) {
				currentWord.updatePracticed();
				wordEditor.set(currentWord);
				
				options[index].setBackground(Color.GREEN);
			}
			else {
				options[index].setBackground(Color.RED);
				options[ansIndex].setBackground(Color.GREEN);
			}
		}
	}
}
