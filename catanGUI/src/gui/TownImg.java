// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import model.Player;
import model.Settlement;

public class TownImg extends Polygon {

	private final Settlement s;
	private final Double size;
	private final Color colour;
	private final Pair<Double, Double> coords;

	public TownImg(Settlement s, Double size, Color colour, Pair<Double, Double> coords) {
		this.s = s;
		this.size = size;
		this.colour = colour;
		this.coords = coords;
		this.drawTown();
	}

	private void drawTown() {

		Double townSize = this.size / 20.0;

		this.getPoints()
				.addAll(new Double[] { this.coords.getKey(), this.coords.getValue() - (3 * townSize),
						this.coords.getKey() - (2 * townSize), this.coords.getValue() - townSize,
						this.coords.getKey() - (2 * townSize), this.coords.getValue() + townSize,
						this.coords.getKey() + (2 * townSize), this.coords.getValue() + townSize,
						this.coords.getKey() + (2 * townSize), this.coords.getValue() - townSize });

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
