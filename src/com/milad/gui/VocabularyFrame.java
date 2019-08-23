package com.milad.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.milad.MiladTools;
import com.milad.ResourceLoader;
import com.milad.Word;
import com.milad.gui.components.AutoscrollSafePanel;
import com.milad.gui.components.VocabularyWordPanel;

public class VocabularyFrame extends JDialog {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private JTextField searchBar;
	private AutoscrollSafePanel resultPanel;
	private ArrayDeque<VocabularyWordPanel> results;

	private List<Word> content;

	public VocabularyFrame(JFrame parent) {
		super(parent, "Vocabulary", true);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());

		results = new ArrayDeque<>(MiladTools.getVocabularySize() / 2);
		content = MiladTools.getVocabulary();

		JPanel buttonPanel = new JPanel(new GridBagLayout());

		searchBar = new JTextField(30);
		searchBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

		resultPanel = new AutoscrollSafePanel();
		resultPanel.setBorder(BorderFactory.createEtchedBorder());
		resultPanel.setBackground(Color.WHITE);
		resultPanel.setLayout(new GridLayout(0, 1));
		resultPanel.setPreventAutoscroll(true);
		JScrollPane scrollPane = new JScrollPane(resultPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		add(buttonPanel, new GBC(0, 0).setAnchor(GBC.NORTHWEST).setWeight(100, 0));
		add(searchBar, new GBC(0, 1).setInsets(10).setAnchor(GBC.NORTHWEST).setWeight(100, 100));
		add(scrollPane, new GBC(0, 2).setInsets(10).setFill(GBC.BOTH).setAnchor(GBC.NORTHWEST).setWeight(100, 100));

		updateResults();
	}

	public void showFrame() {
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void updateResults() {
		while (results.size() < 30) {
			VocabularyWordPanel panel = new VocabularyWordPanel();
			results.add(panel);
			resultPanel.add(panel);
		}
		while (results.size() > content.size()) {
			resultPanel.remove(results.removeLast());
		}

		Iterator<Word> iter = content.iterator();
		Word w;
		for (VocabularyWordPanel p : results) {
			w = iter.next();
			if (p.getWord() == null || !p.getWord().equals(w)) {
				p.setWord(w);
			}
		}
	}

	// TODO delete all following
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		MiladTools.readData();
		ResourceLoader.loadResources();

		EventQueue.invokeLater(() -> {
			VocabularyFrame vf = new VocabularyFrame(null);
			vf.showFrame();
		});
	}
}
