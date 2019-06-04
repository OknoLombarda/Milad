package com.milad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MiladTools {
	private static File data = new File("vocabulary.txt");
	private static File properties = new File("milad.properties");
	private static ArrayList<Word> vocabulary = new ArrayList<>();
	private static Properties prop = new Properties();
	
	public static void initializeFiles() {
		/*private String path = new File(MiladTools.class.getProtectionDomain()
		   .getCodeSource().getLocation().toURI()).getPath();
		path = path.replace("Milad.jar", ""); */
	}
	
	public static void readData() throws IOException {
		prop.load(new FileReader(properties));
/*		Scanner sc = new Scanner(data);
		while (sc.hasNextLine()) {
			String[] param = sc.nextLine().split(";");
			if (param[0].equals("word")) {
				int[] date = Arrays.stream(param[6].split("\\.")).mapToInt(s -> Integer.parseInt(s)).toArray();
				Word temp = new Word(param[1], date[0], date[1], date[2], param[2].split("&"));
				temp.setTranscription(param[3]);
				temp.setUsage(param[4]);
				temp.setStrength(Integer.parseInt(param[5]));
				vocabulary.add(temp);
			}
			else if (param[0].equals("phrase")) {
				int[] date = Arrays.stream(param[4].split("\\.")).mapToInt(s -> Integer.parseInt(s)).toArray();
				Phrase temp = new Phrase(param[1], date[0], date[1], date[2], param[2].split("&"));
				temp.setStrength(Integer.parseInt(param[3]));
				vocabulary.add(temp);
			}
		}
		sc.close(); */
		Scanner sc = new Scanner(data);
		while (sc.hasNextLine()) {
			String[] param = sc.nextLine().split(";");
			if (param[0].equals("word")) {
				Word temp = new Word(param[1], param[2].split("&"));
				if (param[3].isBlank())
					param[3] = "#";
				temp.setTranscription(param[3]);
				if (param[4].isBlank())
					param[4] = "#";
				temp.setUsage(param[4]);
				vocabulary.add(temp);
			}
			else if (param[0].equals("phrase")) {
				Phrase temp = new Phrase(param[1], param[2].split("&"));
				vocabulary.add(temp);
			}
		}
		sc.close();
	}
	
	public static void printData() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(data);
		for (Word w : vocabulary)
			pw.println(w);
		pw.close();
	}
	
	public static Word getRandomWord() {
		Random rand = new Random();
		List<Word> words = vocabulary.stream().filter(v -> v.getClass() == Word.class).collect(Collectors.toList());
		return words.get(rand.nextInt(words.size()));
	}
	
	public static String getProperty(String key) {
		return prop.getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		prop.put(key, value);
	}
}
