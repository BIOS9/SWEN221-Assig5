// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package game;

import java.util.*;

import graph.Node;
import model.*;

/**
 * The main Tile class
 *
 * @author Julian Mackay
 *
 */
public class CatanTile implements Tile{

	private final Integer id;
	private final Map<Location, Settlement> settlements;
	private final Map<Direction, Road> roads;
	private final Node<Direction, Tile> currentNode;
	private Resource resource;
	private ResourceCounter resourceCounter;

	public CatanTile(Integer id, Node<Direction, Tile> currentNode) {
		this.id = id;
		this.settlements = new LinkedHashMap<>();
		this.roads = new LinkedHashMap<>();
		this.currentNode = currentNode;
	}

	@Override
	public Integer getID() {
		return this.id;
	}

	@Override
	public Boolean setResource(Resource r) {
		this.resource = r;
		return true;
	}

	@Override
	public Resource getResource() {
		return this.resource;
	}

	@Override
	public Integer getResourceNumber() {
		return resourceCounter.getNumber();
	}

	@Override
	public Boolean setResourceCounter(ResourceCounter resourceCounter) {
		this.resourceCounter = resourceCounter;
		return true;
	}

	@Override
	public Boolean hasSettlement(Location loc) {
		return settlements.containsKey(loc);
	}

	@Override
	public Boolean addSettlement(Settlement s, Location loc) {
		if(hasSettlement(loc))
			return false;
		settlements.put(loc, s);
		if(currentNode.hasNeighbor(loc.clockwiseDirection())) {
			Tile neighbourTile = currentNode.getNeighbor(loc.clockwiseDirection()).getValue();
			neighbourTile.addSettlement(s, loc.antiClockwise().antiClockwise());
		}

		if(currentNode.hasNeighbor(loc.antiClockwiseDirection())) {
			Tile neighbourTile = currentNode.getNeighbor(loc.antiClockwiseDirection()).getValue();
			neighbourTile.addSettlement(s, loc.clockwise().clockwise());
		}

		return true;
	}

	@Override
	public Boolean hasRoad(Direction dir) {
		//TODO: Replace this method
		throw new Error("implement CatanTile.hasRoad(Direction)");
	}

	@Override
	public Boolean addRoad(Road r, Direction dir) {
		//TODO: Replace this method
		throw new Error("implement CatanTile.addRoad(Road,Direction)");
	}

	@Override
	public List<Settlement> getSettlements(){
		return new ArrayList<>(settlements.values());
	}

	@Override
	public String toString() {
		return this.id.toString();
	}

	@Override
	public void distributeResources() {
		Location l = Location.NORTH;
		do {
			if(hasSettlement(l)) {
				Settlement s = settlements.get(l);
				s.getPlayer().addResource(resource, s.getType() == SettlementType.TOWN ? 1 : 2);
			}
			l = l.clockwise();
		} while (l != Location.NORTH);
	}

}