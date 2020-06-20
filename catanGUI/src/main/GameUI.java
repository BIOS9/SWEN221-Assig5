// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package main;

import game.CatanGame;
import gui.UISetup;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * UI class for Settlers of Catan assignment.
 * There is no need to change this class.
 * @author Julian Mackay
 *
 */
public class GameUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		UISetup.setup(primaryStage, new CatanGame(3), 80.0);
	}
}
