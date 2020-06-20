// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import java.util.HashMap;
import java.util.Map;

import graph.Node;
import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import javafx.util.Pair;
import model.Direction;
import model.Location;
import model.Player;
import model.Resource;
import model.Road;
import model.Settlement;
import model.Tile;

/**
 * The tile image class
 *
 * @author Julian Mackay
 *
 */
public class TileImg extends Group {

	private final Double coEff = Math.sqrt(3.0) / 2.0;

	private final Board board;
	private final Double size;
	private final Map<Player, Color> playerColours;
	private final Map<Player, Map<Location, Shape>> playerTownBuildSites;
	private final Map<Location, Pair<Double, Double>> corners;
	private final Map<Direction, Pair<Double, Double>> roadCoordinates;
	private final Polygon img;
	private final Map<Player, Map<Direction, Shape>> playerRoadBuildSites;
	private final CounterImg resourceCounter;
	private final Map<Player, TownImg> townPalette;
	private final Map<Player, RoadImg> roadPalette;
	private final Node<Direction, Tile> thisNode;

	public TileImg(Board board, Double size, Map<Player, Color> playerColours, Node<Direction, Tile> thisNode) {
		this.board = board;
		this.thisNode = thisNode;
		this.size = size;
		this.playerColours = playerColours;
		this.playerTownBuildSites = new HashMap<Player, Map<Location, Shape>>();
		this.playerRoadBuildSites = new HashMap<Player, Map<Direction, Shape>>();
		this.townPalette = new HashMap<Player, TownImg>();
		this.roadPalette = new HashMap<Player, RoadImg>();

		// Sets the corners of the tile
		this.corners = new HashMap<Location, Pair<Double, Double>>();
		this.corners.put(Location.NORTH, new Pair<Double, Double>(this.coEff * size, 0.0));
		this.corners.put(Location.NORTHWEST, new Pair<Double, Double>(0.0, size * 0.5));
		this.corners.put(Location.SOUTHWEST, new Pair<Double, Double>(0.0, size * 1.5));
		this.corners.put(Location.SOUTH, new Pair<Double, Double>(this.coEff * size, size * 2.0));
		this.corners.put(Location.SOUTHEAST, new Pair<Double, Double>(2.0 * this.coEff * size, size * 1.5));
		this.corners.put(Location.NORTHEAST, new Pair<Double, Double>(2.0 * this.coEff * size, size * 0.5));

		// Sets the road locations of the tile
		this.roadCoordinates = new HashMap<Direction, Pair<Double, Double>>();
		this.roadCoordinates.put(Direction.NORTHWEST, new Pair<Double, Double>(this.coEff * size * 0.5, size * 0.25));
		this.roadCoordinates.put(Direction.NORTHEAST, new Pair<Double, Double>(this.coEff * size * 1.5, size * 0.25));
		this.roadCoordinates.put(Direction.EAST, new Pair<Double, Double>(this.coEff * size * 2.0, size));
		this.roadCoordinates.put(Direction.SOUTHEAST, new Pair<Double, Double>(this.coEff * size * 1.5, size * 1.75));
		this.roadCoordinates.put(Direction.SOUTHWEST, new Pair<Double, Double>(this.coEff * size * 0.5, size * 1.75));
		this.roadCoordinates.put(Direction.WEST, new Pair<Double, Double>(0.0, size));

		// The tile image
		this.img = new Polygon();
		this.img.getPoints()
				.addAll(new Double[] { this.corners.get(Location.NORTH).getKey(),
						this.corners.get(Location.NORTH).getValue(), this.corners.get(Location.NORTHWEST).getKey(),
						this.corners.get(Location.NORTHWEST).getValue(), this.corners.get(Location.SOUTHWEST).getKey(),
						this.corners.get(Location.SOUTHWEST).getValue(), this.corners.get(Location.SOUTH).getKey(),
						this.corners.get(Location.SOUTH).getValue(), this.corners.get(Location.SOUTHEAST).getKey(),
						this.corners.get(Location.SOUTHEAST).getValue(), this.corners.get(Location.NORTHEAST).getKey(),
						this.corners.get(Location.NORTHEAST).getValue() });
		// Set initial tile properties.
		this.img.setFill(Color.BEIGE);
		this.img.setStroke(Color.BLACK);
		this.getChildren().add(this.img);

		// Set initial resource counter properties
		this.resourceCounter = new CounterImg(size / 4.0);
		this.resourceCounter.setFill(Color.TRANSPARENT);
		this.resourceCounter.setStroke(Color.TRANSPARENT);
		this.getChildren().add(this.resourceCounter);

		Translate counterTrans = new Translate();
		counterTrans.setX(size * coEff * 1.25);
		counterTrans.setY(size * 1.35);
		resourceCounter.getTransforms().add(counterTrans);

		/* construct blank build sites as transparent circles */
		for (Player player : playerColours.keySet()) {
			this.constructTownBuildSitesForPlayer(player);
		}
	}

