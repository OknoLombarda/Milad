package com.milad;

import java.awt.EventQueue;
import java.io.IOException;

import com.milad.gui.MiladMainFrame;

public class Milad {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ResourceLoader.loadResources();
		MiladTools.readData();

		EventQueue.invokeLater(() -> {
			MiladMainFrame mainFrame = new MiladMainFrame();
			mainFrame.setVisible(true);
		});
	}
}
