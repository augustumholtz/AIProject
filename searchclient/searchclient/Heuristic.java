package searchclient;

import java.util.Comparator;

import searchclient.NotImplementedException;

public abstract class Heuristic implements Comparator<Node> {
	public Heuristic(Node initialState) {
		// Here's a chance to pre-process the static parts of the level.
	}

	/*
	Heuristic: total Manhattan distances of all boxes
	 */
	public int h(Node n) {
		int dist = 0;
		for (int row  = 0; row < Node.MAX_ROW; row++) {
			for (int col = 0; col < Node.MAX_COL; col++) {
				char b = n.boxes[row][col];
			    if (b > 0) {
			    	// find the row and column indexes of the goal corresponding to the box
			    	int[] goalIndex = correspondGoal(b);
			    	// add Manhattan distance of the box to the total distance
					dist += (Math.abs(goalIndex[0]-row) + Math.abs(goalIndex[1]-col));
				}
			}
		}
		return dist;
	}

	protected int[] correspondGoal(char box) {
		int[] goalIndex = new int[2];
		char boxChar = Character.toLowerCase(box);
		for (int r = 0; r < Node.MAX_ROW; r++) {
			for (int c = 0; c < Node.MAX_COL; c++) {
				char g = Node.goals[r][c];
				if (g > 0 && boxChar == g) {
					goalIndex[0] = r;
					goalIndex[1] = c;
					break;
				}
			}
		}
		return goalIndex;
	}

	public abstract int f(Node n);

	@Override
	public int compare(Node n1, Node n2) {
		return this.f(n1) - this.f(n2);
	}

	public static class AStar extends Heuristic {
		public AStar(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return n.g() + this.h(n);
		}

		@Override
		public String toString() {
			return "A* evaluation";
		}
	}

	public static class WeightedAStar extends Heuristic {
		private int W;

		public WeightedAStar(Node initialState, int W) {
			super(initialState);
			this.W = W;
		}

		@Override
		public int f(Node n) {
			return n.g() + this.W * this.h(n);
		}

		@Override
		public String toString() {
			return String.format("WA*(%d) evaluation", this.W);
		}
	}

	public static class Greedy extends Heuristic {
		public Greedy(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return this.h(n);
		}

		@Override
		public String toString() {
			return "Greedy evaluation";
		}
	}
}
