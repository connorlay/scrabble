package scrabble;

import java.awt.Font;
import java.util.ArrayList;

import edu.princeton.cs.introcs.StdDraw;

public class Human implements Player {

	String name;
	int score;
	ArrayList<Letter> tray;
	String[] dictionary;

	private final Font fontBig = new Font("Courier", Font.BOLD, 25);
	private boolean currentPlayer;

	public Human(String newName, String[] dictionary) {
		name = newName;
		score = 0;
		tray = new ArrayList<Letter>();
		this.dictionary = dictionary;
		currentPlayer = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public void addScore(int newScore) {
		score  += newScore;
	}

	@Override
	public int getNumberOfLetters() {
		return tray.size();
	}

	@Override
	public void addLetter(Letter newLetter) {
		tray.add(newLetter);
	}

	// Which letter in the tray has the user clicked on?
	public Letter letterClickOn() {
		for (int i = 0; i < tray.size(); i++) {
			if (tray.get(i).clickOn()) {
				return tray.get(i);
			}
		}
		return null;
	}


	@Override
	public void draw() {

		double trayCenter = 438;
		double tileStart = 938 - trayCenter + 20;
		double[] sideCenter = { 160, 450 };

		for (int i = 0; i < tray.size(); i++) {
			tray.get(i).draw(tileStart + i * 60, 40);
			if (tray.get(i).getSelected()){
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(tray.get(i).getCoordinates()[0], tray.get(i).getCoordinates()[1], 20);
			}
		}
		StdDraw.setFont(fontBig);
		if(currentPlayer){
			StdDraw.setPenColor(StdDraw.RED);
		} else {
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		StdDraw.textLeft(sideCenter[0] - 150, sideCenter[1] + 210, name + ": ");
		StdDraw.textRight(sideCenter[0] + 140, sideCenter[1] + 210, score + "");

	}

	@Override
	public ArrayList<Letter> getTray() {
		return tray;
	}

	@Override
	public void setCurrentPlayer(boolean value) {
		currentPlayer = value;
	}

}
