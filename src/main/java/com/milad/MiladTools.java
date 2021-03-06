package com.milad;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiladTools {
	private static final Predicate<Word> WORD = w -> w.getClass() == Word.class;
	private static final Predicate<Word> PHRASE = p -> p.getClass() == Phrase.class;
	
	private static File data = new File("vocabulary.dat");
	private static ArrayList<Word> vocabulary = new ArrayList<>();
	
	public static void writeData() throws IOException {
		if (!data.exists()) {
			data.createNewFile();
		}
		ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(data)));
		os.writeObject(vocabulary);
		os.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void readData() throws IOException, ClassNotFoundException {
		if (!data.exists()) {
			data.createNewFile();
		} else {
			ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(data)));
			vocabulary = (ArrayList<Word>) is.readObject();
			is.close();
			vocabulary.forEach(w -> w.updateStrength());
		}
	}

	public static void add(Word word) {
		vocabulary.add(word);
	}
	
	public static List<Word> getVocabulary() {
		return vocabulary;
	}
	
	public static List<Word> getWords(int amount, Predicate<Word> filter) {
		return vocabulary.stream().filter(WORD).filter(filter == null ? w -> true : filter)
					     .sorted().limit(amount).collect(Collectors.toList());
	}
	
	public static List<Word> getWords(int amount) {
		return getWords(amount, null);
	}
	
	public static List<Phrase> getPhrases(int amount) {
		return vocabulary.stream().filter(PHRASE).sorted().limit(amount).map(p -> (Phrase) p).collect(Collectors.toList());
	}
	
	public static List<String> getTranslations(int amount) {
		Stream<Word> str = vocabulary.stream().filter(WORD).sorted().skip(10).limit(amount);
		List<String> list = new ArrayList<String>();
		str.forEach(w -> {
			for (String s : w.getTranslations())
				if (list.size() < amount)
					list.add(s);
		});
		return list;
	}
	
	public static Word getRandomWord() {
		List<Word> words = vocabulary.stream().filter(WORD).collect(Collectors.toList());
		return words.size() > 0 ? words.get(ThreadLocalRandom.current().nextInt(words.size())) : Word.empty();
	}
	
	public static int getVocabularySize() {
		return vocabulary.size();
	}
	
	public static int getAmountOfPhrases() {
		return (int) vocabulary.stream().filter(PHRASE).count();
	}
	
	public static void remove(Word word) {
		vocabulary.remove(word);
	}
	
	public static List<Word> find(String s) {
		boolean checkTranslations = true;
		if (s.matches("[A-Za-z ,.!?-]+"))
			checkTranslations = false;
		
		return find(s, checkTranslations);
	}
	
	private static List<Word> find(String s, boolean flag) {
		List<SearchResult<Word>> results = new ArrayList<SearchResult<Word>>();
		double index;
		
		for (Word w : vocabulary) {
			index = checkSimilarity(w.getWord(), s);
			
			if (flag) {
				for (String t : w.getTranslations()) {
					double t_index = checkSimilarity(t, s);
					if (t_index > index) {
						index = t_index;
					}
				}
			}
			
			if (index != 0.0)
				results.add(new SearchResult<Word>(w, index));
		}
		
		return results.stream().sorted().map(r -> r.getResult()).collect(Collectors.toList());
	}
	
	private static double checkSimilarity(String first, String second) {
		first = first.toLowerCase();
		second = second.toLowerCase();
		if (first.equals(second))
			return Double.MAX_VALUE;
		
		String s1 = second;
		String s2 = second;
		boolean done = false;

		while (!done && s1.length() != 0 && s2.length() != 0) {
			if (first.contains(s1)) {
				done = true;
			} else if (first.contains(s2)) {
				s1 = s2;
				done = true;
			}
			s1 = s1.substring(0, s1.length() - 1);
			s2 = s2.substring(1);
		}
		
		double scale = (double) s1.length() / second.length();
		int diff = Math.abs(second.length() - first.length());
		return (scale < 0.3 ? 0 : scale) * (diff == 0 ? 1 : (1.0 / diff));
	}
}
