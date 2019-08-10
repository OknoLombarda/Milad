package com.milad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ResourceLoader {
	private static final FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("ImageFiles", "jpg", "png");
	private static final FileNameExtensionFilter textFilter = new FileNameExtensionFilter("TextFiles", "txt");
	
	private static HashMap<String, Object> resources = new HashMap<>();
	
	public static void loadResources() throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;
		
		for (File f : new File(ResourceLoader.class.getResource("resources").getPath()).listFiles()) {
			if (imageFilter.accept(f))
				resources.put(removeExtension(f.getName()), new ImageIcon(f.getPath()));
			else if (textFilter.accept(f)) {
				sc = new Scanner(f, "UTF-8");
				while (sc.hasNextLine())
					sb.append(sc.nextLine());
				
				resources.put(removeExtension(f.getName()), sb.toString());
				sb.delete(0, sb.length());
			}
		}
		
		if (sc != null)
			sc.close();
	}
	
	public static Object getResource(String name) {
		return resources.get(name);
	}
	
	private static String removeExtension(String s) {
		return s.replaceAll("\\..+", "");
	}
}
