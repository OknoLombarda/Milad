package com.milad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class ResourceLoader {
	private static File about = new File(ResourceLoader.class.getResource("resources/about.txt").getPath());
	private static File coolPic = new File(ResourceLoader.class.getResource("resources/ava.jpg").getPath());
	
	private static HashMap<String, Object> resources = new HashMap<>();
	
	public static void loadResources() throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(about, "UTF-8");
		while (sc.hasNextLine())
			sb.append(sc.nextLine());
		
		resources.put("about", sb.toString());
		resources.put("coolPic", new ImageIcon(coolPic.getPath()));
	}
	
	public static Object getResource(String name) {
		return resources.get(name);
	}
}
