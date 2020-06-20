// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package graph;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Stream;

import javafx.geometry.Pos;
import model.Direction;

import javax.xml.bind.NotIdentifiableEvent;

/**
 * @author Julian Mackay
 *
 * @param <K> The edge key type.
 * @param <V> The value type
 */
/**
 * @author Julian Mackay
 *
 * @param <K>
 * @param <V>
 */
public interface Node<K extends Object, V extends Object> {

	class Position implements Comparable<Position> {
		private final int x, y;

		private Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Returns a new origin zero position
		 * @return
		 */
		public static Position Zero() {
			return new Position(0, 0);
		}

		public Position GoNorthEast() {
			return new Position(x + 1, y + 1);
		}

		public Position GoSouthWest() {
			return new Position(x - 1, y - 1);
		}

		public Position GoEast() {
			return new Position(x + 2, y);
		}

		public Position GoWest() {
			return new Position(x - 2, y);
		}

		public Position GoSouthEast() {
			return new Position(x + 1, y - 1);
		}

		public Position GoNorthWest() {
			return new Position(x - 1, y + 1);
		}

		/**
		 * Moves the position in a specified direction
		 * @param direction
		 * @return
		 */
		public Position Go(Direction direction) {
			switch (direction) {
				case SOUTHEAST:
					return GoSouthEast();
				case SOUTHWEST:
					return GoSouthWest();
				case EAST:
					return GoEast();
				case WEST:
					return GoWest();
				case NORTHEAST:
					return GoNorthEast();
				default:
					return GoNorthWest();
			}
		}

		/**
		 * Checks if any of the adjacent nodes are the origin node
		 * @return
		 */
		public boolean IsOriginAdjacent() {
			return GetOriginDirection() != null;
		}

		/**
		 * Gets the direction of the adjacent origin from this pos
		 * @return
		 */
		public Direction GetOriginDirection() {
			if(x == 1) {
				if (y == 1)
					return Direction.SOUTHWEST;
				if (y == -1)
					return Direction.NORTHWEST;
			}

			if(x == -1) {
				if (y == 1)
					return Direction.SOUTHEAST;
				if (y == -1)
					return Direction.NORTHEAST;
			}

			if(x == 2 && y == 0)
				return Direction.WEST;

			if(x == -2 && y == 0)
				return Direction.EAST;

			return null;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == null) return false;

			if(obj.getClass() != this.getClass()) return false;

			Position pos = (Position)obj;

			return pos.x == x && pos.y == y;
		}

		@Override
		public int hashCode() {
			int prime = 4139;
			int result = 1;

			result = prime * result + x;
			result = prime * result + y;

			return result;
		}

		@Override
		public String toString() {
			return String.format("( %d, %d )", x, y);
		}

