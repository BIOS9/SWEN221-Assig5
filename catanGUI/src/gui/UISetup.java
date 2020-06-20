// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Game;
import model.Player;
import model.Resource;
import model.Settlement;
import model.Tile;

/**
 * UI Setup class. Sets the stage.
 *
 * @author Julian Mackay
 *
 */
public class UISetup {

	static ControlBoard controls;
	static ScoreBoard playerBoard;
	static Board board;

	static Integer turn = 0;

	/**
	 * Stage setting method.
	 *
	 * @param primaryStage
	 *            the javafx stage
	 * @param game
	 *            the game to setup
	 */
	public static void setup(Stage primaryStage, Game<Tile> game, Double size) {

		primaryStage.setTitle("Catan");

		Group root = new Group();

		Map<Player, Color> playerColours = new LinkedHashMap<Player, Color>();

		Player playerOne = game.getPlayers().get(0);
		Player playerTwo = game.getPlayers().get(1);
		Player playerThree = game.getPlayers().get(2);

		playerColours.put(playerOne, Color.ORANGE);
		playerColours.put(playerTwo, Color.RED);
		playerColours.put(playerThree, Color.GREEN);

		board = new Board(size, playerColours, game);
		board.generateBoard();
		board.addToGroup(root);

		List<Player> players = new ArrayList<Player>();
		players.add(playerOne);
		players.add(playerTwo);
		players.add(playerThree);

		playerBoard = new ScoreBoard(playerColours, size);
		controls = new ControlBoard(size, board, players, playerBoard);

		root.getChildren().add(controls);
		playerBoard.setTranslateX(11 * size);
		root.getChildren().add(playerBoard);

		// Town Image setting
		TownImg townOne = new TownImg(new Settlement(playerOne), size, playerColours.get(playerOne),
				new Pair<Double, Double>(size * 0.5, size * 0.5));

		TownImg townTwo = new TownImg(new Settlement(playerTwo), size, playerColours.get(playerTwo),
				new Pair<Double, Double>(size * 0.5, size * 1.5));

		TownImg townThree = new TownImg(new Settlement(playerThree), size, playerColours.get(playerThree),
				new Pair<Double, Double>(size * 0.5, size * 2.5));

		board.registerDraggableTown(townOne, playerOne);
		board.registerDraggableTown(townTwo, playerTwo);
		board.registerDraggableTown(townThree, playerThree);

		root.getChildren().add(townOne);
		root.getChildren().add(townTwo);
		root.getChildren().add(townThree);

		// Road Image setting
		RoadImg roadOne = new RoadImg(playerOne, size, playerColours.get(playerOne),
				new Pair<Double, Double>(size, size * 0.5));

		RoadImg roadTwo = new RoadImg(playerTwo, size, playerColours.get(playerTwo),
				new Pair<Double, Double>(size, size * 1.5));

		RoadImg roadThree = new RoadImg(playerThree, size, playerColours.get(playerThree),
				new Pair<Double, Double>(size, size * 2.5));

		board.registerDraggableRoad(roadOne, playerOne);
		board.registerDraggableRoad(roadTwo, playerTwo);
		board.registerDraggableRoad(roadThree, playerThree);

		root.getChildren().add(roadOne);
		root.getChildren().add(roadTwo);
		root.getChildren().add(roadThree);

		// Register the settlements and roads to be draggable.
		registerItemAsDraggable(townOne, roadOne, playerOne, board);
		registerItemAsDraggable(townTwo, roadTwo, playerTwo, board);
		registerItemAsDraggable(townThree, roadThree, playerThree, board);

		primaryStage.setScene(new Scene(root, 16 * size, 12 * size));
		primaryStage.show();

	}

