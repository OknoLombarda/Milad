package com.milad;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Word implements Comparable<Word>, Serializable {
	private static final long serialVersionUID = -2109823654436567281L;
	
	private String word;
	private ArrayList<String> translations;
	private LocalDate lastTimePracticed;
	private int strength;
	private String usage;
	private String transcription;

	public Word(String word, String[] translations) {
		this.translations = new ArrayList<>();
		this.word = word;
		this.translations.addAll(Arrays.asList(translations));
		lastTimePracticed = LocalDate.now();
		strength = 0;
		usage = "";
		transcription = "";
	}

	public Word(String word, int year, int month, int day, String[] translations) {
		this(word, translations);
		lastTimePracticed = LocalDate.of(year, month, day);
	}

	public void updatePracticed() {
		lastTimePracticed = LocalDate.now();
		if (strength < 10)
			strength++;
	}
	
	public void updateStrength() {
		LocalDate present = LocalDate.now();
		long weeks = lastTimePracticed.until(present, ChronoUnit.WEEKS);
		strength -= weeks;

		if (strength < 0)
			strength = 0;
	}

	public void addTranslations(String[] translations) {
		this.translations.addAll(Arrays.asList(translations));
	}
	
	public void setTranslations(String[] translations) {
		this.translations = new ArrayList<String>(Arrays.asList(translations));
	}

	public final void setWord(String word) {
		this.word = word;
	}
	
	public final void setTranscription(String transcription) {
		this.transcription = transcription;
	}
	
	public final void setUsage(String usage) {
		this.usage = usage;
	}
	
	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int compareTo(Word word) {
		return this.strength - word.strength;
	}

	public String getWord() {
		return word;
	}

	public ArrayList<String> getTranslations() {
		return translations;
	}
	
	public final String getTranscription() {
		return transcription;
	}
	
	public final String getUsage() {
		return usage;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public LocalDate getLastTimePracticed() {
		return lastTimePracticed;
	}
	
	public boolean hasTranscription() {
		return transcription.length() != 0;
	}
	
	public boolean hasUsage() {
		return usage.length() != 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("word;");
		sb.append(word).append(";");
		Iterator<String> iter = translations.iterator();
		for (int i = 0; i < translations.size(); i++) {
			sb.append(iter.next());
			if (i != translations.size() - 1)
				sb.append("&");
			else
				sb.append(";");
		}
		sb.append(transcription).append(";")
		                        .append(usage).append(";")
		                        .append(strength).append(";")
		                        .append(lastTimePracticed.getYear()).append(".")
		                        .append(lastTimePracticed.getMonthValue()).append(".")
		                        .append(lastTimePracticed.getDayOfMonth());
		return sb.toString();
	}
	
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if (other == null)
			return false;
		
		if (this.getClass() != other.getClass())
			return false;
		
		Word otherWord = (Word) other;
		return word.equals(otherWord.getWord());
	}
	
	public static Word empty() {
		return new Word("<br>", new String[] { "<br>" });
	}
}
