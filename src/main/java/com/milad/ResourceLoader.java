package com.milad;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceLoader {
	private static HashMap<String, String> resources = new HashMap<>();

	public static void loadResources() {
		resources.put("ava", "/ava.jpg");
		resources.put("bin", "/bin.png");
		resources.put("hat", "/hat.png");
		resources.put("about", "/about.txt");
		resources.put("line", "/line.png");
	}

	public static Object getProperty(String key) throws IOException {
		String name = resources.get(key);
		InputStream is = ResourceLoader.class.getResourceAsStream(name);
		if (isTextFile(name)) {
			StringBuilder sb = new StringBuilder();
			Scanner sc = new Scanner(is, "UTF-8");
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}
			sc.close();
			return sb.toString();
		} else if (isImageFile(name)) {
			return new ImageIcon(ImageIO.read(is));
		}
		
		return null;
	}
	
	private static boolean isTextFile(String name) {
		return name.endsWith(".txt");
	}
	
	private static boolean isImageFile(String name) {
		return name.endsWith(".png") || name.endsWith(".jpg");
	}
}
