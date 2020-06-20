// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a SWEN221 assignment.
// You may not distribute it in any other way without permission.
package model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Resource Numbers have alpha equivalents
 *
 * @author Julian Mackay
 *
 */
public enum ResourceCounter {

	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, NONE;

	/**
	 * Getter for the number
	 *
	 * @return
	 */
	public Integer getNumber() {
		switch (this) {
		case A:
			return 5;

		case B:
			return 2;

		case C:
			return 6;

		case D:
			return 3;

		case E:
			return 8;

		case F:
			return 10;

		case G:
			return 9;

		case H:
			return 12;

		case I:
			return 11;

		case J:
			return 4;

		case K:
			return 8;

		case L:
			return 10;

		case M:
			return 9;

		case N:
			return 4;

		case O:
			return 5;

		case P:
			return 6;

		case Q:
			return 3;

		case R:
			return 11;

		default:
			return 0;
		}

	}

	public static ResourceCounter getCounterFromChar(Character c) {
		switch (c) {
		case 'A':
			return A;

		case 'B':
			return B;

		case 'C':
			return C;

		case 'D':
			return D;

		case 'E':
			return E;

		case 'F':
			return F;

		case 'G':
			return G;

		case 'H':
			return H;

		case 'I':
			return I;

		case 'J':
			return J;

		case 'K':
			return K;

		case 'L':
			return L;

		case 'M':
			return M;

		case 'N':
			return N;

		case 'O':
			return O;

		case 'P':
			return P;

		case 'Q':
			return Q;

		case 'R':
			return R;

		default:
			return NONE;
		}

	}

	/**
	 * Generate a randomly organised pool of resource numbers
	 *
	 * @return
	 */
	public static List<ResourceCounter> generateResourceCounters() {
		List<ResourceCounter> pool =

				IntStream.range(0, 18).mapToObj(n -> ResourceCounter.getCounterFromChar(Character.toChars(n + 65)[0]))
						.collect(Collectors.toList());

		return pool;
	}
}
