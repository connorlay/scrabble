package scrabble;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import static edu.princeton.cs.introcs.StdDraw.*;
import static edu.princeton.cs.introcs.StdRandom.*;

public class Game {

	/* Constant fields. */
	private static final Color BACKGROUND = new Color(228, 238, 255);
	private static final Color LIGHT_BROWN = new Color(238, 197, 145);
	private static final Color BROWN = new Color(156, 102, 31);
	private static final Color BLUE = new Color(61, 89, 171);
	private static final Color CYAN = new Color(0, 197, 205);
	private static final Color PINK = new Color(255, 62, 150);
	private static final Color ORANGE = new Color(255, 140, 0);

	private static final Font BIG = new Font("Courier", Font.BOLD, 25);

	private static final double[] SIDE_CENTER = { 160, 450 };
	private static final double[] TRAY_CENTER = { 700, 40 };
	private static final double[] SUBMIT_BUTTON_CENTER = { 970, 40 };
	private static final double[] RESET_BUTTON_CENTER = { 430, 60 };
	private static final double[] PASS_BUTTON_CENTER = { 430, 20 };
	private static final double[] COLOR_KEY_CENTER = { 35, 370 };

	/* Global variables used for the AI findBestMove methods. */
	private String bestString = "";
	private int bestScore = 0;
	private char direction;
	private Square start = null;

	/* Pass booleans. The game ends if both are true. */
	private boolean humanPass;
	private boolean aiPass;

	/* Non-constant fields. */
	private Board board;
	private Human human;
	private AI ai;
	private ArrayList<Letter> bag;
	private String[] dictionary;
	private Hashtable<Character, Integer> letterValues;

	public Game() {
		dictionary = readDictionary();

		letterValues = new Hashtable<Character, Integer>(26);
		letterValues.put('a', 1);
		letterValues.put('b', 3);
		letterValues.put('c', 3);
		letterValues.put('d', 2);
		letterValues.put('e', 1);
		letterValues.put('f', 4);
		letterValues.put('g', 2);
		letterValues.put('h', 4);
		letterValues.put('i', 1);
		letterValues.put('j', 8);
		letterValues.put('k', 5);
		letterValues.put('l', 1);
		letterValues.put('m', 3);
		letterValues.put('n', 1);
		letterValues.put('o', 1);
		letterValues.put('p', 3);
		letterValues.put('q', 10);
		letterValues.put('r', 1);
		letterValues.put('s', 1);
		letterValues.put('t', 1);
		letterValues.put('u', 1);
		letterValues.put('v', 4);
		letterValues.put('w', 4);
		letterValues.put('x', 8);
		letterValues.put('y', 4);
		letterValues.put('z', 10);

		board = new Board(dictionary);

		bag = new ArrayList<Letter>();
		for (int i = 0; i < 12; i++) {
			bag.add(new Letter('e', letterValues));
		}
		for (int i = 0; i < 9; i++) {
			bag.add(new Letter('a', letterValues));
			bag.add(new Letter('i', letterValues));
		}
		for (int i = 0; i < 8; i++) {
			bag.add(new Letter('o', letterValues));
		}
		for (int i = 0; i < 6; i++) {
			bag.add(new Letter('n', letterValues));
			bag.add(new Letter('r', letterValues));
			bag.add(new Letter('t', letterValues));
		}
		for (int i = 0; i < 4; i++) {
			bag.add(new Letter('d', letterValues));
			bag.add(new Letter('l', letterValues));
			bag.add(new Letter('s', letterValues));
			bag.add(new Letter('u', letterValues));
		}
		for (int i = 0; i < 3; i++) {
			bag.add(new Letter('g', letterValues));
		}
		for (int i = 0; i < 2; i++) {
			bag.add(new Letter('b', letterValues));
			bag.add(new Letter('c', letterValues));
			bag.add(new Letter('f', letterValues));
			bag.add(new Letter('h', letterValues));
			bag.add(new Letter('m', letterValues));
			bag.add(new Letter('p', letterValues));
			bag.add(new Letter('v', letterValues));
			bag.add(new Letter('w', letterValues));
			bag.add(new Letter('y', letterValues));
		}
		bag.add(new Letter('j', letterValues));
		bag.add(new Letter('k', letterValues));
		bag.add(new Letter('q', letterValues));
		bag.add(new Letter('x', letterValues));
		bag.add(new Letter('z', letterValues));

		human = new Human("Human", dictionary);
		fillTray(human);

		ai = new AI("Computer", letterValues, dictionary);
		fillTray(ai);

		aiPass = false;
		humanPass = false;

	}

