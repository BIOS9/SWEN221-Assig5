// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import game.HexNode;
import graph.Node;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import model.Direction;
import model.Game;
import model.Location;
import model.Player;
import model.Resource;
import model.ResourceCounter;
import model.Road;
import model.Settlement;
import model.Tile;

/**
 * Board class representing and handling all outside input to the board.
 *
 * @author Julian Mackay
 *
 */
public class Board {

	private final Double size;
	private final Double coEff = Math.sqrt(3.0) / 2.0;
	private final Integer depth = 3;
	private final Map<Player, Color> playerColours;
	private final Map<Node<Direction, Tile>, TileImg> boardMap;
	private final Game<Tile> game;

	public Board(Double size, Map<Player, Color> playerColours, Game<Tile> game) {
		this.size = size;
		this.playerColours = playerColours;
		this.boardMap = new HashMap<Node<Direction, Tile>, TileImg>();
		this.game = game;
	}

	// *****Board generating******//
	public void generateBoard() {
		Stream<Node<Direction, Tile>> gameStream = game.stream();

		if (gameStream != null) {
			gameStream.forEach(n -> {
				TileImg img = new TileImg(this, size, playerColours, n);
				Translate defaultTrans = new Translate();
				defaultTrans.setX(size);
				img.getTransforms().addAll(defaultTrans);
				img.getTransforms().addAll(getPosition(n));
				this.boardMap.put(n, img);
			});
		}
	}

	public Game<Tile> getGame() {
		return this.game;
	}

	private List<Translate> getPosition(Node<Direction, Tile> n) {
		List<Translate> position = new ArrayList<Translate>();

		if (n.hasNeighbor(Direction.NORTHWEST)) {
			position.add(this.transSE());
			position.addAll(getPosition(n.go(Direction.NORTHWEST)));
		} else if (n.hasNeighbor(Direction.NORTHEAST)) {
			position.add(this.transSW());
			position.addAll(getPosition(n.go(Direction.NORTHEAST)));
		} else if (n.hasNeighbor(Direction.WEST)) {
			position.add(this.transE());
			position.addAll(getPosition(n.go(Direction.WEST)));
		} else {
			Translate trans = new Translate();
			trans.setX(((this.coEff * this.size) * (this.depth - 1.0)) + this.size);
			trans.setY(size);
			position.add(trans);
		}

		return position;
	}

	private Translate transE() {
		Translate trans = new Translate();

		trans.setX(this.coEff * this.size * 2.0);

		return trans;
	}

	private Translate transSE() {
		Translate trans = new Translate();

		trans.setX(this.coEff * this.size);
		trans.setY(this.size * (3.0 / 2.0));

		return trans;
	}

	private Translate transSW() {
		Translate trans = new Translate();

		trans.setX(-this.coEff * this.size);
		trans.setY(this.size * (3.0 / 2.0));

		return trans;
	}

	/**
	 * Add the board to the group of game
	 *
	 * @param g
	 */
	public void addToGroup(Group g) {
		if (game.stream() != null) {
			game.stream().forEach(n -> {
				if (this.boardMap.containsKey(n)) {
					g.getChildren().add(this.boardMap.get(n));
				}
			});
		}
	}

	// *****Board setting up******//
	/**
	 * Set up resources for the board
	 */
	public void setResources() {
		try {
			List<Resource> pool = Resource.generateResourcePool();
			this.game.setResources(pool);
			AtomicInteger counter = new AtomicInteger(0);
			this.game.stream().forEach(
				n -> this.boardMap.get(n).setResource(n.getValue().getResource(), counter.getAndIncrement() * 50.0));
		}
		catch(Throwable t) {
			System.out.println("Not enough features implemented. This caused a");
			t.printStackTrace();
		}
	}

	/**
	 * Set up counters on resources
	 */
	public void setCounters() {
		try {
			List<ResourceCounter> pool = ResourceCounter.generateResourceCounters();
			game.setResourceCounters(pool);
			AtomicInteger counter = new AtomicInteger(0);
			game.clockwiseStream().filter(n -> !n.getValue().getResource().equals(Resource.DESERT))
				.forEach(n -> this.boardMap.get(n).setResourceCounter(n.getValue().getResourceNumber(),
					counter.getAndIncrement() * 50.0));
		}
		catch(Throwable t) {
			System.out.println("Not enough features implemented. This caused a");
			t.printStackTrace();
		}
	}

