package tests;

//This program is copyright VUW.
//You are granted permission to use it to construct your answer to a SWEN221 assignment.
//You may not distribute it in any other way without permission.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import game.HexNode;
import graph.Graph;
import graph.Node;
import model.Direction;

public class Part1Tests {

	@Test
	void testadd() {
		Node<Direction, Integer> middle = new HexNode<Integer>();

		middle.add(Direction.NORTHWEST, new HexNode<Integer>());

		assertTrue(middle.hasNeighbor(Direction.NORTHWEST));
	}

	@Test
	void testConnect1() {

		Node<Direction, Integer> origin = new HexNode<Integer>();
		Node<Direction, Integer> adj = new HexNode<Integer>();

		origin.connect(Direction.WEST, adj);

		assertSame(origin, adj.go(Direction.EAST));
	}

	@Test
	void testConnect2() {

		Arrays.stream(Direction.values()).forEach(dir -> {
			Node<Direction, Integer> origin = new HexNode<Integer>();
			Node<Direction, Integer> adj1 = new HexNode<Integer>();
			Node<Direction, Integer> adj2 = new HexNode<Integer>();

			Boolean c1=origin.connect(dir, adj1);
			Boolean c2=origin.connect(dir.antiClockwise(), adj2);
			assertTrue(c1);
			assertTrue(c2);
			assertSame(origin, adj1.go(dir.inverse()));
			assertSame(origin, adj2.go(dir.antiClockwise().inverse()));
			assertSame(adj1, adj2.go(dir.clockwise()));
			assertSame(adj2, adj1.go(dir.antiClockwise().antiClockwise()));
		});
		;
	}

	@Test
	void testConnect3() {

		Node<Direction, Integer> origin = new HexNode<Integer>();
		Node<Direction, Integer> adj1 = new HexNode<Integer>();
		Node<Direction, Integer> adj2 = new HexNode<Integer>();

		origin.connect(Direction.WEST, adj1);
		origin.connect(Direction.SOUTHWEST, adj2);

		assertSame(adj1, adj2.go(Direction.NORTHWEST));
		assertSame(origin, adj1.go(Direction.EAST));
		assertSame(origin, adj2.go(Direction.NORTHEAST));
	}

	@Test
	void testFillNeighborhood() {
		Node<Direction, Integer> middle = new HexNode<Integer>();

		middle.fillNeighborhood();

		for (Direction dir : Direction.values()) {
			assertTrue(middle.hasNeighbor(dir));
			assertTrue(middle.isConnected(dir, middle.go(dir)));
		}
	}

	@Test
	void testToList() {
		Node<Direction, Integer> middle = new HexNode<Integer>();

		middle.generate(2);

		List<Node<Direction, Integer>> middleAsList = middle.toList();

		middle.go(Direction.NORTHWEST).setValue(1);
		middle.go(Direction.NORTHEAST).setValue(2);
		middle.go(Direction.WEST).setValue(3);
		middle.setValue(4);
		middle.go(Direction.EAST).setValue(5);
		middle.go(Direction.SOUTHWEST).setValue(6);
		middle.go(Direction.SOUTHEAST).setValue(7);

		assertEquals("[1, 2, 3, 4, 5, 6, 7]", middleAsList.toString());
		assertEquals(7, middleAsList.size());
	}

	@Test
	void testToString() {
		Node<Direction, Integer> testNode = new HexNode<Integer>();
		testNode.setValue(1);
		assertEquals("1", testNode.toString());

	}
	// stream()
	// Set number for a node and its neighbors, check the sum of their number
	@Test
	public void streamTest1() {
		Node<Direction, Integer> middle = new HexNode<>();
		middle.generate(2);
		middle.go(Direction.NORTHWEST).setValue(1);
		middle.go(Direction.NORTHEAST).setValue(2);
		middle.go(Direction.WEST).setValue(3);
		middle.setValue(4);
		middle.go(Direction.EAST).setValue(5);
		middle.go(Direction.SOUTHWEST).setValue(6);
		middle.go(Direction.SOUTHEAST).setValue(7);
		assertEquals(
			middle.stream().map(Node::getValue).reduce(0, (i, j) -> i + j),
			(Integer)28);
	}

