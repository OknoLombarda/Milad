package com.milad.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.milad.MiladTools;

public class SettingsDialog extends JDialog {
	private static final long serialVersionUID = 8527041759371973735L;
	private static final int DEFAULT_WIDTH = 250;
	private static final int DEFAULT_HEIGHT = 150;
	
	private JButton ok;
	private JButton cancel;
	private JButton apply;
	
	public SettingsDialog(Frame parent) {
		super(parent, "Settings", false);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridBagLayout());
		
		//settingsPanel.add(, new GBC(0, 0).setInsets(10, 10, 10, 10).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setWeight(100, 0));
		//settingsPanel.add(, new GBC(1, 0).setInsets(10, 0, 10, 10).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST).setWeight(100, 0));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		apply = new JButton("Apply");
		
		cancel.addActionListener(event -> setVisible(false));
		
		buttonPanel.add(ok, new GBC(0, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.EAST).setInsets(10, 10, 10, 10).setWeight(100, 0));
		buttonPanel.add(cancel, new GBC(1, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.EAST).setInsets(10, 0, 10, 10).setWeight(100, 0));
		buttonPanel.add(apply, new GBC(2, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.EAST).setInsets(10, 0, 10, 10).setWeight(100, 0));
		
		add(settingsPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void showDialog() {
		setVisible(true);
	}
}
