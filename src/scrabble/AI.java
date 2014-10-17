package scrabble;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import edu.princeton.cs.introcs.StdDraw;

public class AI implements Player {

	String name;
	int score;
	ArrayList<Letter> tray;
	Hashtable<Character, Integer> letterValues;
	String[] dictionary;
	boolean currentPlayer;

	private final Font fontBig = new Font("Courier", Font.BOLD, 25);

	public AI(String newName, Hashtable<Character, Integer> letterValues, String[] dictionary) {
		tray = new ArrayList<Letter>();
		this.letterValues = letterValues;
		this.dictionary = dictionary;
		score = 0;
		name = newName;
		currentPlayer = false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}

	@Override
	public void addScore(int newScore) {
		score += newScore;
	}

	public void addLetter(Letter newLetter) {
		tray.add(newLetter);
	}

	public int getNumberOfLetters() {
		return tray.size();
	}

	/**
	 * Checks if a word contains a specific character and returns the index of
	 * the first instance. Otherwise returns -1.
	 **/
	private int contains(char[] word, char letter) {
		for (int i = 0; i < word.length; i++) {
			if (letter == word[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds all the anagrams of the letters in tray and one on the board in the
	 * dictionary. 
	 **/
	public ArrayList<String> findAnagrams(char letter) {

		char[] characters;
		if (letter != ' ') {
			characters = new char[tray.size() + 1];
		} else {
			characters = new char[tray.size()];
		}

		ArrayList<String> anagrams = new ArrayList<String>();

		/* Fills characters with the chars from tray and with letter */
		for (int i = 0; i < tray.size(); i++) {
			characters[i] = tray.get(i).getCharacter();
		}
		if (letter != ' ') {
			characters[tray.size()] = letter;
		}
		/* For each word in dictionary */
		for (int i = 0; i < dictionary.length; i++) {

			char[] temp = characters.clone();
			char[] word = dictionary[i].toCharArray();

			/* For each char in word */
			for (int j = 0; j < word.length; j++) {
				int index = contains(temp, word[j]);
				if (index != -1) {
					temp[index] = '.';
				} else {
					break;
				}
				if (j == word.length - 1) {
					String newWord = new String(word);
					if (letter != ' ' && newWord.contains("" + letter)){
						anagrams.add(newWord);						
					} else if (letter == ' '){
						anagrams.add(newWord);						
					}
				}
			}
		}
		Collections.sort(anagrams);
		return anagrams;
	}

	/** Find score of a word **/
	public int findScore(String word) {
		int score = 0;
		for (Character c : word.toCharArray()) {
			score += letterValues.get(c);
		}
		return score;
	}

	/** Finds the highest scoring anagram **/
	public String findBestWord(ArrayList<String> anagrams) {
		String word = "";
		int score = 0;
		for (String s : anagrams) {
			int temp = findScore(s);
			if (temp > score) {
				score = temp;
				word = s;
			}
		}
		return word;
	}
	
	public void draw() {
		double[] sideCenter = { 160, 445 };

		StdDraw.setFont(fontBig);
		if(currentPlayer){
			StdDraw.setPenColor(StdDraw.RED);
		} else {
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		StdDraw.textLeft(sideCenter[0] - 150, sideCenter[1] + 180, name + ": ");
		StdDraw.textRight(sideCenter[0] + 140, sideCenter[1] + 180, score + "");
	}

	@Override
	public ArrayList<Letter> getTray() {
		// TODO Auto-generated method stub
		return tray;
	}

	@Override
	public void setCurrentPlayer(boolean value) {
		currentPlayer = value;
	}

}
