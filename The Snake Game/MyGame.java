package Snake;

import java.awt.Point;

import processing.core.PApplet;

/**
 * This class creates the interface and the game mechanics for a snake game. The
 * snake can be moved by using arrow keys. Whenever the game ends the user can
 * press ESC to terminate the game. The goal of the game is 20 points.
 * 
 * @author Han Zhang
 * @version 4/25/16
 *
 */

@SuppressWarnings("serial")
public class MyGame extends PApplet {

	// Setting the dimensions of the canvas
	int xMax = 500;
	int yMax = 500;
	// Setting initial position of the snake
	float x = 250;
	float y = 125;
	// Creating the snake and its food
	Snake viper = new Snake(this);
	Food snakeFood = new Food(this);
	// Creating the gap between the head and the body parts
	int normal_gap = (int) (viper.size / viper.Speed);
	// Creating checkers for game on and game over
	boolean gameOn = false;
	boolean gameOver = false;

	/**
	 * Processing setup method
	 */
	public void setup() {
		// Setting up parameters of the platform
		size(xMax, yMax);
		textSize(32);
		frameRate(30);
	}

	/**
	 * Processing draw method
	 */
	public void draw() {
		// Background color
		background(127, 127, 127);
		stroke(127, 127, 127);

		gameOn = false;
		// If game has not started yet
		if ((!gameOn)) {
			// Print following messages to the user
			textAlign(CENTER);
			text("Snake the Game!", 250, 125);
			textAlign(CENTER);
			text("Welcome Challeger!", 250, 200);
			textAlign(CENTER);
			text("Press Arrow keys to start", 250, 375);
			textAlign(CENTER);
			text("the game", 250, 415);
		}

		// Else the game started with a press of an arrow key
		keyPressed();
		// If the snake starts moving
		if (!(viper.direction == 0)) {
			// The game starts and the current points are shown on the screen
			background(127, 127, 127);
			textAlign(CENTER);
			text("Game Started!", 250, 375);
			textAlign(CENTER);
			text("Points = " + viper.foodEaten, 250, 415);
			rect(0, 250, 500, 5);
			text("Goal = 20", 250, 450);
			rect(0, 250, 500, 5);

			// The food object is created and the viper starts to move
			snakeFood.makeFood();
			viper.move(viper.direction);

			// Store the snake's path into the snake Object
			Point k = new Point(viper.x.intValue() + viper.size / 2, viper.y.intValue() + viper.size / 2);
			viper.points.add(k);
			// If the number of stored values is too high, remove the oldest
			// coordinate in the path list
			if (viper.points.size() > (viper.foodEaten + 1) * normal_gap + 1) {
				viper.points.remove(0);
			}

			// If the snake eats food the body extends by one part
			if (viper.foodEaten > 0) {
				// Drawing every part of the snake depending on its length
				for (int i = viper.getSnakeLength(); i > 0; i--) {
					viper.showBody(viper.points.size() - i * normal_gap);
				}
			}
		}

		// If the food is eaten a new food appears on the screen
		if (viper.eat(snakeFood)) {
			snakeFood = new Food(this);
			snakeFood.makeFood();
			viper.setSnakeLength(viper.getSnakeLength() + 1);
		}

		// If the snake eats a total of 20 food piece the game is over
		if (viper.foodEaten == 20) {
			// A winning screen is shown as the game ends
			background(127, 127, 127);
			textAlign(CENTER);
			text("You Won!", 250, 250);
			textAlign(CENTER);
			text("Press ESC to exit!", 250, 300);
			gameOver = true;
			keyPressed();
		}

		// If the snake touches it own body the game ends
		if (viper.foodEaten > 0) {
			// Looping though the snake's path and determining if the snake's
			// head touches the body
			for (int i = viper.points.size() - viper.size; i >= 0; i--) {
				// If the snake's head touches any of the previous path points
				// depending on the speed
				if (((viper.points.get(i).x == (viper.x + viper.size / 2 - viper.Speed)
						&& viper.points.get(i).y == (viper.y + viper.size / 2 - viper.Speed))
						|| (viper.points.get(i).x == (viper.x - viper.size / 2 + viper.Speed)
								&& viper.points.get(i).y == (viper.y - viper.size / 2 + viper.Speed))
						|| (viper.points.get(i).x == (viper.x + viper.size / 2 - viper.Speed)
								&& viper.points.get(i).y == (viper.y - viper.size / 2 + viper.Speed))
						|| (viper.points.get(i).x == (viper.x - viper.size / 2 + viper.Speed)
								&& viper.points.get(i).y == (viper.y + viper.size / 2 - viper.Speed)))) {
					gameOver();
				}
			}

		}

		// If the viper touches any borders the game ends
		if (viper.x.compareTo(0f) == 0 || viper.y.compareTo(1f) == 0
				|| viper.x.compareTo((float) (xMax - viper.size)) == 1
				|| viper.y.compareTo((float) (yMax / 2 - viper.size)) == 1) {
			gameOver();
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "MyGame" });
	}

	/**
	 * This method produces the game over screen for the user
	 */
	public void gameOver() {
		// Stopping the snake from moving
		viper.direction = 0;

		// Printing out the ending screen
		background(127, 127, 127);
		textAlign(CENTER);
		text("Game Over!", 250, 150);
		textAlign(CENTER);
		text("Better luck next time!", 250, 200);
		textAlign(CENTER);
		text("Press ESC to exit!", 250, 250);
		textAlign(CENTER);
		text("Final Score: " + viper.foodEaten, 250, 415);
		gameOver = true;
		keyPressed();
	}

	/**
	 * This method records directional changes from the user. Depending on the
	 * user's input the snake will turn to (l)eft, (r)ight, (u)p, and (d)own.
	 * (Esc)ape can be pressed only if the game ends.
	 */
	public void keyPressed() {
		// If the key is pressed
		if (keyPressed && key == CODED) {
			// And the key is left arrow key and the game is not over
			if (keyCode == LEFT && gameOver == false) {
				// Check the previous direction to avoid 180 degrees turns to
				// death
				if (viper.direction != 'r')
					// Setting the direction to left
					viper.direction = 'l';
			}
			// And if the key is right arrow key and the game is not over
			else if (keyCode == RIGHT && gameOver == false) {
				// Check the previous direction to avoid 180 degrees turns to
				// death
				if (viper.direction != 'l')
					// Setting the direction to left
					viper.direction = 'r';
			}
			// And if the key is up arrow key and the game is not over
			else if (keyCode == UP && gameOver == false) {
				// Check the previous direction to avoid 180 degrees turns to
				// death
				if (viper.direction != 'd')
					// Setting the direction to left
					viper.direction = 'u';
			}
			// And if the key is down arrow key and the game is not over
			else if (keyCode == DOWN && gameOver == false) {
				// Check the previous direction to avoid 180 degrees turns to
				// death
				if (viper.direction != 'u')
					// Setting the direction to left
					viper.direction = 'd';
			}
			// Finally, if the key is escape and the game is over
			else if (keyCode == ESC && gameOver == true) {
				// Game ends
				this.exit();
			}

		}
	}
}