	/**
	 * Reads in dictionary.txt and fills dictionary. Exits the program if
	 * dictionary.txt is not found.
	 **/
	private static String[] readDictionary() {
		try {
			Scanner input = new Scanner(new File("dictionary.txt"));
			ArrayList<String> dictionary = new ArrayList<String>();
			while (input.hasNextLine()) {
				String word = input.nextLine();
				dictionary.add(word.toLowerCase());
			}
			return dictionary.toArray(new String[dictionary.size()]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}

	/**
	 * Adds letters to a player's tray until there are 7 letters in the tray or
	 * the bag is empty.
	 **/
	private void fillTray(Player player) {
		if (!bag.isEmpty() && player.getNumberOfLetters() < 7) {
			while (!bag.isEmpty() && player.getNumberOfLetters() < 7) {
				player.addLetter(bag.remove(uniform(bag.size())));
			}
		}
	}

	/** Returns true if the mouse is over the submit button, false otherwise **/
	private boolean submitClickOn() {
		if (mouseX() > SUBMIT_BUTTON_CENTER[0] - 55 && mouseX() < SUBMIT_BUTTON_CENTER[0] + 55 && mouseY() > SUBMIT_BUTTON_CENTER[1] - 25
				&& mouseY() < SUBMIT_BUTTON_CENTER[1] + 25) {
			return true;
		}
		return false;
	}

	/** Returns true if the mouse is over the reset button, false otherwise **/
	private boolean resetClickOn() {
		if (mouseX() > RESET_BUTTON_CENTER[0] - 55 && mouseX() < RESET_BUTTON_CENTER[0] + 55 && mouseY() > RESET_BUTTON_CENTER[1] - 15
				&& mouseY() < RESET_BUTTON_CENTER[1] + 15) {
			return true;
		}
		return false;
	}

	/** Returns true if the mouse is over the pass button, false otherwise **/
	private boolean passClickOn() {
		if (mouseX() > PASS_BUTTON_CENTER[0] - 55 && mouseX() < PASS_BUTTON_CENTER[0] + 55 && mouseY() > PASS_BUTTON_CENTER[1] - 15
				&& mouseY() < PASS_BUTTON_CENTER[1] + 15) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the corresponding letter of a specified character if found in a
	 * player's tray.
	 **/
	private Letter findLetterInTray(char c, Player player) {
		/* For each letter in tray */
		for (Letter letter : player.getTray()) {
			if (letter.getCharacter() == c) {
				return letter;
			}
		}
		return null;
	}

	/**
	 * Returns true if any of the squares in a word are neighbored by another
	 * filled square not also in the word.
	 **/
	private boolean hasUniqueNeighbors(ArrayList<Square> word) {
		for (Square s : word) {
			if (s.hasUniqueNeighbor(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the sum of the scores of each word in allWords and removes the
	 * score modifiers from their squares if permanent is true.
	 **/
	private int findScore(ArrayList<ArrayList<Square>> allWords, boolean permanent) {
		int score = 0;
		int wordMult = 1;
		for (ArrayList<Square> word : allWords) {
			int tempScore = 0;
			for (Square s : word) {
				if (s.getLetterMult() > 1) {
					tempScore += s.getLetter().getValue() * s.getLetterMult();
				} else {
					tempScore += s.getLetter().getValue();
				}
				wordMult *= s.getWordMult();
			}
			score += tempScore * wordMult;
			wordMult = 1;
		}

		if (permanent) {
			for (ArrayList<Square> word : allWords) {
				for (Square s : word) {
					s.setLetterMult(1);
					s.setWordMult(1);
				}
			}
		}

		return score;
	}

	/**
	 * Places the letters from AI's tray onto the board according to the fields
	 * bestString, direction, and start.
	 **/
	private void placeBestMove() {

		String prefix = "";
		String suffix = "";
		Square tempSquare = start;
		ArrayList<Square> word = new ArrayList<Square>();
		ArrayList<Square> tempCrossword = new ArrayList<Square>();
		ArrayList<ArrayList<Square>> allWords = new ArrayList<ArrayList<Square>>();

		if (bestString.equals("")) {
			aiPass = true;
			return;
		}

		word.add(start);

		int location = bestString.indexOf(start.getLetter().getCharacter());
		if (location > 0) {
			prefix = bestString.substring(0, location);
		}
		if (location < bestString.length()) {
			suffix = bestString.substring(location + 1, bestString.length());
		}
		if (direction == 'h') {
			/* Places each char in prefix to the left of start, if able */
			while (tempSquare.getLeft() != null && tempSquare.getLeft().getLetter() == null && !prefix.equals("")) {
				char c = prefix.charAt(prefix.length() - 1);
				prefix = prefix.substring(0, prefix.length() - 1);
				tempSquare = tempSquare.getLeft();
				tempSquare.setLetter(findLetterInTray(c, ai));
				word.add(tempSquare);
				ai.getTray().remove(tempSquare.getLetter());
				/* If the newly placed tile forms a crossword */
				tempCrossword = board.buildWord(tempSquare, 'h');
				if (!tempCrossword.isEmpty()) {
					allWords.add(tempCrossword);
				}
			}
			tempSquare = start;
			/* Places each char in suffix to the right of start, if able */
			while (tempSquare.getRight() != null && tempSquare.getRight().getLetter() == null && !suffix.equals("")) {
				char c = suffix.charAt(0);
				suffix = suffix.substring(1);
				tempSquare = tempSquare.getRight();
				tempSquare.setLetter(findLetterInTray(c, ai));
				word.add(tempSquare);
				ai.getTray().remove(tempSquare.getLetter());
				/* If the newly placed tile forms a crossword */
				tempCrossword = board.buildWord(tempSquare, 'h');
				if (!tempCrossword.isEmpty()) {
					allWords.add(tempCrossword);
				}
			}
		} else if (direction == 'v') {
			/* Places each char in prefix to the top of start, if able */
			while (tempSquare.getTop() != null && tempSquare.getTop().getLetter() == null && !prefix.equals("")) {
				char c = prefix.charAt(prefix.length() - 1);
				prefix = prefix.substring(0, prefix.length() - 1);
				tempSquare = tempSquare.getTop();
				tempSquare.setLetter(findLetterInTray(c, ai));
				word.add(tempSquare);
				ai.getTray().remove(tempSquare.getLetter());
				/* If the newly placed tile forms a crossword */
				tempCrossword = board.buildWord(tempSquare, 'v');
				if (!tempCrossword.isEmpty()) {
					allWords.add(tempCrossword);
				}
			}
			tempSquare = start;
			/* Places each char in suffix to the bottom of start, if able */
			while (tempSquare.getBottom() != null && tempSquare.getBottom().getLetter() == null && !suffix.equals("")) {
				char c = suffix.charAt(0);
				suffix = suffix.substring(1);
				tempSquare = tempSquare.getBottom();
				tempSquare.setLetter(findLetterInTray(c, ai));
				word.add(tempSquare);
				ai.getTray().remove(tempSquare.getLetter());
				/* If the newly placed tile forms a crossword */
				tempCrossword = board.buildWord(tempSquare, 'v');
				if (!tempCrossword.isEmpty()) {
					allWords.add(tempCrossword);
				}
			}
		}

		allWords.add(word);
		ai.addScore(findScore(allWords, true));
		start = null;
		direction = ' ';
		bestScore = 0;
		bestString = "";

		blink(allWords);
	}

	/**
	 * Finds all anagrams of the letter in first and the letters in AI's tray
	 * and finds the score of each when placed on the board, if possible. The
	 * highest scoring word is then saved in the fields bestString, bestScore,
	 * direction, and start.
	 **/
	private void findBestMove(Square first, ArrayList<String> anagrams, AI ai) {
		/* If both horizontal neighbors exist and do not have a letter */
		if (first.getRight() != null && first.getRight().getLetter() == null && first.getLeft() != null && first.getLeft().getLetter() == null) {
			/* Check each anagram to see if it fits horizontally */
			for (String s : anagrams) {
				String prefix = "";
				String suffix = "";
				Square tempSquare = first;
				ArrayList<Square> word = new ArrayList<Square>();
				ArrayList<Square> tempCrossword = new ArrayList<Square>();
				ArrayList<ArrayList<Square>> allWords = new ArrayList<ArrayList<Square>>();

				int location = s.indexOf(first.getLetter().getCharacter());
				if (location > 0) {
					prefix = s.substring(0, location);
				}
				if (location < s.length()) {
					suffix = s.substring(location + 1, s.length());
				}
				/* Places each char in prefix to the left of first, if able */
				while (tempSquare.getLeft() != null && tempSquare.getLeft().getLetter() == null && !prefix.equals("")) {
					char c = prefix.charAt(prefix.length() - 1);
					tempSquare = tempSquare.getLeft();
					tempSquare.setLetter(findLetterInTray(c, ai));
					word.add(tempSquare);
					/* If the newly placed tile forms a crossword */
					tempCrossword = board.buildWord(tempSquare, 'h');
					if (tempCrossword == null) {
						for (Square square : word) {
							square.setLetter(null);
						}
						break;
						/* Else add it to the list of words spelled */
					} else if (!tempCrossword.isEmpty()) {
						allWords.add(tempCrossword);
					}
					prefix = prefix.substring(0, prefix.length() - 1);
				}
				/* If not all the letters in prefix were placed, continue */
				if (!prefix.equals("")) {
					for (Square square : word) {
						square.setLetter(null);
					}
					continue;
				}

				/* Resets tempSquare to first */
				tempSquare = first;

				/* Places each char in suffix to the right of first, if able */
				while (tempSquare.getRight() != null && tempSquare.getRight().getLetter() == null && !suffix.equals("")) {
					char c = suffix.charAt(0);
					tempSquare = tempSquare.getRight();
					tempSquare.setLetter(findLetterInTray(c, ai));
					word.add(tempSquare);
					/* If the newly placed tile forms an illegal crossword */
					tempCrossword = board.buildWord(tempSquare, 'h');
					if (tempCrossword == null) {
						for (Square square : word) {
							square.setLetter(null);
						}
						break;
						/* Else add it to the list of words spelled */
					} else if (!tempCrossword.isEmpty()) {
						allWords.add(tempCrossword);
					}
					suffix = suffix.substring(1);
				}
				/* If not all the letters in prefix were placed, continue */
				if (!suffix.equals("")) {
					for (Square square : word) {
						square.setLetter(null);
					}
					continue;
				}

				/* If the word is spelled, save the score */
				ArrayList<Square> finalWord = board.buildWord(first, 'v');
				if (finalWord != null) {
					allWords.add(finalWord);
					int score = findScore(allWords, false);
					if (score > bestScore) {
						bestScore = score;
						bestString = s;
						direction = 'h';
						start = first;
					}
				}
				for (Square square : word) {
					square.setLetter(null);
				}
			}
		}

		/* If both vertical neighbors exist and do not have a letter */
		if (first.getTop() != null && first.getTop().getLetter() == null && first.getBottom() != null && first.getBottom().getLetter() == null) {
			/* Check each anagram to see if it fits vertically */
			for (String s : anagrams) {
				String prefix = "";
				String suffix = "";
				Square tempSquare = first;
				ArrayList<Square> tempCrossword = new ArrayList<Square>();
				ArrayList<ArrayList<Square>> allWords = new ArrayList<ArrayList<Square>>();
				ArrayList<Square> word = new ArrayList<Square>();

				int location = s.indexOf(first.getLetter().getCharacter());
				if (location > 0) {
					prefix = s.substring(0, location);
				}
				if (location < s.length()) {
					suffix = s.substring(location + 1, s.length());
				}
				/* Places each char in prefix to the top of first, if able */
				while (tempSquare.getTop() != null && tempSquare.getTop().getLetter() == null && !prefix.equals("")) {
					char c = prefix.charAt(prefix.length() - 1);
					tempSquare = tempSquare.getTop();
					tempSquare.setLetter(findLetterInTray(c, ai));
					word.add(tempSquare);
					/* If the newly placed tile forms an illegal crossword */
					tempCrossword = board.buildWord(tempSquare, 'v');
					if (tempCrossword == null) {
						for (Square square : word) {
							square.setLetter(null);
						}
						break;
						/* Else add it to the list of words spelled */
					} else if (!tempCrossword.isEmpty()) {
						allWords.add(tempCrossword);
					}
					prefix = prefix.substring(0, prefix.length() - 1);
				}
				/* If not all the letters in prefix were placed, continue */
				if (!prefix.equals("")) {
					for (Square square : word) {
						square.setLetter(null);
					}
					continue;
				}

				/* Resets tempSquare to first */
				tempSquare = first;

				/* Places each char in suffix to the bottom of first, if able */
				while (tempSquare.getBottom() != null && tempSquare.getBottom().getLetter() == null && !suffix.equals("")) {
					char c = suffix.charAt(0);
					tempSquare = tempSquare.getBottom();
					tempSquare.setLetter(findLetterInTray(c, ai));
					word.add(tempSquare);
					/* If the newly placed tile forms an illegal crossword */
					tempCrossword = board.buildWord(tempSquare, 'v');
					if (tempCrossword == null) {
						for (Square square : word) {
							square.setLetter(null);
						}
						break;
						/* Else add it to the list of words spelled */
					} else if (!tempCrossword.isEmpty()) {
						allWords.add(tempCrossword);
					}
					suffix = suffix.substring(1);
				}
				/* If not all the letters in prefix were placed, continue */
				if (!suffix.equals("")) {
					for (Square square : word) {
						square.setLetter(null);
					}
					continue;
				}

				/* If the word is spelled, save the score */
				ArrayList<Square> finalWord = board.buildWord(first, 'h');
				if (finalWord != null) {
					allWords.add(finalWord);
					int score = 0;
					score = findScore(allWords, false);
					if (score > bestScore) {
						bestScore = score;
						bestString = s;
						direction = 'v';
						start = first;
					}
				}

				for (Square square : word) {
					square.setLetter(null);
				}
			}
		}
	}

	/**
	 * Allows the human player to place letters from their tray onto the board
	 * and spell words for scoring.
	 **/
	private void makeMove(Human human) {
		Letter tempLetter = null;
		Square tempSquare = null;
		Square first = null;
		ArrayList<Square> word = new ArrayList<Square>();
		ArrayList<Square> fullWord = new ArrayList<Square>();
		ArrayList<ArrayList<Square>> allWords = new ArrayList<ArrayList<Square>>();
		char direction = ' ';

		human.setCurrentPlayer(true);
		humanPass = false;

		if (human.getTray().size() == 0) {
			humanPass = true;
			return;
		}

		while (true) {

			while (!mousePressed()) {
			}
			/* If the submit button has been clicked on */
			if (submitClickOn() && !word.isEmpty()) {
				if (word.size() == 1) {
					if (!hasUniqueNeighbors(word)) {
						fullWord = null;
					} else {
						if (word.get(0).getLeft() != null && word.get(0).getLeft().getLetter() != null) {
							direction = 'h';
						} else if (word.get(0).getRight() != null && word.get(0).getRight().getLetter() != null) {
							direction = 'h';
						} else if (word.get(0).getTop() != null && word.get(0).getTop().getLetter() != null) {
							direction = 'v';
						} else if (word.get(0).getBottom() != null && word.get(0).getBottom().getLetter() != null) {
							direction = 'v';
						}
					}
				} else {

					/* Find direction */
					if (word.get(0).getPosition()[0] == word.get(1).getPosition()[0]) {
						direction = 'v';
					} else if (word.get(0).getPosition()[1] == word.get(1).getPosition()[1]) {
						direction = 'h';
					}
				}

				fullWord = board.buildWord(first, direction);

				/* Adds all crosswords of word */
				for (Square s : word) {
					ArrayList<Square> crossword = new ArrayList<Square>();
					if (direction == 'v') {
						crossword = board.buildWord(s, 'h');
					} else if (direction == 'h') {
						crossword = board.buildWord(s, 'v');
					}
					allWords.add(crossword);
				}
				allWords.add(fullWord);

				if (!allWords.contains(null)) {
					human.addScore(findScore(allWords, true));
					blink(allWords);
					for (Letter l : human.getTray()) {
						l.setSelected(false);
					}
					break;
				} else {
					for (Square s : word) {
						human.getTray().add(s.getLetter());
						s.setLetter(null);
					}
					for (Letter l : human.getTray()) {
						l.setSelected(false);
					}
					makeMove(human);
					break;
				}
			}

			/* If the reset button is clicked on. */
			if (resetClickOn() && !word.isEmpty()) {
				for (Square s : word) {
					human.getTray().add(s.getLetter());
					s.setLetter(null);
				}
				for (Letter l : human.getTray()) {
					l.setSelected(false);
				}
				
				shuffle(human.getTray());
				makeMove(human);
				break;
			}

			/* If the pass button is clicked on. */
			if (passClickOn()) {
				for (Square s : word) {
					human.getTray().add(s.getLetter());
					s.setLetter(null);
				}
				for (Letter l : human.getTray()) {
					l.setSelected(false);
				}
				humanPass = true;
				break;
			}

			/* If a letter has been clicked on */
			if (human.letterClickOn() != null) {
				if (tempLetter != null) {
					tempLetter.setSelected(false);
				}
				tempLetter = human.letterClickOn();
				tempLetter.setSelected(true);
			}

			tempSquare = board.clickOn();

			while (mousePressed()) {
			}

			/* If a letter and a square have been clicked on */
			if (tempLetter != null && tempSquare != null) {
				/* If this is the first letter placed */
				if (first == null) {
					first = tempSquare;
				}
				tempSquare.setLetter(tempLetter);
				word.add(tempSquare);
				tempLetter.setSelected(false);
				human.getTray().remove(tempLetter);
				tempLetter = null;
				tempSquare = null;
			}
			draw();
		}
	}

	/**
	 * If it is not the first move, traverses each non-empty square in board,
	 * finding the highest scoring anagram possible and places it.
	 **/
	private void makeMove(AI ai) {

		aiPass = false;

		/* If it is the first play of the game */
		if (board.isEmpty()) {
			char[] characters = ai.findBestWord(ai.findAnagrams(' ')).toCharArray();
			ArrayList<Square> word = new ArrayList<Square>();
			/* For each character in word */
			for (int i = 0; i < characters.length; i++) {
				/* For each letter in tray */
				for (Letter letter : ai.getTray()) {
					if (letter.getCharacter() == characters[i]) {
						board.putLetter(ai.getTray().remove(ai.getTray().indexOf(letter)), 7, 7 + i);
						word.add(board.getSquare(7, 7 + i));
						break;
					}
				}
			}
			ArrayList<ArrayList<Square>> allWords = new ArrayList<ArrayList<Square>>();
			allWords.add(word);
			ai.addScore(findScore(allWords, true));
		} else {
			/* For each square in board, find the best play */
			for (int r = 0; r < 15; r++) {
				for (int c = 0; c < 15; c++) {
					Square tempSquare = board.getSquare(r, c);
					if (tempSquare.getLetter() != null) {
						findBestMove(tempSquare, ai.findAnagrams(tempSquare.getLetter().getCharacter()), ai);
					}
				}
			}
			placeBestMove();
		}
	}

	/** Draws the current game state. **/
	private void draw() {

		show(0);
		clear(BACKGROUND);

		/* Draws the side and the tray. */
		setPenRadius(0.004);
		setPenColor(LIGHT_BROWN);
		filledRectangle(SIDE_CENTER[0], SIDE_CENTER[1], 177, 337.5);
		filledRectangle(TRAY_CENTER[0], TRAY_CENTER[1], 337.5, 50);

		/* Draws the buttons and their borders. */
		setPenColor(BROWN);
		filledRectangle(SUBMIT_BUTTON_CENTER[0], SUBMIT_BUTTON_CENTER[1], 55, 25);
		filledRectangle(RESET_BUTTON_CENTER[0], RESET_BUTTON_CENTER[1], 55, 15);
		filledRectangle(PASS_BUTTON_CENTER[0], PASS_BUTTON_CENTER[1], 55, 15);
		filledRectangle(SIDE_CENTER[0], SIDE_CENTER[1] + 300, 100, 25);
		setPenColor(BLACK);
		rectangle(SIDE_CENTER[0], SIDE_CENTER[1], 177, 337.5);
		rectangle(TRAY_CENTER[0], TRAY_CENTER[1], 337.5, 50);
		rectangle(SUBMIT_BUTTON_CENTER[0], SUBMIT_BUTTON_CENTER[1], 55, 25);
		rectangle(RESET_BUTTON_CENTER[0], RESET_BUTTON_CENTER[1], 55, 15);
		rectangle(PASS_BUTTON_CENTER[0], PASS_BUTTON_CENTER[1], 55, 15);

		/* Draw the square color key */
		setPenColor(CYAN);
		filledSquare(COLOR_KEY_CENTER[0], COLOR_KEY_CENTER[1], 20);
		setPenColor(BLUE);
		filledSquare(COLOR_KEY_CENTER[0], COLOR_KEY_CENTER[1] - 55, 20);
		setPenColor(PINK);
		filledSquare(COLOR_KEY_CENTER[0], COLOR_KEY_CENTER[1] - 110, 20);
		setPenColor(ORANGE);
		filledSquare(COLOR_KEY_CENTER[0], COLOR_KEY_CENTER[1] - 165, 20);
		setPenColor(BLACK);

		/* Draws the text. */
		setFont(BIG);
		textLeft(COLOR_KEY_CENTER[0] + 20, COLOR_KEY_CENTER[1], " = Double Letter");
		textLeft(COLOR_KEY_CENTER[0] + 20, COLOR_KEY_CENTER[1] - 55, " = Triple Letter");
		textLeft(COLOR_KEY_CENTER[0] + 20, COLOR_KEY_CENTER[1] - 110, " = Double Word");
		textLeft(COLOR_KEY_CENTER[0] + 20, COLOR_KEY_CENTER[1] - 165, " = Triple Word");

		text(SIDE_CENTER[0], SIDE_CENTER[1] + 300, "Scrabble");

		text(SIDE_CENTER[0], SIDE_CENTER[1] - 300, "Tiles left : " + bag.size());
		text(SUBMIT_BUTTON_CENTER[0], SUBMIT_BUTTON_CENTER[1], "Submit");
		text(RESET_BUTTON_CENTER[0], RESET_BUTTON_CENTER[1], "Reset");
		text(PASS_BUTTON_CENTER[0], PASS_BUTTON_CENTER[1], "Pass");

		board.draw();
		human.draw();
		ai.draw();
		show(0);
	}

	/**
	 * Given a list of words, changes the color of their letters to red then back
	 * again.
	 **/
	private void blink(ArrayList<ArrayList<Square>> allWords) {
		draw();
		show(400);
		for (ArrayList<Square> w : allWords) {
			for (Square s : w) {
				s.getLetter().setBlink(true);
			}
		}
		draw();
		show(400);
		for (ArrayList<Square> w : allWords) {
			for (Square s : w) {
				s.getLetter().setBlink(false);
			}
		}
	}
	
	/** Shuffles an ArrayList of Letters. **/
	 private static void shuffle(ArrayList<Letter> arrayList) {
	        int size = arrayList.size();
	        for (int i = 0; i < size; i++) {
	            int random = i + uniform(size-i);
	            Letter temp = arrayList.get(i);
	            arrayList.set(i, arrayList.get(random));
	            arrayList.set(random, temp);
	        }
	    }

	/** Loops through each player's turn until both players pass. **/
	private void run() {
		
		while (true) {
			human.setCurrentPlayer(false);
			ai.setCurrentPlayer(true);
			draw();
			makeMove(ai);
			fillTray(ai);
			ai.setCurrentPlayer(false);
			human.setCurrentPlayer(true);
			draw();

			makeMove(human);
			fillTray(human);
			draw();

			if (humanPass && aiPass) {
				break;
			}
		}

		String victor;

		if (ai.getScore() > human.getScore()) {
			victor = ai.getName();
		} else {
			victor = human.getName();
		}

		setPenRadius(0.004);
		setPenColor(LIGHT_BROWN);
		filledRectangle(TRAY_CENTER[0], TRAY_CENTER[1], 337.5, 50);
		setPenColor(BLACK);
		rectangle(TRAY_CENTER[0], TRAY_CENTER[1], 337.5, 50);
		setPenColor(RED);
		text(TRAY_CENTER[0], TRAY_CENTER[1], victor + " Won!");
		show();

	}

	public static void main(String[] args) {
		Game game = new Game();
		setCanvasSize(1030, 800);
		setXscale(0, 1030);
		setYscale(0, 800);
		game.run();
	}

}
