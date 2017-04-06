package Snake;

import processing.core.PApplet;
import java.awt.Point;
import java.util.*;

/**
 * This class creates a snake object. The snake can move around, eat food and
 * show its full body depending on how many food it has eaten.
 * 
 * @author Han Zhang
 * @version 4/25/16
 *
 */

class Snake {
	// Create x and y coordinates for the snake's head
	protected Float x;
	protected Float y;

	// Create the parameters of the snake
	protected int size;
	protected float Speed;
	protected char direction;
	protected int foodEaten;
	private int snakeLength;
	// A list of points the snake has traveled
	protected List<Point> points = new ArrayList<Point>();
	// The platform the snake will appear
	PApplet canvas;

	/**
	 * Constructor
	 * 
	 * @param canvas the processing platform the snake will appear
	 */
	public Snake(PApplet canvas) {
		// Setting default values for the parameters
		this.canvas = canvas;
		size = 20;
		x = Float.valueOf(250f);
		y = Float.valueOf(125f);
		Speed = 2;
		foodEaten = 0;

	}

	/**
	 * This method moves the snake to a given direction
	 * 
	 * @param direction a given direction that the snake will move
	 */
	public void move(char direction) {
		// If the direction is left
		if (direction == 'l') {
			x = x - Speed;
		}
		// Or if the direction is right
		else if (direction == 'r') {
			x = x + Speed;
		}
		// Or if the direction is up
		else if (direction == 'u') {
			y = y - Speed;
		}
		// Or if the direction is down
		else if (direction == 'd') {
			y = y + Speed;
		}
		// Create the snake head
		this.canvas.rect(x, y, size, size);

	}

	/**
	 * This method allows the snake to eat a piece of food
	 * 
	 * @param snack the Food object that will be eaten
	 * @return true if the snake touches the food 
	 * false if the snake doesn't
	 */
	public boolean eat(Food snack) {
		// If the snake touches the food, calculated by measuring the distance
		// between them
		if (Math.abs(this.x + this.size / 2 - snack.x) < snack.radius
				&& Math.abs(this.y + this.size / 2 - snack.y) < snack.radius) {
			// If true, the food is eaten and the eat method returns true
			this.foodEaten += 1;
			return true;
		}
		// Otherwise the eat method returns false
		return false;
	}

	/**
	 * This method shows the snake's body
	 * 
	 * @param index the index of the body that will be showed
	 */
	public void showBody(int index) {
		// Creating the shape of the body to the platform
		this.canvas.rect(this.points.get(index).x - size / 2, this.points.get(index).y - size / 2, this.size,
				this.size);
	}

	/**
	 * Getter for the snake's size
	 * 
	 * @return snakeLength the size of the snake
	 */
	public int getSnakeLength() {
		// Gets the snake's size
		return snakeLength;
	}

	/**
	 * Setter for the snake's size
	 * 
	 * @param snakeLength the size of the snake
	 */
	public void setSnakeLength(int snakeLength) {
		// Sets the snake's size
		this.snakeLength = snakeLength;
	}

}
