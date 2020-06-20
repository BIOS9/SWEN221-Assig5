// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

public enum Location {
	NORTH,
	NORTHWEST,
	SOUTHWEST,
	SOUTH,
	SOUTHEAST,
	NORTHEAST;

	public Location antiClockwise() {
		switch (this) {

		case NORTH : return NORTHWEST;

		case NORTHWEST : return SOUTHWEST;

		case SOUTHWEST : return SOUTH;

		case SOUTH : return SOUTHEAST;

		case SOUTHEAST : return NORTHEAST;

		default : return NORTH;

		}
	}

	public Location clockwise() {
		switch (this) {

		case NORTH : return NORTHEAST;

		case NORTHEAST : return SOUTHEAST;

		case SOUTHEAST : return SOUTH;

		case SOUTH : return SOUTHWEST;

		case SOUTHWEST : return NORTHWEST;

		default : return NORTH;

		}
	}

	public Direction clockwiseDirection() {
		switch(this) {

		case NORTH : return Direction.NORTHEAST;

		case NORTHEAST : return Direction.EAST;

		case SOUTHEAST : return Direction.SOUTHEAST;

		case SOUTH : return Direction.SOUTHWEST;

		case SOUTHWEST : return Direction.WEST;

		default : return Direction.NORTHWEST;
		}
	}

	public Direction antiClockwiseDirection() {
		switch(this) {

		case NORTH : return Direction.NORTHWEST;

		case NORTHWEST : return Direction.WEST;

		case SOUTHWEST : return Direction.SOUTHWEST;

		case SOUTH : return Direction.SOUTHEAST;

		case SOUTHEAST : return Direction.EAST;

		default : return Direction.NORTHEAST;
		}
	}
}
