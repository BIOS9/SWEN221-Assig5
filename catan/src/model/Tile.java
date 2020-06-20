// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

import java.util.List;
import java.util.Set;

/**
 * The main Tile interface
 *
 * @author Julian Mackay
 *
 */
public interface Tile {

	/**
	 * Distributes the resources to all the adjacent settlements
	 */
	void distributeResources();

	/**
	 * Getter for the tile id
	 *
	 * @return Integer id
	 */
	public Integer getID();

	/**
	 * Setter for the resource
	 *
	 * @param r
	 *            the resource
	 * @return Boolean indicating success or failure
	 */
	public Boolean setResource(Resource r);

	/**
	 * Getter for the resource
	 *
	 * @return
	 */
	public Resource getResource();

	/**
	 * Getter for the integer number of the resource counter on this tile
	 *
	 * @return
	 */
	public Integer getResourceNumber();

	/**
	 * Setter for the resource counter
	 *
	 * @param resourceCounter
	 * @return Boolean indicating success or failure
	 */
	public Boolean setResourceCounter(ResourceCounter resourceCounter);

	/**
	 * Checks if a settlement is present at the location loc
	 *
	 * @param loc
	 *            the location to check
	 * @return
	 */
	public Boolean hasSettlement(Location loc);

	/**
	 * Add a settlement to the specified location. Since loc is shared with up to
	 * two other tiles, they need to have this settlement added to the appropriate
	 * locations.
	 *
	 * @param s
	 *            the settlement
	 * @param loc
	 *            the location
	 * @return Boolean indicating success or failure
	 */
	public Boolean addSettlement(Settlement s, Location loc);

	/**
	 * Add a road in the direction specified. Since roads are shared with up to 1
	 * other tile, they need to have this road added to the appropriate direction
	 * too.
	 *
	 * @param r
	 *            the road to be added
	 * @param dir
	 *            the direction of the road
	 * @return
	 */
	public Boolean addRoad(Road r, Direction dir);

	/**
	 * Checks if a road is present in the specified direction
	 *
	 * @param dir
	 *            the direction in question
	 * @return Boolean indicating success or failure
	 */
	public Boolean hasRoad(Direction dir);

	/**
	 * Returns a list of the settlements present on this tile
	 *
	 * @return the list of settlements
	 */
	public List<Settlement> getSettlements();

}
