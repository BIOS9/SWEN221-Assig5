// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

/**
 * @author Julian Mackay Six point compass directions
 */
public enum Direction {
	NORTHWEST, WEST, SOUTHWEST, SOUTHEAST, EAST, NORTHEAST;

	/**
	 * Inverse direction
	 *
	 * @return
	 */
	public Direction inverse() {

		switch (this) {
		case NORTHWEST:
			return SOUTHEAST;

		case WEST:
			return EAST;

		case SOUTHWEST:
			return NORTHEAST;

		case SOUTHEAST:
			return NORTHWEST;

		case EAST:
			return WEST;

		default:
			return SOUTHWEST;
		}
	}

	/**
	 * The next direction in a clockwise manner
	 *
	 * @return
	 */
	public Direction clockwise() {

		switch (this) {
		case NORTHEAST:
			return EAST;

		case EAST:
			return SOUTHEAST;

		case SOUTHEAST:
			return SOUTHWEST;

		case SOUTHWEST:
			return WEST;

		case WEST:
			return NORTHWEST;

		default:
			return NORTHEAST;
		}
	}

	/**
	 * The next direction in an anticlockwise manner
	 *
	 * @return
	 */
	public Direction antiClockwise() {

		switch (this) {
		case NORTHWEST:
			return WEST;

		case WEST:
			return SOUTHWEST;

		case SOUTHWEST:
			return SOUTHEAST;

		case SOUTHEAST:
			return EAST;

		case EAST:
			return NORTHEAST;

		default:
			return NORTHWEST;
		}
	}

	/**
	 * Given a starting direction from a center piece, return the relative position
	 * to the piece in that direction of the next (clockwise) co-ordinate.
	 *
	 * @param direction:
	 *            the starting direction
	 * @return the next direction from the center relative to the starting direction
	 */
	public static Direction relativeClockWisePosition(Direction direction) {
		switch (direction) {
		case NORTHEAST:
			return SOUTHEAST;

		case EAST:
			return SOUTHWEST;

		case SOUTHEAST:
			return WEST;

		case SOUTHWEST:
			return NORTHWEST;

		case WEST:
			return NORTHEAST;

		default:
			return EAST;
		}
	}

	/**
	 * Given a starting direction from a center piece, return the relative position
	 * to the piece in that direction of the previous (anti-clockwise) co-ordinate.
	 *
	 * @param direction:
	 *            the starting direction
	 * @return the previous direction from the center relative to the starting
	 *         direction
	 */
	public static Direction relativeAntiClockWisePosition(Direction direction) {
		switch (direction) {
		case NORTHWEST:
			return SOUTHWEST;

		case WEST:
			return SOUTHEAST;

		case SOUTHWEST:
			return EAST;

		case SOUTHEAST:
			return NORTHEAST;

		case EAST:
			return NORTHWEST;

		default:
			return WEST;
		}
	}

	public Location antiClockwiseLocation() {
		switch (this) {
		case NORTHWEST:
			return Location.NORTHWEST;

		case WEST:
			return Location.SOUTHWEST;

		case SOUTHWEST:
			return Location.SOUTH;

		case SOUTHEAST:
			return Location.SOUTHEAST;

		case EAST:
			return Location.NORTHEAST;

		default:
			return Location.NORTH;
		}

	}

	public Location clockwiseLocation() {
		switch (this) {
		case NORTHWEST:
			return Location.NORTH;

		case WEST:
			return Location.NORTHWEST;

		case SOUTHWEST:
			return Location.SOUTHWEST;

		case SOUTHEAST:
			return Location.SOUTH;

		case EAST:
			return Location.SOUTHEAST;

		default:
			return Location.NORTHEAST;
		}

	}
}
