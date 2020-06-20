package tests;

import java.util.function.Function;
import java.util.stream.Stream;

import game.HexNode;
import graph.Graph;
import graph.Node;
import model.Direction;

class IntGraph implements Graph<Direction, Integer>{

	private Node<Direction, Integer> middle;

	public IntGraph(Integer depth) {
		this.middle = new HexNode<>();
		this.middle.generate(depth);
	}

	public IntGraph moveMiddle(Direction dir) {
		middle = middle.go(dir);
		return this;
	}

	@Override
	public Stream<Node<Direction, Integer>> stream() {
		return this.middle.stream();
	}

	@Override
	public Stream<Node<Direction, Integer>> clockwiseStream() {
		return this.middle.clockwiseStream();
	}

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer("");
		stream().map(Node::getValue).map(Object::toString).forEach(s -> res.append("  " + pad(s)));

		return res.toString().trim();
	}

	private String pad(String s) {
		if (s.length() < 2) {
			return pad(s + " ");
		} else {
			return s;
		}
	}
}