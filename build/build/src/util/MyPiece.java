package util;

import util.BasicVariables;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

public class MyPiece {

	public static Polygon myPiece(double x, double y) {
		Polygon polygon = new Polygon();
		polygon.getPoints()
				.addAll(new Double[] { x, y - 2 * BasicVariables.size, x - 1.2 * BasicVariables.size,
						y - 0.75 * BasicVariables.size, x - 1.2 * BasicVariables.size, y + 0.75 * BasicVariables.size,
						x, y + 2 * BasicVariables.size, x + 1.2 * BasicVariables.size, y + 0.75 * BasicVariables.size,
						x + 1.2 * BasicVariables.size, y - 0.75 * BasicVariables.size });
		polygon.setStroke(Color.BLACK);
		polygon.setStrokeType(StrokeType.INSIDE);
		polygon.setFill(BasicVariables.SPACE_COLOR);
		return polygon;
	}

	public static Polygon chessBorde(double x, double y, int direction) {
		Polygon polygon = new Polygon();
		switch (direction) {
		case BasicVariables.LEFT:
			polygon.getPoints()
					.addAll(new Double[] { x - 1.2 * BasicVariables.size, y - 0.75 * BasicVariables.size,
							x - 1.2 * BasicVariables.size, y + 0.75 * BasicVariables.size,
							x - 2.4 * BasicVariables.size, y - 2 * BasicVariables.size });
			polygon.setFill(BasicVariables.SECOND_COLOR);
			break;
		case BasicVariables.RIGHT:
			polygon.getPoints()
					.addAll(new Double[] { x + 2.4 * BasicVariables.size, y + 2 * BasicVariables.size,
							x + 1.2 * BasicVariables.size, y + 0.75 * BasicVariables.size,
							x + 1.2 * BasicVariables.size, y - 0.75 * BasicVariables.size });
			polygon.setFill(BasicVariables.SECOND_COLOR);
			break;
		case BasicVariables.TOP:
			polygon.getPoints().addAll(new Double[] { x, y - 2 * BasicVariables.size, x - 1.2 * BasicVariables.size,
					y - 0.75 * BasicVariables.size, x - 2.4 * BasicVariables.size, y - 2 * BasicVariables.size, });
			polygon.setFill(BasicVariables.FIRST_COLOR);
			break;
		case BasicVariables.BOTTOM:
			polygon.getPoints().addAll(new Double[] { x + 1.2 * BasicVariables.size, y + 0.75 * BasicVariables.size, x,
					y + 2 * BasicVariables.size, x + 2.4 * BasicVariables.size, y + 2 * BasicVariables.size, });
			polygon.setFill(BasicVariables.FIRST_COLOR);
			break;
		}
		polygon.setStroke(Color.BLACK);
		polygon.setStrokeType(StrokeType.INSIDE);
		return polygon;
	}

	public static Polygon northeast() {
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(new Double[] { 558 + 1.2 * BasicVariables.size, 80 - 0.75 * BasicVariables.size,
				558.0, 80.0 - 2 * BasicVariables.size, 572.0, 80.0 - 2 * BasicVariables.size });
		polygon.setFill(BasicVariables.FIRST_COLOR);
		polygon.setStroke(Color.BLACK);
		polygon.setStrokeType(StrokeType.INSIDE);
		return polygon;
	}

	public static Polygon southwest() {
		Polygon polygon = new Polygon();
		polygon.getPoints()
				.addAll(new Double[] { 315.0, 580.0 + 2 * BasicVariables.size, 315.0 - 1.2 * BasicVariables.size,
						580.0 + 0.75 * BasicVariables.size, 342.0 - 2 * BasicVariables.size,
						580.0 + 2 * BasicVariables.size });
		polygon.setFill(BasicVariables.SECOND_COLOR);
		polygon.setStroke(Color.BLACK);
		polygon.setStrokeType(StrokeType.INSIDE);
		return polygon;
	}
}