	// ***** Settlement(Town) stuff******//
	/**
	 * Add a settlement on the given location
	 *
	 * @param n
	 * @param loc
	 * @param s
	 */
	public void addSettlement(Node<Direction, Tile> n, Location loc, Settlement s) {

		this.game.addSettlement(n, loc, s);
		TileImg img = this.boardMap.get(n);

		for (Player player : playerColours.keySet()) {
			removeTownBuildSite(n, loc, player);
			if (player != s.getPlayer()) {
				removeTownBuildSite(n, loc.antiClockwise(), player);
				removeTownBuildSite(n, loc.clockwise(), player);
			}
		}

		img.addSettlement(s, loc, n);

		Node<Direction, Tile> adjNode1 = n.go(loc.antiClockwiseDirection());
		if (adjNode1 != null) {

			TileImg adjImg1 = this.boardMap.get(adjNode1);
			Location adjLoc1 = loc.clockwise().clockwise();
			if (!adjImg1.hasSettlement(s.getPlayer(), adjLoc1))
				addSettlement(adjNode1, adjLoc1, s);
		}

		Node<Direction, Tile> adjNode2 = n.go(loc.clockwiseDirection());
		if (adjNode2 != null) {

			TileImg adjImg2 = this.boardMap.get(adjNode2);
			Location adjLoc2 = loc.antiClockwise().antiClockwise();

			if (!adjImg2.hasSettlement(s.getPlayer(), adjLoc2))
				addSettlement(adjNode2, adjLoc2, s);
		}
	}

	/**
	 * Remove the build sites of town on the given location for a player
	 *
	 * @param n
	 * @param loc
	 * @param player
	 */
	private void removeTownBuildSite(Node<Direction, Tile> n, Location loc, Player player) {
		if (n != null) {
			TileImg img = this.boardMap.get(n);
			img.removeTownBuildSite(player, loc);

			Node<Direction, Tile> adjNode1 = n.go(loc.antiClockwiseDirection());
			if (adjNode1 != null) {
				TileImg adjImg1 = this.boardMap.get(adjNode1);
				adjImg1.removeTownBuildSite(player, loc.clockwise().clockwise());
			}

			Node<Direction, Tile> adjNode2 = n.go(loc.clockwiseDirection());
			if (adjNode2 != null) {
				TileImg adjImg2 = this.boardMap.get(adjNode2);
				adjImg2.removeTownBuildSite(player, loc.antiClockwise().antiClockwise());
			}

		}
	}

	/**
	 * Highlight build sites available for town
	 *
	 * @param player
	 */
	public void highlightAvailableTownSites(Player player) {
		try {
			game.stream().forEach(n -> {
				for (Location loc : Location.values()) {
					if (!n.getValue().hasSettlement(loc)) {
						this.boardMap.get(n).highlightAvailableTownBuildSite(loc, player);
					}
				}
			});
		}
		catch(Throwable t) {
			System.out.println("Not enough features implemented. This caused a");
			t.printStackTrace();
		}
	}

	/**
	 * Dehighlight build sites available for building
	 */
	public void dehighlightAvailableTownSites() {
		game.stream().forEach(n -> {
			for (Location loc : Location.values()) {
				this.boardMap.get(n).deHighlightAvailableTownBuildSite(loc);
			}
		});
	}

	/**
	 * Register a town for dragging
	 *
	 * @param source
	 * @param player
	 */
	public void registerDraggableTown(TownImg source, Player player) {
		if (game.stream() != null) {
			game.stream().forEach(n -> {
				if (this.boardMap.containsKey(n)) {
					this.boardMap.get(n).registerTownBuildSiteForDraggable(source, player);
					this.boardMap.get(n).addTownToPalette(player, source);
				}
			});
		}
	}

	/**
	 * Remove the build sites of town for a player
	 *
	 * @param player
	 */
	public void cleanTownBuildSitesForPlayer(Player player) {
		if (game.stream() != null)
			game.stream().map(n -> this.boardMap.get(n)).forEach(t -> {
				Arrays.asList(Location.values()).stream().filter(
						loc -> !((t.hasSettlement(player, loc)) || (t.playerHasRoad(player, loc.clockwiseDirection())
								|| t.playerHasRoad(player, loc.antiClockwiseDirection()))))
						.forEach(loc -> t.removeTownBuildSite(player, loc));
				;
			});
		;
	}

