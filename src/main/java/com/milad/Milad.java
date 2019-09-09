package com.milad;

import java.awt.EventQueue;
import java.io.IOException;

import com.milad.gui.MiladMainFrame;

public class Milad {
	public static void main(String[] args) {
		ResourceLoader.loadResources();
		try {
			MiladTools.readData();
		} catch (IOException e) {
			System.err.println("Error occurred while reading data. (vocabulary.dat)\n".concat(e.getMessage()));
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.err.println("Files are missing.\n".concat(e.getMessage()));
			System.exit(1);
		}

		EventQueue.invokeLater(() -> {
			MiladMainFrame mainFrame = new MiladMainFrame();
			mainFrame.setVisible(true);
		});
	}
}
