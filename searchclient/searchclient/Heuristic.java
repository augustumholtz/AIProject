package searchclient;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

import searchclient.NotImplementedException;

public abstract class Heuristic implements Comparator<Node> {
	public ArrayList<Goal> goals;
	public Distance[][] distances;
	public boolean isSoko3;

	public Heuristic(Node initialState) {
		goals = initialState.goalList;
		distances = new Distance[initialState.MAX_ROW][initialState.MAX_COL];

		int numBox = 0;

		// calculate Manhattan distance for each cell in the level
		// and check if level is soko3
		for (int r = 0; r < initialState.MAX_ROW; ++r) {
			for (int c = 0; c < initialState.MAX_COL; ++c) {
				distances[r][c] = new Distance(r, c);

				// for checking if soko3
				if (initialState.boxes[r][c] > 0)
					numBox++;
			}
		}

		// if numBox is equal to the number of rows minus two borders then it is soko3
		if (numBox == initialState.MAX_ROW-2)
			isSoko3 = true;
	}

	/*
	Store manhattan distance of a cell to all goals
	 */
	private class Distance {
		//public static ArrayList<Goal> goalList = new ArrayList<Goal>();
		int row, col;
		//private HashMap<Character, Integer> manhatDist = new HashMap<Character, Integer>();
		private HashMap<Goal, Integer> manhatDist = new HashMap<Goal, Integer>();
		public Distance(int row, int col) {
			for (Goal goal : goals) {
				int dist = Math.abs(goal.getRow()-row) + Math.abs(goal.getCol()-col);
				//manhatDist.put(goal.getLetter(),dist);
				manhatDist.put(goal,dist);
			}
		}
		public int getManhatDistance(Goal goal) {
			return manhatDist.get(goal);
		}
	}

	public int h(Node n) {
		int dist = 0;
		boolean boxInWrongGoal = false;

		for (int row  = 0; row < Node.MAX_ROW; row++) {
			for (int col = 0; col < Node.MAX_COL; col++) {
				char b = Character.toLowerCase(n.boxes[row][col]);
			    if (b > 0) {
			        // specifically to solve SASoko03_48.lvl
					// check if this box is taking the position of the box designated (subjectively) to this goal
			    	if (isSoko3 && n.goals[row][col] == b) {
						// by checking each col of the row, excluding the goal, to see if there is a box
						// of the same letter is already on that row
						for (int i = 0; i < col; ++i) {
							if (Character.toLowerCase(n.boxes[row][i]) == n.goals[row][col]) {
								boxInWrongGoal = true;
							}
						}
					}

					// get the distance between the box and the nearest goal (defined by manhattan distance)
                    int minDistance = 100000000;
                    for (Goal goal : goals) {
						if (goal.getLetter() == Character.toLowerCase(b)) {
							int d = distances[row][col].getManhatDistance(goal);
							if (minDistance > d) {
								minDistance = d;
							}
						}
					}
					//dist += (minDistance + cellWeight[row][col]);
					dist += (minDistance);

                    // for SASoko03
                    // if box is in wrong goal,
					// add an arbitrary extra cost to this node
                    if (boxInWrongGoal) {
                    	dist += 200;
					}
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
