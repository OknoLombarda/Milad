package com.milad;

import java.awt.EventQueue;
import java.io.IOException;

import com.milad.gui.MiladMainFrame;

public class Milad {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// read files in separate thread
		// make loading screen ?
		ResourceLoader.loadResources();
		MiladTools.readData();

		EventQueue.invokeLater(() -> {
			MiladMainFrame mainFrame = new MiladMainFrame();
			mainFrame.setVisible(true);
		});
	}
}
