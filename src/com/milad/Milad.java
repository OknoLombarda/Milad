package com.milad;

import java.awt.EventQueue;
import java.io.FileNotFoundException;

import com.milad.gui.MiladMainFrame;

public class Milad {
	public static void main(String[] args) throws FileNotFoundException {
		MiladTools.readData();
		
		EventQueue.invokeLater(() -> {
			MiladMainFrame mainFrame = new MiladMainFrame();
			mainFrame.setVisible(true);
		});
	}
}