// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Player;
import model.Resource;

public class ScoreBoard extends Group {

	private final Map<Player, Map<Resource, Text>> resourceLabels;

	public ScoreBoard(Map<Player, Color> players, Double size) {
		this.resourceLabels = new HashMap<Player, Map<Resource, Text>>();

		for(Player player : players.keySet()) {
			this.resourceLabels.put(player, new HashMap<Resource, Text>());
		}


		//represent "SH", "WH", ST", "BR", "WO" on the top of score board
		for(int i = 0; i < Resource.values().length; i++) {
			if(Resource.values()[i] != Resource.DESERT) {
				Text resourceLabel = new Text();
				resourceLabel.setText(Resource.values()[i].toString());
				resourceLabel.setRotate(90);
				resourceLabel.setTranslateX(1.5 * size + (i * 50));
				resourceLabel.setTranslateY(size);
				this.getChildren().add(resourceLabel);
			}
		}


		//represent "Player 1,2,3"
		int i = 1;
		for(Player player : players.keySet()) {
			Text playerLabel = new Text();
			playerLabel.setText("Player "+ i + ": ");
			playerLabel.setY(2 * i * size);
			playerLabel.setFill(players.get(player));
			playerLabel.setFont(Font.font ("Verdana", 20));
			this.getChildren().add(playerLabel);
			//show the points of a player according to resource amount of that player
			for(int j = 0; j < Resource.values().length; j++) {
				if(Resource.values()[j] != Resource.DESERT) {
					Text resourceLabel = new Text();
					resourceLabel.setText(player.getResourceAmount(Resource.values()[j]).toString());
					resourceLabel.setY(2 * i * size);
					resourceLabel.setTranslateX(1.5 * size + (j * 50));
					this.getChildren().add(resourceLabel);
					this.resourceLabels.get(player).put(Resource.values()[j], resourceLabel);
				}
			}
			i++;
		}

	}

	public void updateScores() {
		for(Player player : resourceLabels.keySet()) {
			for(Resource r : resourceLabels.get(player).keySet()) {
				resourceLabels.get(player).get(r).setText(player.getResourceAmount(r).toString());

			}
		}
	}

}