	/**
	 * Drag settlement or road to the board.
	 *
	 * @param town
	 *            The town to drag
	 * @param road
	 *            The road to drag
	 * @param player
	 *            Current player
	 * @param b
	 *
	 */
	private static void registerItemAsDraggable(TownImg town, RoadImg road, Player player, Board b) {
		town.setOnDragDetected((MouseEvent event) -> {

			// The town can only be dragged during its turn
			if (playerSettlementTurn(player, b)) {
				Dragboard db = town.startDragAndDrop(TransferMode.ANY);

				ClipboardContent content = new ClipboardContent();
				content.putString("town");
				db.setContent(content);

				// highlight the sites available for the current player to place a town
				board.highlightAvailableTownSites(player);

				event.consume();
			}
		});

		// When finish dragging, goes to next player's road turn.
		town.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// dehighlight all sites when the drag is done
				board.dehighlightAvailableTownSites();

				// During the initial setup period, each player can build 2 settlements and 2
				// roads in total.
				if (b.getSettlementAmount(player) <= 2) {
					turn = turn + 1;
				}

				// if any player has 10 settlements on the board, the player wins the game.
				if (b.getSettlementAmount(player) == 10) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Game End");
					alert.setHeaderText(null);
					alert.setContentText("Player " + (player.getID() + 1) + " wins the game!");
					alert.showAndWait();
				}

				event.consume();
			}
		});

		// The road can only be dragged during its turn.
		road.setOnDragDetected((MouseEvent event) -> {

			if (playerRoadTurn(player, b)) {
				Dragboard db = road.startDragAndDrop(TransferMode.ANY);

				ClipboardContent content = new ClipboardContent();
				content.putString("road");
				db.setContent(content);

				// highlight the sites available for the current player to place a town
				board.highlightAvailableRoadSites(player);
				event.consume();
			}
		});

		// When finish dragging, goes to next player's settlement turn.
		road.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				// dehighlight all sites when the drag is done
				board.dehighlightAvailableRoadSites();

				// During the initial setup period, each player can build 2 settlements and 2
				// roads in total.
				if (b.getRoadAmount(player) <= 2) {
					turn = turn + 1;
				}

				event.consume();
			}
		});

	}

	/**
	 * Check if during a given player's settlement turn. Settlements and roads can
	 * only be dragged during the corresponding player's turn.
	 *
	 * @param player
	 *            The given player.
	 * @param b
	 *            Current game board.
	 * @return
	 */
	private static boolean playerSettlementTurn(Player player, Board b) {

		// During the initial setup period, playing turn should be player 1,2,3,3,2,1
		if (controls.isInitialSetup()) {
			if (player.getID() == 0 && (turn == 0 || turn == 10)) {
				return true;
			}
			if (player.getID() == 1 && (turn == 2 || turn == 8)) {
				return true;
			}
			if (player.getID() == 2 && (turn == 4 || turn == 6)) {
				return true;
			}

		}

		// During the game, playing tuan should be player 1,2,3
		if (controls.duringGame()) {
			if ((controls.getCurrentPlayer() == 0 && player.getID() == 2)
					|| (controls.getCurrentPlayer() == player.getID() + 1)) {
				// A player can only build a settlement with required resources, then
				// corresponding resources
				// should be consumed. A town needs 1 brick, 1 sheep and 1 wheat.
				if (player.getResourceAmount(Resource.BRICK) >= 1 && player.getResourceAmount(Resource.SHEEP) >= 1
						&& player.getResourceAmount(Resource.WHEAT) >= 1) {

					player.addResource(Resource.BRICK, -1);
					player.addResource(Resource.SHEEP, -1);
					player.addResource(Resource.WHEAT, -1);

					return true;
				}

			}
		}

		return false;

	}

	private static boolean playerRoadTurn(Player player, Board b) {

		// During the initial setup period, playing turn should be player 1,2,3,3,2,1
		if (controls.isInitialSetup()) {
			if (player.getID() == 0 && (turn == 1 || turn == 11)) {
				return true;
			}
			if (player.getID() == 1 && (turn == 3 || turn == 9)) {
				return true;
			}
			if (player.getID() == 2 && (turn == 5 || turn == 7)) {
				return true;
			}

		}

		// During the game, playing tuan should be player 1,2,3
		if (controls.duringGame()) {
			if ((controls.getCurrentPlayer() == 0 && player.getID() == 2)
					|| (controls.getCurrentPlayer() == player.getID() + 1)) {
				// A player can only build a road with required resources, then corresponding
				// resources
				// should be consumed. A road needs 1 brick, and 1 wood.
				if (player.getResourceAmount(Resource.BRICK) >= 1 && player.getResourceAmount(Resource.WOOD) >= 1) {

					player.addResource(Resource.BRICK, -1);
					player.addResource(Resource.WOOD, -1);
					return true;
				}

			}
		}

		return false;

	}

}
