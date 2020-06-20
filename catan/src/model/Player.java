// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Player class, handling points and settlements
 *
 * @author Julian Mackay
 *
 */
public class Player {
	private Integer id;
	private Integer points;
	private final Map<Resource, Integer>resourcePool;

	public Player(int id) {
		this.id=id;
		this.points = 0;
		this.resourcePool = new HashMap<Resource, Integer>();
		this.resourcePool.put(Resource.BRICK, 0);
		this.resourcePool.put(Resource.WOOD, 0);
		this.resourcePool.put(Resource.SHEEP, 0);
		this.resourcePool.put(Resource.STONE, 0);
		this.resourcePool.put(Resource.WHEAT, 0);
	}


	public Integer getID() {
		return this.id;
	}

	//If the resource is not a desert, then add it to the pool, and plus 1 on the quantity
	public void addResource(Resource r, Integer n) {
		if(!r.equals(Resource.DESERT)) {
			this.resourcePool.put(r, this.resourcePool.get(r) + n);
		}
	}


	public Integer getResourceAmount(Resource r) {
		if(!r.equals(Resource.DESERT))
			return this.resourcePool.get(r);
		else
			return null;
	}

	protected void score(Integer i) {
		this.points += i;
	}

}
