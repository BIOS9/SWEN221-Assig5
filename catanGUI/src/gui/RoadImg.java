// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import model.Direction;
import model.Player;

public class RoadImg extends Polygon {

	private final Player player;
	private final Double size;
	private final Color colour;
	private final Pair<Double, Double> coords;
	private final Double coEff = Math.sqrt(3) / 4.0;
	private final Direction dir;

	public RoadImg(Player player, Double size, Color colour, Pair<Double, Double> coords, Direction dir) {
		this.player = player;
		this.size = size;
		this.colour = colour;
		this.coords = coords;
		this.dir = dir;
		drawRoad();
	}

	public RoadImg(Player player, Double size, Color colour, Pair<Double, Double> coords) {
		this.player = player;
		this.size = size;
		this.colour = colour;
		this.coords = coords;
		this.dir = Direction.EAST;
		drawRoad();
	}

	private void drawRoad() {

		Double length = 0.6 * size;
		Double width = 0.1 * size;

		if (this.dir == Direction.EAST || this.dir == Direction.WEST) {
			this.getPoints()
					.addAll(new Double[] { this.coords.getKey() - (width * 0.5), this.coords.getValue() - (length * 0.5),
							this.coords.getKey() + (width * 0.5), this.coords.getValue() - (length * 0.5),
							this.coords.getKey() + (width * 0.5), this.coords.getValue() + (length * 0.5),
							this.coords.getKey() - (width * 0.5), this.coords.getValue() + (length * 0.5) });

		} else if (this.dir == Direction.NORTHWEST || this.dir == Direction.SOUTHEAST) {
			this.getPoints()
					.addAll(new Double[] { this.coords.getKey() + (this.coEff * length) - (0.25 * width),
							this.coords.getValue() - (0.25 * length) - (this.coEff * width),
							this.coords.getKey() + (this.coEff * length) + (0.25 * width),
							this.coords.getValue() - (0.25 * length) + (this.coEff * width),
							this.coords.getKey() - (this.coEff * length) + (0.25 * width),
							this.coords.getValue() + (0.25 * length) + (this.coEff * width),
							this.coords.getKey() - (this.coEff * length) - (0.25 * width),
							this.coords.getValue() + (0.25 * length) - (this.coEff * width) });
		} else if (this.dir == Direction.NORTHEAST || this.dir == Direction.SOUTHWEST) {
			this.getPoints()
					.addAll(new Double[] { this.coords.getKey() - (this.coEff * length) - (0.25 * width),
							this.coords.getValue() - (0.25 * length) + (this.coEff * width),
							this.coords.getKey() - (this.coEff * length) + (0.25 * width),
							this.coords.getValue() - (0.25 * length) - (this.coEff * width),
							this.coords.getKey() + (this.coEff * length) + (0.25 * width),
							this.coords.getValue() + (0.25 * length) - (this.coEff * width),
							this.coords.getKey() + (this.coEff * length) - (0.25 * width),
							this.coords.getValue() + (0.25 * length) + (this.coEff * width) });
		}

		this.setFill(colour);
		this.setStroke(Color.BLACK);

	}

	public Player getPlayer() {
		return this.player;
	}

}
