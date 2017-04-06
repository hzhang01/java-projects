package Snake;

import processing.core.PApplet;

/**
 * This class creates a Food for the snake game.
 * The food's location can be randomly generated. 
 * 
 * @author Han Zhang
 * @version 4/25/16
 *
 */

class Food {
	//Setting location and the size of the food 
	protected float x;
	protected float y;
	protected float radius;
	//The platform the food will be created 
	PApplet canvas;

	/**
	 * Food constructor 
	 * 
	 * @param canvas the platform the food will be created  
	 */
	public Food(PApplet canvas) {
		//Setting up values for the parameters
		this.canvas = canvas;
		radius = 12;
		//Pseudo-randomizing the location of the food 
		x = canvas.random(20f, (float) canvas.width - 20);
		y = canvas.random(20f, (float) canvas.height / 2 - 20);
	}

	/**
	 * This method creates the image for the food 
	 * on the platform 
	 * 
	 */
	public void makeFood() {
		//Drawing the food 
		this.canvas.ellipse(x, y, radius, radius);
	}

}
