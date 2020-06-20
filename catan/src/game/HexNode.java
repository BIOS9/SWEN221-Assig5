// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package game;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import graph.Node;
import javafx.util.Pair;
import model.Direction;

/**
 *
 * A graph where each node has up to 6 edges.
 *
 * @author Julian Mackay
 *
 * @param <V>
 *            the value stored in each node
 */
public class HexNode<V> implements Node<Direction, V> {
	private final Map<Direction, Node<Direction, V>> neighbors=new LinkedHashMap<>();
	private V v;

	public HexNode(V v) {
		this.v = v;
	}

	public HexNode() {}

	@Override
	public V getValue() {
		return v;
	}

	@Override
	public Boolean setValue(V v) {
		if (this.v != null) {return false;}
		this.v = v;
		return true;
	}

	@Override
	public Node<Direction, V> go(Direction direction) {
		if (!this.neighbors.containsKey(direction)) {return null;}
		return this.neighbors.get(direction);
	}

	@Override
	public Boolean connect(Direction direction, Node<Direction, V> n) {
		if(n == null || n == this)
			return false;

		if(hasNeighbor(direction))
			return false;

		if(n.hasNeighbor(direction.inverse()))
			return false;

		if(isInGraph(n, new HashSet<>()))
		    return false;

		forceNeighbour(direction, n);

		n.traverseAndLinkNode(Position.Zero(), n, new HashSet<>()); // Recursively connect the new node to adjacent nodes

		return true;
	}

	@Override
	public Boolean isInGraph(Node<Direction, V> n, Set<Node<Direction, V>> visited) {
        if(!visited.add(this)) // Skip traversal of this node if already traversed
            return false;

        visited.add(this);

        Direction d = Direction.EAST;
        do {
            if(hasNeighbor(d)) {
                if(getNeighbor(d).isInGraph(n, visited))
                    return true;
            }

            d = d.clockwise();
        } while(d != Direction.EAST);

        return false;
    }

	/**
	 * Recursively traverses all of the connected nodes and ensures any node touching the origin has the origin linked
	 * @param originNode
	 * @param visited
	 */
	@Override
	public void traverseAndLinkNode(Position position, Node<Direction, V> originNode, Set<Node<Direction, V>> visited) {
		if(!visited.add(this)) // Skip traversal of this node if already traversed
			return;

		if(position.IsOriginAdjacent()) // Link nodes that are touching the origin
			forceNeighbour(position.GetOriginDirection(), originNode);

		// Loop over all directions that contain nodes to traverse the graph
		Direction d = Direction.EAST;
		do {

			if(hasNeighbor(d))
				getNeighbor(d).traverseAndLinkNode(position.Go(d), originNode, visited); // Recursively link neighbours

			d = d.clockwise();
		} while(d != Direction.EAST);
	}

	@Override
	public Boolean isConnected(Direction direction, Node<Direction, V> n) {
		Node<Direction, V> neighbor = neighbors.get(direction);

		if(neighbor == null)
			return false;

		return neighbor.equals(n);
	}

	@Override
	public Boolean add(Direction direction, Node<Direction, V> n) {
		if(neighbors.containsKey(direction) || n == null)
			return false;

		neighbors.put(direction, n);
		return true;
	}

	@Override
	public void forceNeighbour(Direction direction, Node<Direction, V> node) {
		if(node == null) {
			neighbors.remove(direction);
			return;
		}

		// Remove existing neighbor
		if(neighbors.containsKey(direction))
			neighbors.get(direction).forceNeighbour(direction.inverse(), null);

		// Remove neighbours neighbours
		if(node.hasNeighbor(direction.inverse())) {
			node.getNeighbor(direction.inverse()).forceNeighbour(direction, null);
			node.forceNeighbour(direction.inverse(), null);
		}

		// Connect the two nodes
		neighbors.put(direction, node);
		node.add(direction.inverse(), this);
	}

	@Override
	public Node<Direction, V> getNeighbor(Direction direction) {
		return neighbors.get(direction);
	}

	@Override
	public Boolean hasNeighbor(Direction dir) {
		return neighbors.containsKey(dir);
	}

	@Override
	public Node<Direction, V> fillNeighborhood() {
		Direction d = Direction.EAST;
		do {
			if(!neighbors.containsKey(d))
				connect(d, new HexNode<>());

			d = d.clockwise();
		} while(d != Direction.EAST);

		return this;
	}

