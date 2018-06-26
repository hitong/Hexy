package application;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.BasicVariables;
import util.Message;
import util.Point;
import view.ChessBorderController;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public ChessBorderController c;
	ObservableList<Message> message = FXCollections.observableArrayList();
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Hex棋");
		this.primaryStage.setResizable(false);
		this.primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});
		Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
		showView();
	}

	public void showView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/ChessBorde.fxml"));
			BorderPane page = (BorderPane) loader.load();
			Scene scene = new Scene(page);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
			c = loader.getController();
			c.setMain(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void move(String id) {
		String[] tmp = id.split(" ");
		int m = Integer.parseInt(tmp[0]);
		int n = Integer.parseInt(tmp[1]);
		if (BasicVariables.HISTORY.size() % 2 == 0) {
			BasicVariables.TABLE[m][n] = 1;
			this.message.add(new Message("先手落子" + (BasicVariables.HISTORY.size() / 2 + 1), id));
		} else {
			BasicVariables.TABLE[m][n] = -1;
			this.message.add(new Message("后手落子" + (BasicVariables.HISTORY.size() / 2 + 1), id));
		}
		BasicVariables.HISTORY.push(new Point(m, n));
	}

	public Point undo() {
		if (BasicVariables.HISTORY.isEmpty()) {
			return null;
		} else {
			if (BasicVariables.HISTORY.size() % 2 == 1) {
				message.add(new Message("先手悔棋", BasicVariables.HISTORY.peek().toString()));
			} else {
				message.add(new Message("后手悔棋", BasicVariables.HISTORY.peek().toString()));
			}
			return BasicVariables.HISTORY.pop();
		}
	}

	public void reStart(TextArea area) {
		BasicVariables.HISTORY.clear();
		BasicVariables.TABLE = new int[11][11];
		message.clear();
	}

	public boolean checkWin() {
		if (BasicVariables.HISTORY.size() == 0) {
			return false;
		}
		boolean[][] table = new boolean[11][11];
		boolean isFirst;
		if (BasicVariables.HISTORY.size() % 2 == 1) {
			isFirst = true;
		} else {
			isFirst = false;
		}
		boolean start;
		boolean end;

		table[BasicVariables.HISTORY.peek().x][BasicVariables.HISTORY.peek().y] = true;
		List<Point> p = getNeighbor(new Point(BasicVariables.HISTORY.peek().x, BasicVariables.HISTORY.peek().y), table);
		if (p == null) {
			return false;
		}
		if (isFirst) {
			start = (BasicVariables.HISTORY.peek().x == 0);
			end = (BasicVariables.HISTORY.peek().x == 10);
			while (!p.isEmpty()) {
				Point t = p.remove(p.size() - 1);
				if (t.x == 0) {
					start = true;
				} else if (t.x == 10) {
					end = true;
				}
				if (start && end) {
					if (BasicVariables.UndoCheckWin == 0) {
						BasicVariables.UndoCheckWin = 1;
						c.showResult(true);
					}
					c.stopAllTime();
					// showInfo("总步数：" + BasicVariables.HISTORY.size()
					// + "\n总耗时：" + ShowTime.getAllTime(c.getTime(true),
					// c.getTime(false))
					// + "\n对局结果：先手胜利"
					// );
					return true;
				}
				List<Point> tmp = getNeighbor(t, table);
				p.addAll(tmp);
			}
		} else {
			start = (BasicVariables.HISTORY.peek().y == 0);
			end = (BasicVariables.HISTORY.peek().y == 10);
			while (!p.isEmpty()) {
				Point t = p.remove(p.size() - 1);
				if (t.y == 0) {
					start = true;
				} else if (t.y == 10) {
					end = true;
				}
				if (start && end) {
					if (BasicVariables.UndoCheckWin == 0) {
						BasicVariables.UndoCheckWin = -1;
						c.showResult(false);
					}
					c.stopAllTime();
					// showInfo("总步数：" + BasicVariables.HISTORY.size()
					// + "\n总耗时：" + ShowTime.getAllTime(c.getTime(true),
					// c.getTime(false))
					// + "\n对局结果：后手胜利"
					// );
					return true;
				}
				List<Point> tmp = getNeighbor(t, table);
				p.addAll(tmp);
			}
		}
		BasicVariables.UndoCheckWin = 0;
		return false;
	}

	private List<Point> getNeighbor(Point mid, boolean[][] table) {
		int m = mid.x;
		int n = mid.y;
		ArrayList<Point> allNeighbor = new ArrayList<>();
		allNeighbor.add(new Point(m - 1, n));
		allNeighbor.add(new Point(m - 1, n + 1));
		allNeighbor.add(new Point(m + 1, n - 1));
		allNeighbor.add(new Point(m + 1, n));
		allNeighbor.add(new Point(m, n - 1));
		allNeighbor.add(new Point(m, n + 1));

		int f = 0;
		for (int i = 0; i < 6; i++) {
			if (!allNeighbor.get(i).outRange(BasicVariables.TABLE.length, BasicVariables.TABLE[0].length)) {
				if (!table[allNeighbor.get(i).x][allNeighbor.get(i).y]) {
					table[allNeighbor.get(i).x][allNeighbor.get(i).y] = true;
					Polygon p1 = (Polygon) c.getTable().getChildren().get(mid.x * 11 + mid.y);
					Polygon p2 = (Polygon) c.getTable().getChildren()
							.get(allNeighbor.get(i).x * 11 + allNeighbor.get(i).y);
					if (p1.getFill().equals(p2.getFill())) {
						allNeighbor.set(f++, allNeighbor.get(i));
					}
				}
			}
		}
		if (f == 0) {
			return new ArrayList<>();
		}
		return allNeighbor.subList(0, f);
	}

	public void showInfo(String s) {
		Alert information = new Alert(Alert.AlertType.INFORMATION);
		information.initStyle(StageStyle.DECORATED);
		information.setHeaderText("新消息");
		information.setContentText(s);
		Button infor = new Button("show Information");
		infor.setOnAction((ActionEvent) -> {
			information.showAndWait();
		});
		information.show();
	}

	public void showErr(String s) {
		Alert information = new Alert(Alert.AlertType.ERROR);
		information.initStyle(StageStyle.DECORATED);
		information.setHeaderText("错误");
		information.setContentText(s);
		Button infor = new Button("show Information");
		infor.setOnAction((ActionEvent) -> {
			information.showAndWait();
		});
		information.show();
	}

	public ObservableList<Message> getMessage() {
		return this.message;
	}

	public void writeToFile() {
		try (PrintWriter out = new PrintWriter(new File("Hex.txt"))) {
			for (int i = 0; i < BasicVariables.HISTORY.size(); i++) {
				out.write("第" + (i + 1) + "手坐标" + BasicVariables.HISTORY.get(i) + "    ");
			}
			showInfo("记录已经保存至Hex.txt文档中");
		} catch (Exception ex) {
			showErr("当前暂无记录或存储空间已满，存储失败");
		}
		System.out.println("位置");
	}
}
