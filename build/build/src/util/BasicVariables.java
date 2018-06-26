package util;

import java.util.Stack;

import javafx.scene.paint.Color;

public class BasicVariables {
	public final static int size = 20;
	public final static int LEFT = 1;
	public final static int RIGHT = 2;
	public final static int TOP = 3;
	public final static int BOTTOM = 4;
	public final static Color FIRST_COLOR = Color.GREENYELLOW;
	public final static Color SECOND_COLOR = Color.ROYALBLUE;
	public final static Color SPACE_COLOR = Color.WHITE;
	public static int UndoCheckWin = 0;//该变量用于存储棋子撤回前胜负的状态，0为无结果，1为先手胜，-1为后手胜
	public static float TMP = 0;
	public static int ROBOT = 0;
	public static Point ROBOTMOVE = null;

	public final static Color MOUSE_ON_FIRST = Color.LIGHTGREEN;
	public final static Color MOUSE_ON_SECOND = Color.DARKTURQUOISE;
	public final static String EXPRESION = "Hex棋，又叫六角棋，译作海克斯棋。据说这个游戏是约翰·纳什发明的。"
			+ "棋盘为11×11的六边形小格子组成，它是一种两人添子类游戏（跟五子棋一样）。" 
			+ "它的游戏规则是：两人轮流下子，直到有一方用棋子沟通了两条边（比如先手沟通上"
			+ "下两边则胜利，后手沟通左右两边则胜利）";
	public final static String ADITOR = "重理最帅的桐爸爸:使用中有任何问题都可以通过QQ邮箱:1906194855@qq.com联系我\n共同学习共同进步！！！😁";
	public static int[][] TABLE = new int[11][11];// 先手为1，后手为-1，空白为0
	public static Stack<Point> HISTORY = new Stack<>();
}
