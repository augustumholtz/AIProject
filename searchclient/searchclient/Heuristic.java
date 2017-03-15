package searchclient;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

import searchclient.NotImplementedException;

public abstract class Heuristic implements Comparator<Node> {
	public Distance[][] distances;
	public Heuristic(Node initialState) {
		distances = new Distance[initialState.MAX_ROW][initialState.MAX_COL];
        // get list of goals for Distance
        for (int r = 0; r < initialState.MAX_ROW; ++r) {
        	for (int c = 0; c < initialState.MAX_COL; ++c) {
        		if (initialState.goals[r][c] > 0) {
        			Goal goal = new Goal(initialState.goals[r][c], r, c);
        			Distance.goalList.add(goal);
				}
			}
		}

		// calculate Manhattan distance for each cell in the level
		for (int r = 0; r < initialState.MAX_ROW; ++r) {
        	for (int c = 0; c < initialState.MAX_COL; ++c) {
        		distances[r][c] = new Distance(r,c);
			}
		}
	}
	private static class Distance {
		public static ArrayList<Goal> goalList = new ArrayList<Goal>();
		int row, col;
		private HashMap<Character, Integer> manhatDist = new HashMap<Character, Integer>();
		public Distance(int row, int col) {
			for (Goal goal : goalList) {
				int dist = Math.abs(goal.getRow()-row) + Math.abs(goal.getCol()-col);
				manhatDist.put(goal.getLetter(),dist);
			}
		}

		public int getManhatDistance(char letter) {
			return manhatDist.get(letter);
		}
	}
	private class Goal {
		char letter;
		int row, col;
		public Goal(char l, int r, int c) {
			letter = l;
			row = r;
			col = c;
		}

		public char getLetter() {
			return letter;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
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
			        dist += distances[row][col].getManhatDistance(Character.toLowerCase(b));
				}
			}
		}
		return dist;
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
