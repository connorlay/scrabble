package scrabble;

import java.util.ArrayList;

public interface Player {

	public String getName();
	public int getScore();
	public void addScore(int newScore);
	public void draw();
	/** Returns the number of Letters in a player's tray**/
	public int getNumberOfLetters();
	/** Returns the tray **/
	public ArrayList<Letter> getTray();
	/** Adds a new Letter to the player's tray **/
	public void addLetter(Letter newLetter);
	public void setCurrentPlayer(boolean value);
	
}