	@Override
	public HexNode<V> generate(Integer depth) {

	    //Breadth first traversal of current layer ring to generate another
	    Queue<Pair<HexNode<V>, Integer>> toFill = new LinkedList<>();
	    toFill.offer(new Pair<>(this, 1));

	    while(!toFill.isEmpty()) {
            Pair<HexNode<V>, Integer> nodeToFill = toFill.poll();
            HexNode<V> node = nodeToFill.getKey();
            int nodeDepth = nodeToFill.getValue();

            // Iterate over all the neighbour positions and generate nodes
            Direction d = Direction.EAST;
            do {
                if (!node.hasNeighbor(d)) {
                    HexNode<V> newNode = new HexNode<>();
                    node.connect(d, newNode);

                    // Add the new neighbour to the traversal queue
                    if(nodeDepth < depth - 1)
                        toFill.offer(new Pair<>(newNode, nodeDepth + 1));
                }
                d = d.clockwise();
            } while (d != Direction.EAST);
        }

		return this;
	}

	@Override
	public List<Node<Direction, V>> toList() {
        SortedMap<Position, Node<Direction, V>> visited = new TreeMap<>();
        traverseAndSort(Position.Zero(), visited);
        return new ArrayList<>(visited.values());
	}

	@Override
	public void traverseAndSort(Position position, SortedMap<Position, Node<Direction, V>> visited) {
        if(visited.containsKey(position))
            return;

        visited.put(position, this);

        Direction d = Direction.EAST;
        do {

            if(neighbors.containsKey(d))
                neighbors.get(d).traverseAndSort(position.Go(d), visited);

            d = d.clockwise();
        } while(d != Direction.EAST);
    }

	@Override
	public Stream<Node<Direction, V>> stream() {
		return toList().stream();
	}

    /**
     * Produces list of nodes spiraling anticlockwise from the centre.
     * First priority is northWest most node.
     * @return
     */
	private List<Node<Direction, V>> clockwiseList() {
        List<Node<Direction, V>> list = new ArrayList<>(); // List of nodes in clockwise order
        Set<Node<Direction, V>> visited = new HashSet<>(); // Separate set for visited nodes, faster than checking if list contains.
        Node<Direction, V> currentNode;
        Direction currentDirection = Direction.EAST; // The direction to go in the graph till a missing node is found

        // Gets the northwest-most node in the graph
        Optional<Node<Direction, V>> nodeOptional = stream().filter(n ->
                        !n.hasNeighbor(Direction.WEST) &&
                        !n.hasNeighbor(Direction.NORTHWEST) &&
                        !n.hasNeighbor(Direction.NORTHEAST)).findFirst();

        if(!nodeOptional.isPresent())
            throw new RuntimeException("Cant find northwest-most node!");

        currentNode = nodeOptional.get();

	    visited.add(currentNode);
	    list.add(currentNode);

	    int rotations = 0;
	    while (true) {
            Node<Direction, V> nextNode = currentNode.getNeighbor(currentDirection);
	        if(nextNode == null || visited.contains(nextNode)) {
	            currentDirection = currentDirection.clockwise();
	            ++rotations;
	            if(rotations == 6)
	                break; // No neighbours
	            continue;
            }

	        rotations = 0;
	        currentNode = currentNode.getNeighbor(currentDirection);
            visited.add(currentNode);
	        list.add(currentNode);
        }
        return list;
    }

	@Override
	public Stream<Node<Direction, V>> clockwiseStream() {
	    return clockwiseList().stream();
	}

	@Override
	public String toString() {
		return v==null?"*"+this.hashCode():v.toString();
	}
	@Override
	public Boolean isValid() {
		assert this.collectAll().stream().allMatch(n->isValidOne(n));
		return true;
	}
	/**
	 * Very general algorithm to collect all the nodes of a graph
	 * @return
	 */
	private Set<Node<Direction,V>> collectAll(){
		Set<Node<Direction,V>> res=new LinkedHashSet<>();
		collectAll(this,res);
		return res;
	}
	private static<V> void collectAll(Node<Direction,V> n,Set<Node<Direction,V>> acc){
		if(n==null) {return;}
		if(acc.contains(n)) {return;}
		acc.add(n);
		Stream.of(Direction.values()).forEach(d->collectAll(n.go(d),acc));
	}

	public static<V> Boolean isValidOne(Node<Direction,V> n) {
		Predicate<Predicate<Direction>> ns=
			p->Stream.of(Direction.values())
				.filter(d->n.hasNeighbor(d))
				.allMatch(p);
		assert ns.test(d->n.go(d)!=n);
		assert ns.test(d->n.go(d).go(d.inverse())==n);
		assert ns.test(d->{
			Node<Direction, V> n1 = n.go(d);
			Node<Direction, V> n2 = n.go(d.clockwise());
			assert n1!=null;
			if(n2==null) {return true;}
			assert n1.go(d.inverse().antiClockwise())==n2:
				"surrunding pieces not well connected";
			return true;
		});
		assert ns.test(d->{
			Node<Direction, V> n1 = n.go(d);
			Node<Direction, V> n2 = n.go(d.antiClockwise());
			assert n1!=null;
			if(n2==null) {return true;}
			assert n1.go(d.inverse().clockwise())==n2;
			return true;
		});
		return true;
	}

}