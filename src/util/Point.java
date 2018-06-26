package util;

/**
 * 将落子坐标的X和Y进行包装
 * @author Supertong
 *
 */
public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean outRange(int tableX, int tableY) {
		return x < 0 || y < 0 || x > tableX - 1 || y > tableY - 1;
	}
		
	public String toString() {
		return x + " " + y;
	}
}
