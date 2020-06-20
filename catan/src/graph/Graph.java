// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package graph;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Generic graph interface used to perform actions on an entire graph, not just a single node.
 * @author Julian Mackay
 *
 * @param <K> The key type of each node in the graph
 * @param <V> The value type of each node in the graph
 */
public interface Graph<K, V> {

	/**
	 * Stream the nodes of the graph from left to right and top to bottom
	 * @return
	 */
	public Stream<Node<K, V>> stream();

	/**
	 * Stream the nodes of the graph in a clockwise fashion
	 * @return The stream of the nodes
	 */
	public Stream<Node<K, V>> clockwiseStream();
}
