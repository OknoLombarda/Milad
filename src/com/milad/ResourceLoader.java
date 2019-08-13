package com.milad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ResourceLoader {
	private static final FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("ImageFiles", "jpg", "png");
	private static final FileNameExtensionFilter textFilter = new FileNameExtensionFilter("TextFiles", "txt");

	private static HashMap<String, Object> resources = new HashMap<>();

	public static void loadResources() throws IOException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = null;

	/*	CodeSource src = ResourceLoader.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			URL jar = src.getLocation();
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			while (true) {
				ZipEntry e = zip.getNextEntry();
				if (e == null)
					break;
				
				String name = e.getName();
				if (name.matches("com/milad/resources/.+")) {
					name = name.replace("com/milad/", "");
					InputStream is = ResourceLoader.class.getResourceAsStream(name);
					
					if (isTextFile(name)) {
						sc = new Scanner(is, "UTF-8");
						while (sc.hasNextLine())
							sb.append(sc.nextLine());

						resources.put(getKey(name), sb.toString());
						sb.delete(0, sb.length());
					} else if (isImageFile(name)) {
						resources.put(getKey(name), new ImageIcon(ImageIO.read(is)));
					}
				}
			}
			
			if (sc != null)
				sc.close();
		} */

		for (File f : new File(ResourceLoader.class.getResource("resources").getPath()).listFiles()) {
			if (imageFilter.accept(f))
				resources.put(getKey(f.getName()), new ImageIcon(f.getPath()));
			else if (textFilter.accept(f)) {
				sc = new Scanner(f, "UTF-8");
				while (sc.hasNextLine())
					sb.append(sc.nextLine());

				resources.put(getKey(f.getName()), sb.toString());
				sb.delete(0, sb.length());
			}
		}

		if (sc != null)
			sc.close();
	}

	public static Object getProperty(String name) {
		return resources.get(name);
	}

	private static String getKey(String s) {
		return s.replaceAll("\\..+", "").replace("resources/", "");
	}
	
	private static boolean isTextFile(String name) {
		return name.endsWith(".txt");
	}
	
	private static boolean isImageFile(String name) {
		return name.endsWith(".png") || name.endsWith(".jpg");
	}
}