	//Tile Stuff
	/**
	 * set up the sites of potential settlement positions
	 * @param currentPlayer
	 */
	private void constructTownBuildSitesForPlayer(Player currentPlayer) {
		for (Location loc : Location.values()) {
			addTownBuildSite(currentPlayer, loc);
		}
	}

	/**
	 * Sets the resource of the tile.
	 *
	 * @param r
	 * @param delay
	 * @return
	 */
	public void setResource(Resource r, Double delay) {
		PauseTransition pause = new PauseTransition(Duration.millis(delay));
		FillTransition fillTrans = new FillTransition(Duration.millis(2));

		fillTrans.setFromValue(Color.BEIGE);
		fillTrans.setToValue(getResourceColor(r));
		fillTrans.setAutoReverse(false);

		SequentialTransition sequence = new SequentialTransition(this.img, pause, fillTrans);
		sequence.play();
	}
	public static Color getResourceColor(Resource r) {
		switch (r) {
		case WOOD:
			return Color.DARKOLIVEGREEN;

		case BRICK:
			return Color.FIREBRICK;

		case STONE:
			return Color.GREY;

		case WHEAT:
			return Color.GOLD;

		case SHEEP:
			return Color.LIGHTGREEN;

		default:
			return Color.ANTIQUEWHITE;
		}}

