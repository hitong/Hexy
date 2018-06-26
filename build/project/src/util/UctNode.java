package util;

import java.util.ArrayList;

public class UctNode {
	int visit = 0;
	int win = 0;
	ArrayList<UctNode> child = new ArrayList<>();
	Point bestMove;
	Point lastMove;

	public UctNode() {
	}

	private UctNode(Point p) {
		lastMove = p;
	}

	public void getAllChildren(int[][] table) {
		// int[] x = this.getLmt(lastMove.x);
		// int[] y = this.getLmt(lastMove.y);
		// for (int i = x[0]; i <= x[1]; i++) {
		// for (int j = y[0]; j <= y[1]; j++) {
		// if (table[i][j] == 0) {
		// UctNode tmp = new UctNode(new Point(i, j));
		// tmp.lastMove = new Point(i, j);
		// child.add(tmp);
		// }
		// }
		// }
		// if(child.size() == 0) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				if (table[i][j] == 0) {
					child.add(new UctNode(new Point(i, j)));
				}
			}
		}
	}

	public int[] getLmt(int t) {
		int[] tmp = new int[2];
		if (t == 0 || t == 1) {
			tmp[0] = 0;
			tmp[1] = 4;
		} else if (t == 10 || t == 9) {
			tmp[0] = 6;
			tmp[1] = 10;
		} else {
			tmp[0] = t - 2;
			tmp[1] = t + 2;
		}
		return tmp;
	}

	public UctNode bestSelect(UctNode root, boolean now, boolean boot) {
		if (child.isEmpty()) {
			return null;
		} else {
			double UCT = 0;
			double winrate = 0;
			double bestUct = Integer.MIN_VALUE;
			double UCTvalue;
			UctNode tmp = null;
			for (int i = 0; i < child.size(); i++) {
				if (child.get(i).visit == 0) {
					UCTvalue = 10000;
				} else {
					winrate =( child.get(i).win * 1.0) / (child.get(i).visit * 1.0);
					UCT = 1 * Math.sqrt(Math.log(root.visit) / (5 * child.get(i).visit));
					if (now != boot) {
						UCTvalue = winrate + UCT;       
					} else {
						UCTvalue = 1 - winrate + UCT;
					}
//					
//					if(flag == 1){
//						System.out.println(UCTvalue);
//					}
				}
				if (UCTvalue > bestUct) {
					bestUct = UCTvalue;
					tmp = child.get(i);
				}
			}
//			System.out.println();
//			System.out.println();
			return tmp;
		}
	}
}
