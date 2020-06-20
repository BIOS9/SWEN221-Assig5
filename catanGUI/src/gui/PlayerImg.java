// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import javafx.scene.paint.Color;
import model.Player;

public class PlayerImg {

	private final Player player;
	private final Color colour;

	public PlayerImg(Player player, Color colour) {
		this.player = player;
		this.colour = colour;
	}

	public Color getColour() {
		return this.colour;
	}

}