	/**
	 * Sets the resource number. This sets the underlying number and draws the
	 * resource counter.
	 *
	 * @param r
	 *            The resource number to be set
	 * @param delay
	 *            the delay before the counter is revealed
	 * @return
	 */
	public Boolean setResourceCounter(Integer i, Double delay) {
		Text number = new Text();

		// set number properties
		number.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, size / 5));
		number.setFill(Color.TRANSPARENT);

		number.setX(size * coEff * 1.1);
		number.setY(size * 1.42);

		number.setText(i.toString());
		this.getChildren().add(number);

		// FillTransition changes the colour of the number on the counter from transparent to black
		FillTransition numberFillTrans = new FillTransition(Duration.millis(2));

		numberFillTrans.setFromValue(Color.TRANSPARENT);
		numberFillTrans.setToValue(Color.BLACK);
		numberFillTrans.setAutoReverse(false);

		PauseTransition pause = new PauseTransition(Duration.millis(delay));

		// FillTransition changes the colour of the counter from transparent to white
		FillTransition fillTrans = new FillTransition(Duration.millis(2));

		fillTrans.setFromValue(Color.TRANSPARENT);
		fillTrans.setToValue(Color.WHITE);
		fillTrans.setAutoReverse(false);

		// StrokeTransition changes the colour of the stroke from transparent to black
		StrokeTransition strokeTrans = new StrokeTransition(Duration.millis(2));

		strokeTrans.setFromValue(Color.TRANSPARENT);
		strokeTrans.setToValue(Color.BLACK);
		strokeTrans.setAutoReverse(false);

		SequentialTransition numberSequence = new SequentialTransition(number, numberFillTrans);
		SequentialTransition sequence = new SequentialTransition(this.resourceCounter, pause, fillTrans, strokeTrans,
				numberSequence);
		sequence.play();

		return true;
	}


	// Settlement(Town) Stuff
	/**
	 * Add a settlement image to a location on the tile
	 *
	 * @param s
	 *            the settlement
	 * @param loc
	 *            the location
	 */
	public void addSettlement(Settlement s, Location loc, Node<Direction, Tile> thisNode) {

		addRoadBuildSite(s.getPlayer(), loc.antiClockwiseDirection(), thisNode);
		addRoadBuildSite(s.getPlayer(), loc.clockwiseDirection(), thisNode);
		TownImg town = new TownImg(s, this.size, this.playerColours.get(s.getPlayer()), this.corners.get(loc));
		this.getChildren().add(town);
		this.playerTownBuildSites.get(s.getPlayer()).put(loc, town);
	}

	/**
	 * Whether there is a settlement of the given player on the given location
	 *
	 * @param player
	 * @param loc
	 * @return
	 */
	public Boolean hasSettlement(Player player, Location loc) {
		return this.playerTownBuildSites.containsKey(player) && this.playerTownBuildSites.get(player).containsKey(loc)
				&& this.playerTownBuildSites.get(player).get(loc).getClass() == TownImg.class;
	}

	/**
	 * Add a build site of town to the given location for a given player
	 *
	 * @param currentPlayer
	 * @param loc
	 */
	public void addTownBuildSite(Player currentPlayer, Location loc) {
		BuildSite buildSite = new BuildSite(size);
		buildSite.configure(this.corners.get(loc));
		// if there is no record of the player in the current set of player build sites
		if (!this.playerTownBuildSites.containsKey(currentPlayer))
			this.playerTownBuildSites.put(currentPlayer, new HashMap<Location, Shape>());
		if (!this.playerTownBuildSites.get(currentPlayer).containsKey(loc)) {
			this.playerTownBuildSites.get(currentPlayer).put(loc, buildSite);
		}
		this.registerTownBuildSiteForDraggable(this.townPalette.get(currentPlayer), currentPlayer);
	}

	/**
	 * Remove the build site of the given player on the given location
	 *
	 * @param player
	 * @param loc
	 */
	public void removeTownBuildSite(Player player, Location loc) {
		if (hasTownBuildSite(player, loc)) {
			this.getChildren().remove(getTownBuildSite(player, loc));
			this.playerTownBuildSites.get(player).remove(loc);
		}
	}

	/**
	 * get the build site for the player at the specified location
	 *
	 * @param player
	 * @param loc
	 * @return
	 */
	private BuildSite getTownBuildSite(Player player, Location loc) {
		if (hasTownBuildSite(player, loc)) {
			return (BuildSite) this.playerTownBuildSites.get(player).get(loc);
		} else
			return null;
	}

	/**
	 * Check if there is an available site to build on for the player at the
	 * location
	 *
	 * @param player
	 *            The player who wished to build
	 * @param loc
	 *            The location they wish to build at
	 * @return Boolean indicating availability of build site.
	 */
	public Boolean hasTownBuildSite(Player player, Location loc) {
		return this.playerTownBuildSites.containsKey(player) && this.playerTownBuildSites.get(player).containsKey(loc)
				&& this.playerTownBuildSites.get(player).get(loc).getClass() == BuildSite.class;
	}

	/**
	 * Highlight build sites available for town
	 *
	 * @param loc
	 * @param player
	 */
	public void highlightAvailableTownBuildSite(Location loc, Player player) {
		if (this.playerTownBuildSites.containsKey(player) && this.playerTownBuildSites.get(player).containsKey(loc)
				&& this.playerTownBuildSites.get(player).get(loc).getClass() == BuildSite.class) {
			this.getChildren().add(this.playerTownBuildSites.get(player).get(loc));
		}
	}

	/**
	 * Dehighlight the buildsites once they are not needed anymore, i.e. once the
	 * item being dragged has been dropped.
	 *
	 * @param loc
	 */
	public void deHighlightAvailableTownBuildSite(Location loc) {

		for (Player player : this.playerTownBuildSites.keySet()) {
			if (this.playerTownBuildSites.containsKey(player) && this.playerTownBuildSites.get(player).containsKey(loc)
					&& this.playerTownBuildSites.get(player).get(loc).getClass() == BuildSite.class) {
				this.getChildren().remove(this.playerTownBuildSites.get(player).get(loc));
			}
		}
	}

	/**
	 * Register town build sites with the build palette
	 */

	/**
	 * Register the build site for draggable towns
	 *
	 * @param source
	 * @param player
	 */
	public void registerTownBuildSiteForDraggable(TownImg source, Player player) {

		for (Location loc : Location.values()) {
			if (this.playerTownBuildSites.containsKey(player) && this.playerTownBuildSites.get(player).containsKey(loc)
					&& this.playerTownBuildSites.get(player).get(loc).getClass() == BuildSite.class) {
				registerOnDragOverTown(source, this.playerTownBuildSites.get(player).get(loc));
				registerOnDragEnteredTown(source, this.playerTownBuildSites.get(player).get(loc));
				registerOnDragDroppedTown(source, this.playerTownBuildSites.get(player).get(loc), loc, player,
						this.thisNode);
				registerOnDragExitedTown(source, this.playerTownBuildSites.get(player).get(loc));
			}

		}
	}

	/**
	 * Register the build site for being dragged over
	 *
	 * @param source
	 * @param target
	 */
	private void registerOnDragOverTown(Shape source, Shape target) {
		target.setOnDragOver((DragEvent event) -> {
			if (event.getGestureSource() != target && event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});
	}

	/**
	 * Register the build site for being dragged enter
	 *
	 * @param source
	 * @param target
	 */
	private void registerOnDragEnteredTown(Shape source, Shape target) {
		target.setOnDragEntered((DragEvent event) -> {
			if (event.getGestureSource() != target && event.getDragboard().hasString()) {
				target.setStroke(Color.BLACK);
				target.setFill(Color.LIGHTGREEN);
			}
			event.consume();

		});
	}

	/**
	 * Register the build site for being dragged drop
	 *
	 * @param source
	 * @param target
	 * @param loc
	 * @param player
	 * @param thisNode
	 */
	private void registerOnDragDroppedTown(Shape source, Shape target, Location loc, Player player,
			Node<Direction, Tile> thisNode) {
		target.setOnDragDropped((DragEvent event) -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				success = true;
			}
			// add a settlement to the location for the player
			Settlement town = new Settlement(player);
			this.board.addSettlement(thisNode, loc, town);
			event.setDropCompleted(success);
			event.consume();
		});

	}

	/**
	 * Register the build site for exiting dragged
	 *
	 * @param source
	 * @param target
	 */
	private void registerOnDragExitedTown(Shape source, Shape target) {
		target.setOnDragExited((DragEvent event) -> {
			target.setStroke(Color.AQUA);
			target.setFill(Color.AQUA);
			event.consume();
		});
	}

	/**
	 * Add the dragged town to the town palette
	 *
	 * @param player
	 * @param source
	 */
	public void addTownToPalette(Player player, TownImg source) {
		if (!this.townPalette.containsKey(player))
			this.townPalette.put(player, source);

	}

	// Road Stuff
	/**
	 * Add a road in the direction specified
	 *
	 * @param r
	 *            the road
	 * @param dir
	 *            the direction
	 */
	public void addRoad(Road r, Direction dir, Node<Direction, Tile> thisNode) {
		if (hasRoadBuildSite(r.getPlayer(), dir)) {
			removeRoadBuildSite(r.getPlayer(), dir);
			RoadImg road = new RoadImg(r.getPlayer(), this.size, this.playerColours.get(r.getPlayer()),
					this.roadCoordinates.get(dir), dir);
			this.getChildren().add(road);
			this.playerRoadBuildSites.get(r.getPlayer()).put(dir, road);
		}
	}

	/**
	 * Whether there is a road on the given direction
	 *
	 * @param dir
	 * @return
	 */
	public Boolean hasRoad(Direction dir) {
		for (Player player : this.playerRoadBuildSites.keySet()) {
			if (this.playerRoadBuildSites.containsKey(player) && this.playerRoadBuildSites.get(player).containsKey(dir)
					&& this.playerRoadBuildSites.get(player).get(dir).getClass() == RoadImg.class) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Whether there is a road of the given player on the given direction
	 *
	 * @param player
	 * @param dir
	 * @return
	 */
	public Boolean playerHasRoad(Player player, Direction dir) {
		return (this.playerRoadBuildSites.containsKey(player) && this.playerRoadBuildSites.get(player).containsKey(dir)
				&& this.playerRoadBuildSites.get(player).get(dir).getClass() == RoadImg.class);
	}

	/**
	 * Add a build site of road to the given direction for a given player
	 *
	 * @param currentPlayer
	 * @param dir
	 * @param thisNode
	 */
	public void addRoadBuildSite(Player currentPlayer, Direction dir, Node<Direction, Tile> thisNode) {
		BuildSite buildSite = new BuildSite(size / 4.0);
		buildSite.configure(this.roadCoordinates.get(dir));
		// if there is no record of the player in the current set of player build sites
		if (!this.playerRoadBuildSites.containsKey(currentPlayer))
			this.playerRoadBuildSites.put(currentPlayer, new HashMap<Direction, Shape>());
		if (!this.playerRoadBuildSites.get(currentPlayer).containsKey(dir)) {
			this.playerRoadBuildSites.get(currentPlayer).put(dir, buildSite);
			this.roadPalette.get(currentPlayer);
			this.registerRoadBuildSiteForDraggable(this.roadPalette.get(currentPlayer), buildSite, dir, currentPlayer,
					thisNode);
		}
	}

	/**
	 * Remove the build site of road on the given direction
	 *
	 * @param player
	 * @param dir
	 */
	private void removeRoadBuildSite(Player player, Direction dir) {
		if (hasRoadBuildSite(player, dir)) {
			this.getChildren().remove(getRoadBuildSite(player, dir));
			this.playerRoadBuildSites.get(player).remove(dir);
		}
	}

	/**
	 * Get the road buildsite in the direction specified if one exists, else null
	 *
	 * @param player
	 *            the player
	 * @param dir
	 *            the direction
	 * @return the build site if it exists, or null if not
	 */
	private BuildSite getRoadBuildSite(Player player, Direction dir) {
		if (hasRoadBuildSite(player, dir)) {
			return (BuildSite) this.playerRoadBuildSites.get(player).get(dir);
		} else {
			return null;
		}
	}

	/**
	 * Check if there is an available site to build on for the player in the
	 * direction
	 *
	 * @param player
	 *            The player
	 * @param dir
	 *            The direction they wish to build a road in
	 * @return Boolean indicating availability of build site.
	 */
	public Boolean hasRoadBuildSite(Player player, Direction dir) {
		return this.playerRoadBuildSites.containsKey(player) && this.playerRoadBuildSites.get(player).containsKey(dir)
				&& this.playerRoadBuildSites.get(player).get(dir).getClass() == BuildSite.class;
	}

	/**
	 * Highlight build sites available for building
	 *
	 * @param dir
	 * @param player
	 */
	public void highlightAvailableRoadBuildSite(Direction dir, Player player) {
		if (this.playerRoadBuildSites.containsKey(player) && this.playerRoadBuildSites.get(player).containsKey(dir)
				&& this.playerRoadBuildSites.get(player).get(dir).getClass() == BuildSite.class) {
			this.getChildren().add(this.playerRoadBuildSites.get(player).get(dir));
		}
	}

	/**
	 * Dehighlight the build sites once they are not needed anymore, i.e. once the
	 * item being dragged has been dropped.
	 *
	 * @param dir
	 */
	public void deHighlightAvailableRoadBuildSite(Direction dir) {

		for (Player player : this.playerRoadBuildSites.keySet()) {
			if (this.playerRoadBuildSites.containsKey(player) && this.playerRoadBuildSites.get(player).containsKey(dir)
					&& this.playerRoadBuildSites.get(player).get(dir).getClass() == BuildSite.class) {
				this.getChildren().remove(this.playerRoadBuildSites.get(player).get(dir));
			}
		}
	}

	/**
	 * Register the build site for draggable roads
	 *
	 * @param source
	 * @param currentNode
	 * @param player
	 */
	public void registerRoadBuildSiteForDraggable(RoadImg source, BuildSite target, Direction dir, Player player,
			Node<Direction, Tile> thisNode) {
		registerOnDragOverRoad(source, target);
		registerOnDragEnteredRoad(source, target);
		registerOnDragDroppedRoad(source, target, dir, player, thisNode);
		registerOnDragExitedRoad(source, target);
	}

	/**
	 * Register the build site for being dragged over
	 *
	 * @param source
	 * @param target
	 */
	private void registerOnDragOverRoad(RoadImg source, Shape target) {
		target.setOnDragOver((DragEvent event) -> {
			if (event.getGestureSource() != target && event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});
	}

	/**
	 * Register the build site for being dragged enter
	 * @param source
	 * @param target
	 */
	private void registerOnDragEnteredRoad(RoadImg source, Shape target) {
		target.setOnDragEntered((DragEvent event) -> {
			if (event.getGestureSource() != target && event.getDragboard().hasString()) {
				target.setStroke(Color.BLACK);
				target.setFill(Color.LIGHTGREEN);
			}
			event.consume();
		});
	}

	/**
	 * Register the build site for being dragged drop
	 * @param source
	 * @param target
	 * @param dir
	 * @param player
	 * @param thisNode
	 */
	private void registerOnDragDroppedRoad(RoadImg source, Shape target, Direction dir, Player player,
			Node<Direction, Tile> thisNode) {
		target.setOnDragDropped((DragEvent event) -> {
			Dragboard db = event.getDragboard();
			boolean success = false;
			if (db.hasString()) {
				success = true;
			}
			// add a road to the location for the player
			Road road = new Road(player);
			this.board.addRoad(thisNode, dir, road);
			//System.out.println("Dropped!!!");
			event.setDropCompleted(success);
			event.consume();
		});
	}

	/**
	 * Register the build site for exiting dragged
	 * @param source
	 * @param target
	 */
	private void registerOnDragExitedRoad(RoadImg source, Shape target) {
		target.setOnDragExited((DragEvent event) -> {
			target.setStroke(Color.AQUA);
			target.setFill(Color.AQUA);
			event.consume();
		});
	}

	/**
	 * Add the dragged road to the road palette
	 * @param player
	 * @param source
	 */
	public void addRoadToPalette(Player player, RoadImg source) {
		if (!this.roadPalette.containsKey(player))
			this.roadPalette.put(player, source);
	}


}
