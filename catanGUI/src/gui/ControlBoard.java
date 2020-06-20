// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import model.Player;

public class ControlBoard extends Group {

	Integer currentPlayer = 0;

	public ControlBoard(Double size, Board b, List<Player> players, ScoreBoard scoreBoard) {

		this.setLayoutY((5 * (size * 1.5)) + (2.5 * size));
		// this.setAutoSizeChildren(true);

		Button setResourcesBtn = new Button("Set Resources");
		setResourcesBtn.setLayoutX(size);

		Button setCountersBtn = new Button("Set Resource Counters");
		setCountersBtn.setLayoutX(size + 200);
		setCountersBtn.setDisable(true);

		Button setupBtn = new Button("End Setup");
		setupBtn.setLayoutX(size + 400);
		setupBtn.setDisable(true);

		Button rollBtn = new Button("Roll Dice");
		rollBtn.setLayoutX(size + 600);
		rollBtn.setDisable(true);

		Dice dice1 = new Dice(new DiceImg().getImages());
		Dice dice2 = new Dice(new DiceImg().getImages());
		HBox diceHbox = new HBox(dice1.getdiceFace(), dice2.getdiceFace());
		diceHbox.setLayoutX(size + 800);
		diceHbox.setVisible(false);

		this.getChildren().addAll(setResourcesBtn, setCountersBtn, setupBtn, rollBtn, diceHbox);

		setResourcesBtn.setOnMouseClicked(event -> {
			setResourcesBtn.setDisable(true);
			b.setResources();
			PauseTransition pause = new PauseTransition(Duration.millis(500 * 19));
			pause.play();
			setCountersBtn.setDisable(false);
		});

		setCountersBtn.setOnMouseClicked(event -> {
			b.setCounters();
			setCountersBtn.setDisable(true);
			setupBtn.setDisable(false);

		});

		setupBtn.setOnMouseClicked(event -> {
			for (Player p : players) {
				b.cleanTownBuildSitesForPlayer(p);
			}
			setupBtn.setDisable(true);
			rollBtn.setDisable(false);

		});

		rollBtn.setOnMouseClicked(event -> {
			rollBtn.setDisable(true);// Disable Button
			diceHbox.setVisible(true); // Enable the dice picture

			Integer[] diceNum = b.rollDice(players.get(currentPlayer));
			// System.out.println("Current Player: " + currentPlayer);

			dice1.setdiceFace(diceNum[0]);
			dice2.setdiceFace(diceNum[1]);

			rollBtn.setDisable(false);// Enable Button
			scoreBoard.updateScores(); // Assign the resources according to dice number

			currentPlayer++;
			if (currentPlayer == players.size()) {
				currentPlayer = 0;
			}

		});
	}

	/**
	 * Check if the game is during initial setting up stage (when the button of "End
	 * Setup" is enabled).
	 *
	 * @return
	 */
	public boolean isInitialSetup() {
		if (!this.getChildren().get(2).isDisabled()) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the game is in progress (when the button of "Roll dice" is visible).
	 *
	 * @return
	 */
	public boolean duringGame() {
		if (this.getChildren().get(4).isVisible())
			return true;
		return false;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}
}
