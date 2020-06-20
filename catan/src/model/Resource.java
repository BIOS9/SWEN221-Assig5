// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Julian Mackay
 *
 *         Resources are either Wood, Brick, Stone, Wheat, Sheep or Desert
 *
 */
public enum Resource {
	WOOD, BRICK, STONE, WHEAT, SHEEP, DESERT;

	/**
	 * Generate a pool of 19 resources in a random order. The quantities of each
	 * resource correspond to the quantities in a standard game of Catan
	 *
	 * @return return List of resources. 4 of each Wood, Wheat and Sheep. 3 of each
	 *         Brick and Stone. 1 Desert.
	 */
	public static List<Resource> generateResourcePool() {
		List<Resource> pool = new ArrayList<Resource>();

		for (int i = 0; i < 4; i++) {
			pool.add(WOOD);
			pool.add(WHEAT);
			pool.add(SHEEP);
		}

		for (int i = 0; i < 3; i++) {
			pool.add(BRICK);
			pool.add(STONE);
		}

		pool.add(DESERT);

		Collections.shuffle(pool);

		return pool;
	}

	@Override
	public String toString() {
		switch (this) {
		case WOOD:
			return "WO";

		case BRICK:
			return "BR";

		case STONE:
			return "ST";

		case WHEAT:
			return "WH";

		case SHEEP:
			return "SH";

		default:
			return "DE";
		}
	}

}
