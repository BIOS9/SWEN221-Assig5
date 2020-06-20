// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import model.Player;
import model.Settlement;

/**
 * A City Polygon.
 *
 * @author Julian Mackay
 *
 */
public class CityImg extends Polygon {

	private final Settlement s;
	private final Double size;
	private final Color colour;
	private final Pair<Double, Double> coords;

	public CityImg (Settlement s, Double size, Color colour, Pair<Double, Double> coords) {
		this.s = s;
		this.size = size;
		this.colour = colour;
		this.coords = coords;
		this.drawCity();
	}

	/**
	 * Draw city according to the size of the game.
	 */
	private void drawCity() {

		Double townSize = this.size / 20.0;
		
		this.getPoints()
		.addAll(new Double[] { 
				this.coords.getKey(), this.coords.getValue() - (4 * townSize),
				this.coords.getKey() - (1.5 * townSize), this.coords.getValue() - townSize,
				this.coords.getKey() - (4.5 * townSize), this.coords.getValue() - townSize,
				this.coords.getKey() - (4.5 * townSize), this.coords.getValue() + townSize,
				this.coords.getKey() - (1.5 * townSize), this.coords.getValue() + townSize,
				this.coords.getKey() + (1.5 * townSize), this.coords.getValue() + townSize,
				this.coords.getKey() + (1.5 * townSize), this.coords.getValue() - townSize,
				});
		

        this.setFill(this.colour);
        this.setStroke(Color.BLACK);
    }

	public Player getPlayer() {
		return this.s.getPlayer();
	}

	public Settlement getSettlement() {
		return this.s;
	}

}
