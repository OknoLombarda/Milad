package com.milad.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.milad.MiladTools;
import com.milad.Phrase;
import com.milad.Word;
import com.milad.gui.components.AutoscrollSafePanel;
import com.milad.gui.components.VocabularyWordPanel;

public class VocabularyFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String BUTTON_PANEL = "buttons";
	private static final String SELECTION_PANEL = "selection";

	private JPanel cards;
	private String currentCard = BUTTON_PANEL;
	private int selected = 0;

	private JLabel count;
	private JTextField searchBar;
	private AutoscrollSafePanel resultPanel;
	private ArrayList<VocabularyWordPanel> results;
	private WordEditor wordEditor;
	private PhraseEditor phraseEditor;

	private List<Word> content;
	
	public int pos = 0;
	private boolean firstTime = true;

	public VocabularyFrame(JFrame parent) {
		super(parent, "Vocabulary", true);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());
		
		wordEditor = new WordEditor(this);
		phraseEditor = new PhraseEditor(this);

		content = MiladTools.getVocabulary();
		results = new ArrayList<>(MiladTools.getVocabularySize());

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		JButton addWord = new JButton("Add word");
		JButton addPhrase = new JButton("Add phrase");
		addWord.setPreferredSize(addPhrase.getPreferredSize());
		addWord.addActionListener(event -> wordEditor.showEditor());
		addPhrase.addActionListener(event -> phraseEditor.showEditor());
		buttonPanel.add(addWord, new GBC(0, 0).setInsets(10, 10, 0, 10)
                .setAnchor(GBC.WEST).setWeight(0,0));
		buttonPanel.add(addPhrase, new GBC(1, 0).setInsets(10, 0, 0, 10)
                .setAnchor(GBC.WEST).setWeight(1000,0));

		JPanel selectionPanel = new JPanel(new GridBagLayout());
		count = new JLabel();
		JButton remove = new JButton("Remove");
		remove.addActionListener(event -> {
			if (userIsSure("Remove")) {
				for (VocabularyWordPanel p : results) {
					if (p.isSelected()) {
						MiladTools.remove(p.getWord());
						results.remove(p);
						resultPanel.remove(p);
					}
				}
				write();
				resultPanel.repaint();
			}
		});
		JButton toLearning = new JButton("Send to learning");
		toLearning.addActionListener(event -> {
			if (userIsSure("Send to learning")) {
				for (VocabularyWordPanel p : results) {
					if (p.isSelected()) {
						p.getWord().setStrength(0);
						p.updatePanel();
					}
				}
				write();
				resultPanel.repaint();
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(event -> {
			selectAll(false);
		});

		selectionPanel.add(count, new GBC(0, 0).setAnchor(GBC.WEST)
                .setInsets(10, 10, 0, 0).setWeight(100, 100));
		selectionPanel.add(toLearning, new GBC(1, 0).setAnchor(GBC.WEST)
                .setInsets(10, 10, 0, 0).setWeight(100, 100));
		selectionPanel.add(remove, new GBC(2, 0).setAnchor(GBC.WEST)
                .setInsets(10, 10, 0, 0).setWeight(100, 100));
		selectionPanel.add(cancel, new GBC(3, 0).setAnchor(GBC.WEST)
				.setInsets(10, 10, 0, 10).setWeight(100, 100));

		cards = new JPanel(new CardLayout());
		cards.add(buttonPanel, BUTTON_PANEL);
		cards.add(selectionPanel, SELECTION_PANEL);

		searchBar = new JTextField(30);
		searchBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		searchBar.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				find();
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				find();
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent) {
				find();
			}
		});

		resultPanel = new AutoscrollSafePanel();
		resultPanel.setBorder(BorderFactory.createEtchedBorder());
		resultPanel.setBackground(Color.WHITE);
		resultPanel.setLayout(new GridBagLayout());
		resultPanel.setPreventAutoscroll(true);
		resultPanel.getInputMap(JPanel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("ctrl A"), "selectAll");
		resultPanel.getActionMap().put("selectAll", new Selector());
		JScrollPane scrollPane = new JScrollPane(resultPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		add(cards, new GBC(0, 0).setAnchor(GBC.WEST).setWeight(100, 0));
		add(searchBar, new GBC(0, 1).setInsets(10).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH)
                .setWeight(100, 0));
		add(scrollPane, new GBC(0, 2).setInsets(10).setFill(GBC.BOTH).setAnchor(GBC.NORTH)
                .setWeight(100, 100));

		updateResults();
	}

	public void showFrame() {
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateResults(List<Word> content) {
		this.content = content;
		updateResults();
	}

	private void updateResults() {
		while (results.size() < content.size()) {
			VocabularyWordPanel panel = new VocabularyWordPanel(this);
			panel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if (event.getClickCount() == 2) {
						Word w = panel.getWord();
						if (w.getClass() == Word.class) {
							wordEditor.showEditor(w);
						} else {
							phraseEditor.showEditor((Phrase) w);
						}
					}
				}
			});
			panel.addListeners();
			JCheckBox cb = panel.getCheckBox();
			cb.addActionListener(event -> {
				if (cb.isSelected()) {
					selected++;
					if (currentCard.equals(BUTTON_PANEL)) {
						switchCards(SELECTION_PANEL);
					}
				} else {
					selected--;
					if (selected == 0) {
						switchCards(BUTTON_PANEL);
					}
				}
				updateCountLabel();
				cards.repaint();
			});
			results.add(panel);
			resultPanel.add(panel, new GBC(0, pos).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL)
                    .setWeight(100, 100));
			pos++;
		}
		if (firstTime) {
			JPanel fill;
			for (int i = pos; i < (pos + 8); i++) {
				fill = new JPanel();
				fill.setBackground(Color.WHITE);
				resultPanel.add(fill, new GBC(0, i).setAnchor(GBC.NORTH)
						.setFill(GBC.BOTH).setWeight(100, 100));
			}
			firstTime = false;
		}

		VocabularyWordPanel p;
		while (pos > 0 && pos >= content.size()) {
			pos--;
			p = results.get(pos);
			if (p.isSelected()) {
				p.setSelected(false);
			}
			p.setVisible(false);
		}
		
		while (pos < content.size()) {
			results.get(pos).setVisible(true);
			pos++;
		}
		
		Word w;
		Iterator<Word> iter = content.iterator();
		for (int i = 0; i < pos; i++) {
			p = results.get(i);
			w = iter.next();
			if (p.getWord() == null || !p.getWord().equals(w)) {
				p.setWord(w);
			}
		}
	}

	private void find() {
		content = MiladTools.find(searchBar.getText());
		updateResults();
	}

	private void updateCountLabel() {
		count.setText("Selected ".concat(String.valueOf(selected).concat(" word").concat(selected > 1 ? "s" : "")));
	}

	private void switchCards(String cardName) {
		if (!cardName.equals(SELECTION_PANEL) && !cardName.equals(BUTTON_PANEL)) {
			throw new IllegalArgumentException("Illegal card name");
		}

		((CardLayout) cards.getLayout()).show(cards, cardName);
		currentCard = cardName;
		cards.repaint();
	}

	private boolean userIsSure(String title) {
		int input = JOptionPane.showConfirmDialog(this, "Are you sure?", title,
				JOptionPane.YES_NO_OPTION);
		return input == JOptionPane.YES_OPTION;
	}

	private void write() {
		new Thread(() -> {
			try {
				MiladTools.writeData();
			} catch (IOException e) {
				System.err.println("Error appeared while writing data. (VocabularyFrame)\n".concat(e.getMessage()));
				System.exit(1);
			}
		}).start();
	}

	private void selectAll(boolean flag) {
		for (VocabularyWordPanel p : results) {
			p.setSelected(flag);
		}
		selected = flag ? results.size() : 0;
		updateCountLabel();
		if (flag) {
			if (currentCard.equals(BUTTON_PANEL)) {
				switchCards(SELECTION_PANEL);
			}
		} else {
			if (currentCard.equals(SELECTION_PANEL)) {
				switchCards(BUTTON_PANEL);
			}
		}
	}

	private class Selector extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			selectAll(true);
		}
	}
}
