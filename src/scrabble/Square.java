package scrabble;

import java.awt.Color;
import java.util.ArrayList;

import edu.princeton.cs.introcs.StdDraw;

public class Square {

	private Letter letter;
	private double[] coordinates;
	private int[] position;
	private int letterMult;
	private int wordMult;
	private Square top;
	private Square bottom;
	private Square left;
	private Square right;

	private final Color blue = new Color(61, 89, 171);
	private final Color cyan = new Color(0, 197, 205);
	private final Color pink = new Color(255, 62, 150);
	private final Color orange = new Color(255, 140, 0);
	private final Color lightBrown = new Color(238, 197, 145);

	public Square(Letter newLetter, double X, double Y, int R, int C) {
		letter = newLetter;
		coordinates = new double[2];
		coordinates[0] = X;
		coordinates[1] = Y;
		position = new int[2];
		position[0] = R;
		position[1] = C;
		wordMult = 1;
		letterMult = 1;
	}

	public void setLetter(Letter newTile) {
		letter = newTile;
	}

	public void setLetterMult(int newLetterMult) {
		letterMult = newLetterMult;
	}

	public void setWordMult(int newWordMult) {
		wordMult = newWordMult;
	}

	public void addTop(Square square) {
		top = square;
	}

	public void addBottom(Square square) {
		bottom = square;
	}

	public void addLeft(Square square) {
		left = square;
	}

	public void addRight(Square square) {
		right = square;
	}

	public Square getTop() {
		return top;
	}

	public Square getBottom() {
		return bottom;
	}

	public Square getLeft() {
		return left;
	}

	public Square getRight() {
		return right;
	}

	/*
	 * Given a list of Squares, does this square have a neighbor not in the
	 * list?
	 */
	public boolean hasUniqueNeighbor(ArrayList<Square> word) {
		return getTop() != null && getTop().getLetter() != null
				&& !word.contains(getTop()) || getBottom() != null
				&& getBottom().getLetter() != null
				&& !word.contains(getBottom()) || getLeft() != null
				&& getLeft().getLetter() != null && !word.contains(getLeft())
				|| getRight() != null && getRight().getLetter() != null
				&& !word.contains(getRight());
	}

	public Letter getLetter() {
		return letter;
	}

	public int getLetterMult() {
		return letterMult;
	}

	public int getWordMult() {
		return wordMult;
	}

	public int[] getPosition() {
		return position;
	}

	public boolean clickOn() {
		if (StdDraw.mouseX() > coordinates[0] - 20
				&& StdDraw.mouseX() < coordinates[0] + 20
				&& StdDraw.mouseY() > coordinates[1] - 20
				&& StdDraw.mouseY() < coordinates[1] + 20) {
			return true;
		}
		return false;
	}

	public void drawSquare() {

		if (letterMult == 2) {
			StdDraw.setPenColor(cyan);
		} else if (letterMult == 3) {
			StdDraw.setPenColor(blue);
		} else if (wordMult == 2) {
			StdDraw.setPenColor(pink);
		} else if (wordMult == 3) {
			StdDraw.setPenColor(orange);
		} else {
			StdDraw.setPenColor(lightBrown);
		}
		StdDraw.filledSquare(coordinates[0], coordinates[1], 20);
		StdDraw.setPenColor(StdDraw.BLACK);
		if (letter != null) {
			letter.draw(coordinates[0], coordinates[1]);
		}
	}

	public boolean isEmpty() {
		return letter == null;
	}

}
