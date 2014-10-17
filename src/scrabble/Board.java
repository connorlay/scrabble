package scrabble;

import java.util.ArrayList;

import edu.princeton.cs.introcs.StdDraw;

public class Board {

	private Square[][] squares;
	int numberOfTiles;
	String[] dictionary;

	double[] boardCenter = { 700, 450 };
	double boardWidth = 337.5;
	double columnStart = boardCenter[0] - boardWidth;
	double rowStart = boardCenter[1] - boardWidth;

	public Board(String[] dictionary) {

		this.dictionary = dictionary;
		squares = new Square[15][15];
		numberOfTiles = 0;

		// fills squares with new instances of square
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				squares[i][j] = new Square(null, (j * (2 * boardWidth / 15)) + columnStart + (boardWidth / 15), (i * (2 * boardWidth / 15)) + rowStart
						+ (boardWidth / 15), i, j);
			}
		}

		// tells each square who its neighbors are
		for (int r = 0; r < 15; r++) {
			for (int c = 0; c < 15; c++) {
				Square temp = squares[r][c];
				if (r > 0) {
					temp.addBottom(squares[r - 1][c]); // add bottom
				}
				if (c > 0) {
					temp.addLeft(squares[r][c - 1]); // add left
				}
				if (c < 14) {
					temp.addRight(squares[r][c + 1]); // add right
				}
				if (r < 14) {
					temp.addTop(squares[r + 1][c]); // add top
				}
			}
		}

		// adds in triple word multipliers
		squares[0][0].setWordMult(3);
		squares[7][0].setWordMult(3);
		squares[14][0].setWordMult(3);
		squares[0][7].setWordMult(3);
		squares[14][7].setWordMult(3);
		squares[0][14].setWordMult(3);
		squares[7][14].setWordMult(3);
		squares[14][14].setWordMult(3);

		// adds in double word multipliers
		squares[1][1].setWordMult(2);
		squares[2][2].setWordMult(2);
		squares[3][3].setWordMult(2);
		squares[4][4].setWordMult(2);
		squares[10][10].setWordMult(2);
		squares[11][11].setWordMult(2);
		squares[12][12].setWordMult(2);
		squares[13][13].setWordMult(2);
		squares[13][1].setWordMult(2);
		squares[12][2].setWordMult(2);
		squares[11][3].setWordMult(2);
		squares[10][4].setWordMult(2);
		squares[4][10].setWordMult(2);
		squares[3][11].setWordMult(2);
		squares[2][12].setWordMult(2);
		squares[1][13].setWordMult(2);

		// adds in triple letter multipliers
		squares[5][1].setLetterMult(3);
		squares[9][1].setLetterMult(3);
		squares[1][5].setLetterMult(3);
		squares[1][9].setLetterMult(3);
		squares[5][5].setLetterMult(3);
		squares[9][5].setLetterMult(3);
		squares[13][5].setLetterMult(3);
		squares[9][9].setLetterMult(3);
		squares[5][9].setLetterMult(3);
		squares[13][9].setLetterMult(3);
		squares[5][13].setLetterMult(3);
		squares[9][13].setLetterMult(3);

		// adds in double letter multipliers
		squares[6][6].setLetterMult(2);
		squares[6][8].setLetterMult(2);
		squares[8][6].setLetterMult(2);
		squares[8][8].setLetterMult(2);
		squares[0][3].setLetterMult(2);
		squares[0][11].setLetterMult(2);
		squares[2][6].setLetterMult(2);
		squares[2][8].setLetterMult(2);
		squares[3][7].setLetterMult(2);
		squares[6][2].setLetterMult(2);
		squares[8][2].setLetterMult(2);
		squares[7][3].setLetterMult(2);
		squares[12][6].setLetterMult(2);
		squares[12][8].setLetterMult(2);
		squares[11][7].setLetterMult(2);
		squares[8][12].setLetterMult(2);
		squares[6][12].setLetterMult(2);
		squares[7][11].setLetterMult(2);
		squares[10][14].setLetterMult(2);
		squares[4][14].setLetterMult(2);
		squares[14][4].setLetterMult(2);
		squares[14][10].setLetterMult(2);
		squares[4][0].setLetterMult(2);
		squares[10][0].setLetterMult(2);

	}

	/** Returns true if the given word exists in the Scrabble dictionary. **/
	public boolean inDictionary(String word) {
		for (String s : dictionary) {
			if (word.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/** Inserts a given letter into the square at coordinates r, c on the board. **/
	public void putLetter(Letter letter, int r, int c) {
		squares[r][c].setLetter(letter);
		numberOfTiles++;
	}

	/** Returns the square at coordinates r, c on the board. **/
	public Square getSquare(int r, int c) {
		return squares[r][c];
	}

	/** For each square on the board, returns true if the mouse is over it. **/
	public Square clickOn() {
		while (!StdDraw.mousePressed()) {
		}
		for (Square[] r : squares) {
			for (Square s : r) {
				if (s.clickOn() && s.getLetter() == null) {
					return s;
				}
			}
		}
		return null;
	}

	/** Returns true if there are no letters in any of the squares in board. **/
	public boolean isEmpty() {
		return numberOfTiles == 0;
	}

	/**
	 * Given a starting square and a direction, constructs a word and returns an
	 * ArrayList of squares that contain its letters.
	 **/
	public ArrayList<Square> buildWord(Square first, char direction) {
		String word = "";
		ArrayList<Square> fullWord = new ArrayList<Square>();
		Square tempSquare = first;

		if (direction == ('h')) {
			if (first.getBottom() != null && first.getBottom().getLetter() != null || first.getTop() != null && first.getTop().getLetter() != null) {
				/* Add first */
				word += tempSquare.getLetter().getCharacter();
				fullWord.add(tempSquare);
				/* If there are letters below first */
				if (first.getBottom() != null && first.getBottom().getLetter() != null) {
					while (tempSquare.getBottom() != null && tempSquare.getBottom().getLetter() != null) {
						word += tempSquare.getBottom().getLetter().getCharacter();
						fullWord.add(tempSquare.getBottom());
						tempSquare = tempSquare.getBottom();
					}
				}
				/* If there are letters above first */
				if (first.getTop() != null && first.getTop().getLetter() != null) {
					tempSquare = first;
					while (tempSquare.getTop() != null && tempSquare.getTop().getLetter() != null) {
						tempSquare = tempSquare.getTop();
						word = tempSquare.getLetter().getCharacter() + word;
						fullWord.add(tempSquare);
					}
				}
			}
		} else if (direction == 'v') {
			if (first.getRight() != null && first.getRight().getLetter() != null || first.getLeft() != null && first.getLeft().getLetter() != null) {
				/* Add first */
				word += tempSquare.getLetter().getCharacter();
				fullWord.add(tempSquare);
				/* If there are letters to the right of first */
				if (first.getRight() != null && first.getRight().getLetter() != null) {
					while (tempSquare.getRight() != null && tempSquare.getRight().getLetter() != null) {
						word += tempSquare.getRight().getLetter().getCharacter();
						fullWord.add(tempSquare.getRight());
						tempSquare = tempSquare.getRight();
					}
				}
				/* If there are letters to the left of first */
				if (first.getLeft() != null && first.getLeft().getLetter() != null) {
					tempSquare = first;
					while (tempSquare.getLeft() != null && tempSquare.getLeft().getLetter() != null) {
						tempSquare = tempSquare.getLeft();
						word = tempSquare.getLetter().getCharacter() + word;
						fullWord.add(tempSquare);
					}
				}
			}
		}

		if (inDictionary(word) || word.isEmpty()) {
			return fullWord;
		} else {
			return null;
		}

	}

	/** Draws the current state of the board. **/
	public void draw() {

		for (int i = 0; i < squares.length; i++) { // draws each square
			for (int j = 0; j < squares.length; j++) {
				squares[i][j].drawSquare();
			}
		}

		StdDraw.square(boardCenter[0], boardCenter[1], boardWidth); // draws the
																	// border of
																	// the board

		for (int i = 1; i < 15; i++) { // draws the lines of the board grid
			StdDraw.line(columnStart + (i * (2 * boardWidth / 15)), boardCenter[1] - boardWidth, columnStart + (i * (2 * boardWidth / 15)), boardCenter[1]
					+ boardWidth);
			StdDraw.line(boardCenter[0] - boardWidth, rowStart + (i * (2 * boardWidth / 15)), boardCenter[0] + boardWidth, rowStart
					+ (i * (2 * boardWidth / 15)));
		}

	}

}