	// stream()
	// Set number for a node and its neighbors, check the sum of their number (in
	// clockwise)
	@Test
	public void clockwiseStreamTest() {
		Node<Direction, Integer> middle = new HexNode<>();
		middle.generate(2);
		middle.go(Direction.NORTHWEST).setValue(1);
		middle.go(Direction.NORTHEAST).setValue(2);
		middle.go(Direction.WEST).setValue(3);
		middle.setValue(4);
		middle.go(Direction.EAST).setValue(5);
		middle.go(Direction.SOUTHWEST).setValue(6);
		middle.go(Direction.SOUTHEAST).setValue(7);
		assertEquals(
			middle.clockwiseStream().map(Node::getValue).reduce(0, (i, j) -> i + j),
		    (Integer)28);
	}
	@Test
	public void testadd2() {
		Node<Direction, Integer> middle = new HexNode<>();
		Node<Direction, Integer> adjNode = new HexNode<>();
		middle.add(Direction.NORTHWEST, adjNode);
		middle.add(Direction.NORTHEAST, new HexNode<Integer>());
		assertFalse(middle.go(Direction.NORTHEAST).isConnected(Direction.WEST, adjNode),
		"Add should not connect adjacent nodes.");
	}
	public static void checkC(Node<Direction, Integer>n1,Direction d, Node<Direction, Integer> n2) {
		Direction di=d.inverse();
		for(Direction dj:Direction.values()){
			assertSame(n1.isConnected(dj, n2),dj==d);
			assertSame(n2.isConnected(dj, n1),dj==di);
		}
	}
	// connect() & isConnected()
	// Connect nodes in clockwise order, check if all nodes are connected
	@Test
	public void testIsConnected() {
		HexNode<Integer> node1 = new HexNode<>();
		HexNode<Integer> node2 = new HexNode<>();
		HexNode<Integer> node3 = new HexNode<>();
		HexNode<Integer> node4 = new HexNode<>();
		HexNode<Integer> node5 = new HexNode<>();
		HexNode<Integer> node6 = new HexNode<>();
		HexNode<Integer> node7 = new HexNode<>();
		node1.connect(Direction.EAST, node2);
		node2.connect(Direction.SOUTHEAST, node3);
		node3.connect(Direction.SOUTHWEST, node4);
		node4.connect(Direction.WEST, node5);
		node5.connect(Direction.NORTHWEST, node6);
		node6.connect(Direction.EAST, node7);

		//Check that the specific connections we made are really there
		checkC(node1,Direction.EAST, node2);
		checkC(node2,Direction.SOUTHEAST, node3);
		checkC(node3,Direction.SOUTHWEST, node4);
		checkC(node4,Direction.WEST, node5);
		checkC(node5,Direction.NORTHWEST, node6);
		checkC(node6,Direction.EAST, node7);

		//Check 7, sitting in the center, is now connected to all others
		checkC(node7,Direction.NORTHWEST, node1);
		checkC(node7,Direction.NORTHEAST, node2);
		checkC(node7,Direction.EAST, node3);
		checkC(node7,Direction.SOUTHEAST, node4);
		checkC(node7,Direction.SOUTHWEST, node5);
		checkC(node7,Direction.WEST, node6);

		//Check 1 and 6 are also connected
		checkC(node1,Direction.SOUTHWEST, node6);
	}

