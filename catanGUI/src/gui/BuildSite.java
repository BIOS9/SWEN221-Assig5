// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

public class BuildSite extends Circle {

	public BuildSite(Double size) {
		super(size / 3.0);
		this.setStroke(Color.AQUA);
		this.setFill(Color.TRANSPARENT);
	}

	public void configure(Pair<Double, Double> coords) {

		// put each build site on a corner
		this.setLayoutX(coords.getKey());
		this.setLayoutY(coords.getValue());

		// Set the colour of the fill and stroke for each site
		this.setStroke(Color.AQUA);
		this.setFill(Color.AQUA);
		this.setOpacity(0.3);

	}

}
