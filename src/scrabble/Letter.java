package scrabble;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import edu.princeton.cs.introcs.StdDraw;

public class Letter {

	private char character;
	private int value;
	private double[] coordinates;
	private boolean selected;
	private boolean blink;

	private final Color brown = new Color(156, 102, 31);
	private final Font fontBig = new Font("Courier", Font.BOLD, 32);
	private final Font fontSmall = new Font("Courier", Font.BOLD, 11);

	public Letter(char newCharacter, Hashtable<Character, Integer> letterValues) {
		character = newCharacter;
		value = letterValues.get(character);
		selected = false;
		blink = false;
	}

	public char getCharacter() {
		return character;
	}

	public int getValue() {
		return value;
	}
	
	public double[] getCoordinates(){
		return coordinates;
	}
	
	public boolean getSelected(){
		return selected;
	}
	
	public void setSelected(boolean value){
		selected = value;
	}
	
	public void setBlink(boolean value){
		blink = value;
	}
	
	public boolean clickOn(){
		if (StdDraw.mouseX() > coordinates[0] - 20 && StdDraw.mouseX() < coordinates[0] + 20){
			if(StdDraw.mouseY() > coordinates[1] - 20 && StdDraw.mouseY() < coordinates[1] + 20){
				return true;
			}
		}
		return false;
	}

	public void draw(double X, double Y) {
		coordinates = new double[2];
		coordinates[0] = X;
		coordinates[1] = Y;
		
		StdDraw.setPenColor(brown);
		if(blink){
			StdDraw.setPenColor(StdDraw.RED);
		}
		StdDraw.filledSquare(coordinates[0], coordinates[1], 20);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setFont(fontBig);
		StdDraw.text(coordinates[0], coordinates[1], character + "");
		StdDraw.setFont(fontSmall);
		StdDraw.text(coordinates[0] + 13, coordinates[1] - 13, value + "");
	}
}