	@Test
	public void testFillNeighborhood1() {
		for (Direction dir : Direction.values()) {
			HexNode<Integer> middle = new HexNode<>();
			HexNode<Integer> node1 = new HexNode<>();
			HexNode<Integer> node2 = new HexNode<>();
			HexNode<Integer> node3 = new HexNode<>();
			middle.connect(Direction.EAST, node1);
			middle.connect(Direction.SOUTHWEST, node2);
			middle.connect(Direction.NORTHWEST, node3);
			middle.fillNeighborhood();
			assertTrue(middle.hasNeighbor(dir),"middle node does not have neighbor to the " + dir);
			checkC(middle,dir, middle.go(dir));
			}
	}
	@Test
	public void streamTest() {
		Node<Direction, String> middle = new HexNode<>();
		middle.generate(3);
		AtomicInteger i = new AtomicInteger(1);
		middle.stream().forEach(n ->n.setValue(Integer.toString(i.getAndIncrement())));
		assertEquals(
				"1 2 3"
						+" 4 5 6 7"
						+" 8 9 10 11 12"
						+" 13 14 15 16"
						+" 17 18 19",
			middle.stream().map(Node::getValue).reduce("", (s1, s2) -> s1 + " " + s2).trim(),
			"graph stream mismatch");

		assertEquals(
				"1 2 3 7 12 16 19 18 17 13 8 4 5 6 11 15 14 9 10",
				middle.clockwiseStream().map(Node::getValue).reduce("", (s1, s2) -> s1 + " " + s2).trim(),
				"graph clockwiseStream mismatch");

		assertEquals(//Changing the starting node does not change the result
				"1 2 3 7 12 16 19 18 17 13 8 4 5 6 11 15 14 9 10",
				middle.go(Direction.EAST).clockwiseStream().map(Node::getValue).reduce("", (s1, s2) -> s1 + " " + s2).trim(),
				"graph clockwiseStream mismatch");

	}
	//generate a node with depth 5
	@Test
	public void generateTestDept5() {
		Node<Direction, String> middle = new HexNode<>();
		middle.generate(5);
		AtomicInteger i = new AtomicInteger(1);
		middle.stream().forEach(n ->
			n.setValue(Integer.toString(i.getAndIncrement())));
		assertEquals(
				"1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 "
						+ "21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 "
						+ "41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61",
			middle.stream().map(Node::getValue).reduce("", (s1, s2) -> s1 + " " + s2).trim(),
			"generate error: depth 5");
	}

	@Test
	void clockWiseStreamTestCustom() {
		Node<Direction, String> middle = new HexNode<>();
		middle.generate(2);
		middle.go(Direction.NORTHWEST).setValue("1");
		middle.go(Direction.NORTHEAST).setValue("2");
		middle.go(Direction.EAST).setValue("3");
		middle.go(Direction.SOUTHEAST).setValue("4");
		middle.go(Direction.SOUTHWEST).setValue("5");
		middle.go(Direction.WEST).setValue("6");
		middle.setValue("7");

		//middle.clockwiseStream().forEach(n -> System.out.println(n.getValue()));
	}

	@Test
	void testClockwiseStream1() {
		Graph<Direction, Integer> graph = new IntGraph(2);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
				            + "6   7   3   "
				   		      + "5   4", graph.toString());
	}

	@Test
	void testClockwiseStream2() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.EAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testClockwiseStream3() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.NORTHEAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testClockwiseStream4() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.SOUTHEAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testClockwiseStream5() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.NORTHWEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testClockwiseStream6() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.WEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testClockwiseStream7() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.SOUTHWEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}
	@Test
	void testStdStream1() {
		Graph<Direction, Integer> graph = new IntGraph(2);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4", graph.toString());
	}

	@Test
	void testStdStream2() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.EAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testStdStream3() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.NORTHEAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testStdStream4() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.SOUTHEAST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testStdStream5() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.NORTHWEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testStdStream6() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.WEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

	@Test
	void testStdStream7() {
		IntGraph graph = new IntGraph(2);
		graph.moveMiddle(Direction.SOUTHWEST);

		AtomicInteger i = new AtomicInteger(1);
		graph.clockwiseStream().forEach(n -> n.setValue(i.getAndIncrement()));

		assertEquals("1   2   "
		         + "6   7   3   "
				   + "5   4",
				   graph.toString());
	}

}