	// *****Road Stuff*****//
	/**
	 * Add a build site of road on the given direction for a player
	 *
	 * @param n
	 * @param dir
	 * @param player
	 */
	private void addRoadBuildSite(Node<Direction, Tile> n, Direction dir, Player player) {
		if (n != null) {
			this.boardMap.get(n).addRoadBuildSite(player, dir, n);

			Node<Direction, Tile> adjNode = n.go(dir);
			if (adjNode != null) {
				this.boardMap.get(adjNode).addRoadBuildSite(player, dir.inverse(), adjNode);
			}
		}
	}

	/**
	 * Add a road on the given direction of a tile
	 *
	 * @param n
	 * @param dir
	 * @param r
	 */
	public void addRoad(Node<Direction, Tile> n, Direction dir, Road r) {
		this.game.addRoad(n, r, dir);

		TileImg img = this.boardMap.get(n);
		img.addRoad(r, dir, n);
		addRoadBuildSite(n, dir.antiClockwise(), r.getPlayer());
		addRoadBuildSite(n, dir.clockwise(), r.getPlayer());

		Node<Direction, Tile> adjNode = n.go(dir);
		if (adjNode != null) {
			TileImg adjImg = this.boardMap.get(adjNode);
			adjImg.addRoad(r, dir.inverse(), adjNode);
			addRoadBuildSite(adjNode, dir.inverse().antiClockwise(), r.getPlayer());
			addRoadBuildSite(adjNode, dir.inverse().clockwise(), r.getPlayer());
		}

		this.boardMap.get(n).addTownBuildSite(r.getPlayer(), dir.antiClockwiseLocation());
		this.boardMap.get(n).addTownBuildSite(r.getPlayer(), dir.clockwiseLocation());
	}

	/**
	 * Highlight build sites available for road
	 *
	 * @param player
	 */
	public void highlightAvailableRoadSites(Player player) {
		game.stream().forEach(n -> {
			for (Direction dir : Direction.values()) {
				if (!n.getValue().hasRoad(dir)) {
					this.boardMap.get(n).highlightAvailableRoadBuildSite(dir, player);
				}

			}
		});
	}

	/**
	 * Dehighlight build sites available for road
	 */
	public void dehighlightAvailableRoadSites() {
		game.stream().forEach(n -> {
			for (Direction dir : Direction.values()) {
				this.boardMap.get(n).deHighlightAvailableRoadBuildSite(dir);
			}
		});
	}

	/**
	 * Register a road for dragging
	 *
	 * @param source
	 * @param player
	 */
	public void registerDraggableRoad(RoadImg source, Player player) {

		if (game.stream() != null) {
			game.stream().forEach(n -> {
				if (this.boardMap.containsKey(n)) {
					this.boardMap.get(n).addRoadToPalette(player, source);
				}
			});
		}
	}

	// ******During the game*****//
	/**
	 * Roll the double dices
	 *
	 * @return
	 */
	public Integer[] rollDice(Player player) {
		Integer roll1 = (int) (Math.ceil((Math.random() * 6)));
		Integer roll2 = (int) (Math.ceil((Math.random() * 6)));

		System.out.println("Player " + (player.getID()+1) + "'s turn.");

		Integer[] diceNum = new Integer[] { roll1, roll2 };

		this.game.distributeResources(roll1 + roll2);


		//  if (roll1 + roll2 == 7) {

	//	System.out.println(game.getPlayers().stream().forEach(p-> Arrays.asList(Resource.values()).stream()
	//			  .map(p::getResourceAmount)));

	//	  }


		return (diceNum);
	}

	/**
	 * Return the amount of settlement that a given player has on the board.
	 *
	 * @param player
	 * @return
	 */
	public int getSettlementAmount(Player player) {
		long amount = game.stream().map(n -> this.boardMap.get(n)).flatMap(
				tile -> Arrays.asList(Location.values()).stream().filter(loc -> tile.hasSettlement(player, loc)))
				.count();

		// 1 settlement would be added to 3 tiles
		return (int) amount / 3;

	}

	/**
	 * Return the amount of road that a given player has on the board.
	 *
	 * @param player
	 * @return
	 */
	public int getRoadAmount(Player player) {

		long amount = game.stream().map(n -> this.boardMap.get(n)).flatMap(
				tile -> Arrays.asList(Direction.values()).stream().filter(dir -> tile.playerHasRoad(player, dir)))
				.count();

		// 1 road would be added to 2 tiles
		return (int) amount / 2;

	}

}
