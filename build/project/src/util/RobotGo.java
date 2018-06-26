package util;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import view.ChessBorderController;

/**
 * 
 * 由当前局面建立根节点，生成根节点的子节点 从根节点开始 利用UCB公式计算每个子节点的UCB值，选择最大值的子节点
 * 若此节点不是叶节点，则从此节点开始，重复2 直到遇到叶节点，如果叶节点曾经被模拟对局过，为这个叶节点生成子节点，从此节点开始，重复2
 * 否则对这个叶节点进行模拟对局，得到胜负结果，将这个收益按对应颜色更新到该节点及它的每一级祖先节点上去 回到1，除非时间结束或者达到预设循环次数
 * 从根节点的子节点中挑选平均收益最高的，作为最佳点
 */

public class RobotGo implements Runnable {
	private int[][] table;
	private int searchWidth;
	private int hasMove = 0;
	int devX = 0;
	int devY = 0;
	int f = 0;
	int s = 0;
	public boolean isFirst = false;
	public ArrayList<Point> child = new ArrayList<Point>();
	private Point lastMove;
	private ChessBorderController c;
	private boolean goOne = false;
	private int[][] backUp = new int[11][11];
	private int count = 0;
	private UctNode root;

	/**
	 * 将不同的小数组和对应偏移量算出获取
	 * 
	 * @param table
	 * @param c
	 */
	public RobotGo(int[][] table, ChessBorderController c) {
		int first = 0;
		int second = 0;
		this.c = c;
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				if (table[i][j] == 1) {
					first++;
					backUp[i][j] = 1;
					hasMove++;
				} else if (table[i][j] == -1) {
					second++;
					backUp[i][j] = -1;
					hasMove++;
				}
			}
		}
		isFirst = (first == second);
		try {
			lastMove = new Point(BasicVariables.HISTORY.peek().x, BasicVariables.HISTORY.peek().y);
		} catch (EmptyStackException ex) {
			lastMove = null;
		}
		System.out.println("table: " + hasMove);
		System.out.println("isFirst:" + isFirst);
		// System.out.println("lastMove:" + lastMove.x + " " + lastMove.y);
		goOne = evaluateTable(table, lastMove);
	}

	private boolean evaluateTable(int[][] table, Point p) {
		if (p == null) {
			return true;
		}
		for (int i = 3; i < 11; i++) {
			this.searchWidth = i;
			changeTable(table, p, i);
			if (checkTable()) {// ******************
				int canMove = 0, j = 0, k = 0;
				for (; j < this.table.length; j++) {
					for (k = 0; k < this.table[0].length; k++) {
						if (this.table[j][k] == 0) {
							canMove++;
						}
					}
				}
				if (canMove > j * k / 2) {
					break;
				}
			}
			this.lastMove.x += devX;
			this.lastMove.y += devY;
		}
		System.out.println("search width:   " + this.searchWidth + "     ");
		return false;
	}

	private boolean checkTable() {
		boolean flag = false;
		for (int i = 0; i < table.length; i++) {
			if (table[i][0] == 1) {
				if (checkWin(table, new Point(i, 0)) != 0) {
					return false;
				}
			} else if (table[i][0] == 0 || table[i][table[0].length - 1] == 0) {
				flag = true;
			}
		}
		for (int i = 0; i < table[0].length; i++) {
			if (table[0][i] == -1) {
				if (checkWin(table, new Point(0, i)) != 0) {
					return false;
				}
			} else if (table[0][i] == 0 || table[table.length - 1][i] == 0) {
				flag = true;
			}
		}
		return flag;
	}

	private void changeTable(int[][] table, Point p, int range) {
		int limXF = p.x - range;
		int limXN = p.x + range;
		int limYF = p.y - range;
		int limYN = p.y + range;
		if (limXF < 0) {
			limXF = 0;
		}
		if (limXN > 10) {
			limXN = 10;
		}

		if (limYF < 0) {
			limYF = 0;
		}
		if (limYN > 10) {
			limYN = 10;
		}

		devX = limXF;
		devY = limYF;
		this.table = new int[limXN - limXF + 1][limYN - limYF + 1];
		for (int i = 0; i < this.table.length; i++) {
			for (int j = 0; j < this.table[0].length; j++) {
				this.table[i][j] = table[limXF + i][limYF + j];
			}
		} // 数据装入
		this.lastMove.x -= devX;
		this.lastMove.y -= devY;
	}

	@Override
	public void run() {
		if (goOne) {
			BasicVariables.ROBOTMOVE = new Point(5, 5);
		} else {
			BasicVariables.ROBOTMOVE = UctSearch();
		}
		c.dd();
	}

	private Point UctSearch() {
		// long startTime = System.currentTimeMillis();
		// long endTime = System.currentTimeMillis();
		UctNode node = new UctNode();
		root = node;
		node.lastMove = lastMove;
		int t = 0;
		// while (endTime - startTime < 15000) {// 1000为1秒
		while (t++ < 300000.0 / this.searchWidth) {
			playSimulation(node);
			// endTime = System.currentTimeMillis();
		}
		setBest(node);
		// if(node.win * 1.0 / node.visit < 0.4){
		// t = 0;
		// node.child = new ArrayList<>();
		// this.table = backUp;
		// devX = 0;
		// devY = 0;
		// while (t++ < 10000) {
		// System.out.println("15");
		// playSimulation(node);
		// }
		// }
		// for(int i = 0; i < node.child.size(); i++){
		// System.out.println(node.child.get(i).win / (1.0*
		// node.child.get(i).visit) + " " + node.child.get(i).visit);
		// }

		// for(int i = 0; i < table.length; i++){
		// for(int j = 0; j < table[0].length; j++){
		// System.out.print(table[i][j] + " ");
		// }
		// System.out.println();
		// }

		System.out.println("first win:	" + f + "  second win: 	" + s);
		System.out.println("devX: " + devX + "  devY: " + devY);
		System.out.println("all node " + count);
		System.out.println("win rate: " + node.win * 1.0 / node.visit);
		try {
			node.bestMove.x += devX;
			node.bestMove.y += devY;
			return node.bestMove;
		} catch (NullPointerException ex) {
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					if (table[i][j] == 0) {
						return new Point(i, j);
					}
				}
			}
			throw new NullPointerException();
		}
	}

	private Point getRamdom() {
		int x = (int) (Math.random() * 1000) % (table.length);
		int y = (int) (Math.random() * 1000) % (table[0].length);
		return new Point(x, y);
	}

	boolean randomResult = false;

	/**
	 * 
	 * @param node
	 *            uct节点
	 * @param table
	 *            棋盘
	 * @param isFirst
	 *            最后一次落子的先后手情况
	 * @return
	 */
	private void playSimulation(UctNode node) {
		int result = checkWin(table, node.lastMove);
		if (result != 0) {
			// System.out.println(node.lastMove);
			if (isFirst == (result == 1)) {
				randomResult = true;
			} else {
				randomResult = false;
			}
		} else if (node.visit == 0) {
			count++;
			result = randomMove(node);
			if (isFirst == (result == 1)) {
				randomResult = true;
			} else {
				randomResult = false;
			}
		} else {
			if (node.child.isEmpty()) {
				node.getAllChildren(table);
			}
			UctNode next = node.bestSelect(root, table[node.lastMove.x][node.lastMove.y] == 1, isFirst);
			// if(node.flag == 1){
			// System.out.println(next.lastMove);
			// }
			this.move(table[node.lastMove.x][node.lastMove.y] == 1, next.lastMove);
			playSimulation(next);
			this.unmove(next.lastMove);
		}

		node.visit++;
		updataWin(node);
		// if (node.child.size() != 0) {
		// setBest(node);
		// }
	}

	private void updataWin(UctNode node) {
		if (this.randomResult) {
			node.win++;
		}
	}

	private void setBest(UctNode node) {
		double max = -1;
		for (int i = 0; i < node.child.size(); i++) {
			UctNode tmp = node.child.get(i);
			if (tmp.win / (1.0 * tmp.visit) > max) {
				max = tmp.win * 1.0 / tmp.visit;
				node.bestMove = tmp.lastMove;
				// System.out.println(max);
			}
		}
	}

	/**
	 * 随机模拟至棋局结束
	 * 
	 * @param table
	 *            当前棋盘
	 * @param node
	 *            模拟起始UCT节点
	 * @return 先手胜返回1，后手胜返回-1
	 */
	private int randomMove(UctNode node) {
		int t = this.checkWin(table, node.lastMove);
		boolean isFirst = false;
		if (table[node.lastMove.x][node.lastMove.y] == -1) {
			isFirst = true;
		}
		Stack<Point> history = new Stack<>();
		while (t == 0) {
			Point p = this.getRamdom();
			if (table[p.x][p.y] == 0) {
				history.push(p);
				if (isFirst) {
					table[p.x][p.y] = 1;
				} else {
					table[p.x][p.y] = -1;
				}
				t = this.checkWin(table, history.peek());
				isFirst = !isFirst;
			}
		}
		Point tmp = null;
		while (!history.isEmpty()) {
			tmp = history.pop();
			table[tmp.x][tmp.y] = 0;
		} // 恢复
		return t;
	}

	/**
	 * 
	 * @param table
	 *            当前棋盘
	 * @param point
	 *            上一手落子坐标
	 * @return 先手胜利返回1，后手胜利返回-1，无结果返回0
	 */
	private int checkWin(int[][] table, Point point) {
		if (point == null) {
			return 0;
		}
		boolean start = false;
		boolean end = false;
		boolean[][] tmpTable = new boolean[table.length][table[0].length];
		tmpTable[point.x][point.y] = true;
		List<Point> p = getNeighbor(table, tmpTable, point);

		if (table[point.x][point.y] == 1) { // 先手
			if (point.x == 0) {
				start = true;
			} else if (point.x == table.length - 1) {
				end = true;
			}
			while (!p.isEmpty()) {
				Point t = p.remove(p.size() - 1);
				if (t.x == 0) {
					start = true;
				} else if (t.x == table.length - 1) {
					end = true;
				}
				if (start && end) {
					f++;
					return 1;
				}
				List<Point> tmp = getNeighbor(table, tmpTable, t);
				p.addAll(tmp);
			}
		} else {
			if (point.y == 0) {
				start = true;
			} else if (point.y == table[0].length - 1) {
				end = true;
			}
			while (!p.isEmpty()) {
				Point t = p.remove(p.size() - 1);
				if (t.y == 0) {
					start = true;
				} else if (t.y == table[0].length - 1) {
					end = true;
				}
				if (start && end) {
					s++;
					return -1;
				}
				List<Point> tmp = getNeighbor(table, tmpTable, t);
				p.addAll(tmp);
			}
		}
		return 0;
	}

	// 从输入点开始向围绕点拓展
	private List<Point> getNeighbor(int[][] table, boolean[][] tmpTable, Point mid) {
		int m = mid.x;
		int n = mid.y;
		ArrayList<Point> allNeighbor = new ArrayList<>();
		allNeighbor.add(new Point(m - 1, n));
		allNeighbor.add(new Point(m - 1, n + 1));
		allNeighbor.add(new Point(m + 1, n - 1));
		allNeighbor.add(new Point(m + 1, n));
		allNeighbor.add(new Point(m, n - 1));
		allNeighbor.add(new Point(m, n + 1));

		int flag = 0;
		for (int i = 0; i < 6; i++) {
			if (!allNeighbor.get(i).outRange(table.length, table[0].length)) {
				if (!tmpTable[allNeighbor.get(i).x][allNeighbor.get(i).y]) {
					tmpTable[allNeighbor.get(i).x][allNeighbor.get(i).y] = true;
					if (table[mid.x][mid.y] == table[allNeighbor.get(i).x][allNeighbor.get(i).y]) {
						allNeighbor.set(flag++, allNeighbor.get(i));
					}
				}
			}
		}
		if (flag == 0) {
			return new ArrayList<>();
		} else {
			return allNeighbor.subList(0, flag); // 提出多余的点
		}
	}

	private boolean move(boolean isFirst, Point p) {
		if (!isFirst) {
			table[p.x][p.y] = 1;
		} else {
			table[p.x][p.y] = -1;
		}
		return true;
	}

	private void unmove(Point p) {
		table[p.x][p.y] = 0;
	}
}