		/**
		 * Compares positions, order is based on row first then column,
		 * expected to sort from top left to bottom right, row by row
		 * @param o
		 * @return
		 */
		@Override
		public int compareTo(Position o) {
			if(o == null)
				return 0;

			if(y == o.y) {
				if(x == o.x)
					return 0;
				else
					return x - o.x;
			} else
				return o.y - y;
		}
	}

	/**
	 * Traverse the graph in the direction specified.
	 *
	 * @param direction
	 * @return the node in the specified direction
	 */
	public Node<K, V> go(K direction);

	/**
	 * Connect this node to a node in the specified direction.
	 * This method can assume the nodes are on a valid graph,
	 * and must guarantee that the result is still a valid graph,
	 * as detailed in the assignment handout.
	 *
	 * @param direction: the direction this connects to n
	 * @param n: the connected node
	 * @return A boolean indicating success of the connection;
	 *   in case the connection is not successful, the nodes should not be mutated.
	 *   Connection failure can happen, for example, if a node is already connected to
	 *   a node in the given direction.
	 */
	public Boolean connect(K direction, Node<K, V> n);

	/**
	 * Checks that the specified node is connected to this node in the direction.
	 * The graph is assumed to be valid, and this method can not modify the nodes.
	 *
	 * @param direction
	 * @param n
	 * @return Boolean indicating connectivity
	 */
	public Boolean isConnected(K direction, Node<K, V> n);

	/**
	 * Generates a list from the graph, from left to right, top to bottom
	 * This method can assumes that the graph is valid.
	 * @return
	 */
	public List<Node<K, V>> toList();

	/**
	 * Adds a node in the given direction. This is unidirectional, i.e. this does
	 * not add this node as a neighbor of n in the opposite direction.
	 * This method do *not* require the graph to be valid, and may
	 * make a valid graph invalid, or vice versa make an invalid graph valid.
	 *
	 * @param direction
	 * @param n
	 * @return Boolean indicating success of the addition. Addition fails if there
	 *   is already a connected node in the given direction.
	 *   If the addition fails, the nodes are not modified.
	 */
	public Boolean add(K direction, Node<K, V> n);

	/**
	 * Sets the node value
	 *
	 * @param v
	 * @return Boolean indicating success of the setter
	 */
	public Boolean setValue(V v);

	/**
	 * Generate a connected graph of specified depth. Using this node as the centre
	 * generate constructs a connected graph surrounding it of the specified depth,
	 * constructing new nodes where appropriate. The depth is an integer measuring
	 * the number of nodes from this node (the middle) to the outer edge. This is
	 * inclusive of this node. That is a single node has a depth of 1, a node
	 * entirely surrounded by other nodes has a depth of 2. Adding another layer to
	 * that gives us a layer of depth of 3, etc.
	 *
	 *
	 * @param depth
	 * @return this node
	 */
	public Node<K, V> generate(Integer depth);

	/**
	 * Checks if this node has a neighbor in the direction dir
	 *
	 * @param dir
	 * @return Boolean indicating neighborhood
	 */
	public Boolean hasNeighbor(K dir);

	/**
	 * Getter for value
	 *
	 * @return
	 */
	public V getValue();

	/**
	 * Surrounds the current node with nodes. This creates new nodes where
	 * appropriate and connects them together.
	 *
	 * @return this node
	 */
	public Node<K, V> fillNeighborhood();

	/**
	 * Returns a stream that starts from the north west most corner and streams row
	 * by row, west to east
	 *
	 * @return
	 */
	public Stream<Node<K, V>> stream();

	/**
	 * Returns a stream starting from the north west most node, spiraling in toward
	 * the centre.
	 *
	 * @return
	 */
	public Stream<Node<K, V>> clockwiseStream();

	/**
	 * Checks if the node belongs to a valid graph.
	 * This method is designed to be used in assertions, so it either throw an assertion
	 * error or returns true.
	 *
	 * @return true, or throw AssertionError
	 */
	public Boolean isValid();

	/**
	 * Traverses all nodes and connects any adjacent nodes that aren't connected to the specified origin node.
	 * @param pos
	 * @param originNode
	 * @param visited
	 */
	void traverseAndLinkNode(Position pos, Node<Direction, V> originNode, Set<Node<Direction, V>> visited);

	/**
	 * Forces a connection to a neighbour regardless of existing connections
	 * @param direction
	 * @param node
	 */
	void forceNeighbour(Direction direction, Node<Direction, V> node);

	/**
	 * Gets the neighbour at specified direction
	 * @param direction
	 * @return Node object or null if neighbour does not exist
	 */
	Node<Direction, V> getNeighbor(Direction direction);

	/**
	 * Traverses all connected nodes and sorts them by 2d graph position from top left to bottom right
	 * @param position
	 * @param visited
	 */
	void traverseAndSort(Position position, SortedMap<Position, Node<Direction, V>> visited);

	/**
	 * Recursively checks if node n is inside the graph of connected nodes
	 * @param n Node to check if exists
	 * @param visited Set of nodes that have already been visited
	 * @return
	 */
	Boolean isInGraph(Node<Direction, V> n, Set<Node<Direction, V>> visited);

}
