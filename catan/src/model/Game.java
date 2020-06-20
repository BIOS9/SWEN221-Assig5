// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

import java.util.List;

import graph.Graph;
import graph.Node;

public interface Game<V> extends Graph<Direction, V> {

	/**
	 * Distribute a pool of resources across the board from left to right and top to
	 * bottom.
	 *
	 * @param pool
	 *            the resources to be distributed
	 */
	public void setResources(List<Resource> pool);

	/**
	 * Get the resource of the tile with the specified id
	 *
	 * @param id
	 *            the id in question
	 * @return the resource
	 */
	public Resource getResource(Integer id);

	/**
	 * Distribute a pool of resource counters across a board in a clockwise manner,
	 * starting from the NORTHWEST most corner and spiraling inwards toward the
	 * centre.
	 *
	 * @param pool
	 *            the resources to be distributed
	 */
	public void setResourceCounters(List<ResourceCounter> pool);

	/**
	 * get a list of the players of the game
	 *
	 * @return
	 */
	public List<Player> getPlayers();

	/**
	 * distribute resources to the players who have settlements on tiles with
	 * counters matching the dice roll
	 *
	 * @param diceRoll
	 *            the dice roll, from 2 to 12
	 */
	public void distributeResources(Integer diceRoll);

	/**
	 * Add a road r to node n in the direction of dir
	 *
	 * @param n
	 *            the node
	 * @param r
	 *            the road
	 * @param dir
	 *            the direction
	 */
	public void addRoad(Node<Direction, Tile> n, Road r, Direction dir);

	/**
	 * Upgrade a settlement from town to city if it is a town, leave it alone
	 * otherwise
	 *
	 * @param s
	 *            the settlement
	 * @return a boolean indicating success or failure
	 */
	public Boolean upgradeSettlement(Settlement s);

	/**
	 * Build a string of the resource counters on the tiles
	 *
	 * @return
	 */
	public String toResourceCounterString();

	/**
	 * Build a string of the resources on the tiles
	 *
	 * @return
	 */
	public String toResourceString();

	/**
	 * Add a settlement to a location on a node
	 *
	 * @param n
	 *            the node
	 * @param loc
	 *            the location
	 * @param s
	 *            the settlement
	 */
	public void addSettlement(Node<Direction, Tile> n, Location loc, Settlement s);
}
