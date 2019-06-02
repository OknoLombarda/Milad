package com.milad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class Phrase extends Word {
	private String phrase;
	
	public Phrase(String phrase, String[] translations) {
		super(phrase, translations);
		this.phrase = getWord();
	}
	
	public Phrase(String word, int year, int month, int day, String[] translations) {
		super(word, year, month, day, translations);
		this.phrase = getWord();
	}
	
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	
	public String getPhrase() {
		return phrase;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("phrase;");
		sb.append(phrase).append(";");
		LocalDate ltp = getLastTimePracticed();
		ArrayList<String> transl = getTranslations();
		Iterator<String> iter = transl.iterator();
		for (int i = 0; i < transl.size(); i++) {
			sb.append(iter.next());
			if (i != transl.size() - 1)
				sb.append("&");
			else
				sb.append(";");
		}
		sb.append(getStrength()).append(";")
		                        .append(ltp.getYear()).append(".")
		                        .append(ltp.getMonthValue()).append(".")
		                        .append(ltp.getDayOfMonth());
		return sb.toString();
	}
}
