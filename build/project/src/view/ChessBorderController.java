package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import application.Main;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.BasicVariables;
import util.Message;
import util.MyPiece;
import util.Point;
import util.RobotGo;
import util.ShowTime;

public class ChessBorderController {
	@FXML
	private TableView<Message> tablePane;

	@FXML
	private TableColumn<Message, String> peo;

	@FXML
	private TableColumn<Message, String> message;

	@FXML
	TextArea result;

	@FXML
	TextArea firstTime;// 先手计时

	@FXML
	TextArea secondTime;// 后手计时

	@FXML
	Pane mainPane; // 棋盘界面

	@FXML
	ProgressIndicator pro;

	@FXML
	ProgressBar bar;

	Main main;

	@FXML
	private void setRobot() {
		BasicVariables.ROBOT = 1;
	}

	@FXML
	private void setMan() {
		BasicVariables.ROBOT = 0;
	}

	@FXML
	private void initialize() {
		peo.setCellValueFactory(cellData -> cellData.getValue().getPeople());
		message.setCellValueFactory(cellData -> cellData.getValue().getMessage());
	}

	Thread robot;

	public void robotGo() {
		setDisable(true);
		if (robot == null) {
			robot = new Thread(new RobotGo(BasicVariables.TABLE, main.c));
			robot.start();
		} else {
			if (!robot.isAlive()) {
				robot = new Thread(new RobotGo(BasicVariables.TABLE, main.c));
				robot.start();
			}
		}
	}

	public void setMain(Main main) {
		this.main = main;
		firstTime.setText(ShowTime.initTime());
		secondTime.setText(ShowTime.initTime());
		result.setText("0:0");
		this.tablePane.setItems(main.getMessage());
		loadChessBord();
		mainPane.setStyle("-fx-background-image:url('/bc.jpg');");
	}

	public Task<Void> createWorker() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				BasicVariables.TMP = 0;
				for (int i = 0; i < 100; i++) {
					BasicVariables.TMP += 0.1;
					Thread.sleep(100);
					updateMessage("2000 milliseconds");
					updateProgress(BasicVariables.TMP, 10);
				}
				return null;
			}
		};
	}

	Task<Void> copyWorker;

	private void loadChessBord() {
		for (int i = 0; i < 11; i++) {
			double y = i * 50 + 80;
			for (int j = 0; j < 11; j++) {
				double x = 23.5 * i + 47.8 * j + 80;
				Polygon po = MyPiece.myPiece(x, y);
				po.setId(i + " " + j);

				po.setOnMouseClicked(event -> {
					// copyWorker = createWorker();
					// pro.progressProperty().unbind();
					// pro.progressProperty().bind(copyWorker.progressProperty());
					// bar.progressProperty().unbind();
					// bar.progressProperty().bind(copyWorker.progressProperty());
					// new Thread(copyWorker).start();
					if (po.getFill() != BasicVariables.MOUSE_ON_FIRST
							&& po.getFill() != BasicVariables.MOUSE_ON_SECOND) {
						return;
					}
					if (BasicVariables.HISTORY.size() % 2 == 0) {
						po.setFill(BasicVariables.FIRST_COLOR);
					} else {
						po.setFill(BasicVariables.SECOND_COLOR);
					}
					main.move(po.getId());
					if (main.checkWin()) {
						setDisable(true);
					} else {
						showTime();
						if (BasicVariables.ROBOT == 1) {
							robotGo();
						}
					}
				});
				po.setOnMouseMoved(event -> {
					if (po.getFill() == BasicVariables.SPACE_COLOR) {
						if (BasicVariables.HISTORY.size() % 2 == 0) {
							po.setFill(BasicVariables.MOUSE_ON_FIRST);
						} else {
							po.setFill(BasicVariables.MOUSE_ON_SECOND);
						}
					}
				});
				po.setOnMouseExited(event -> {
					if (po.getFill() == BasicVariables.MOUSE_ON_FIRST
							|| po.getFill() == BasicVariables.MOUSE_ON_SECOND) {
						po.setFill(BasicVariables.SPACE_COLOR);
					}
				});
				mainPane.getChildren().add(po);
			}
		} // 棋子添加
		mainPane.getChildren().add(MyPiece.northeast());
		mainPane.getChildren().add(MyPiece.southwest());
		Text num;
		for (int i = 0; i < 11; i++) {
			double y = i * 50 + 80;
			mainPane.getChildren().add(MyPiece.chessBorde(23.5 * i + 80, y, BasicVariables.LEFT));
			mainPane.getChildren().add(MyPiece.chessBorde(23.5 * i + 47.8 * 10 + 80, y, BasicVariables.RIGHT));
			mainPane.getChildren().add(MyPiece.chessBorde(47.8 * i + 80, 80, BasicVariables.TOP));
			mainPane.getChildren()
					.add(MyPiece.chessBorde(23.5 * 10 + 47.8 * i + 80, 10 * 50 + 80, BasicVariables.BOTTOM));
			num = new Text(i + "");
			num.setLayoutX(i * 47.5 + 75);
			num.setLayoutY(32);
			num.setFont(Font.font("微软雅黑", 20));
			mainPane.getChildren().add(num);

			num = new Text(i + "");
			num.setLayoutX(i * 23 + 30);
			num.setLayoutY(i * 50 + 90);
			num.setFont(Font.font("微软雅黑", 20));
			mainPane.getChildren().add(num);
		}
	}

	public void dd() {
		main.move(BasicVariables.ROBOTMOVE.x + " " + BasicVariables.ROBOTMOVE.y);
		Polygon p = (Polygon) mainPane.getChildren().get(BasicVariables.ROBOTMOVE.y + BasicVariables.ROBOTMOVE.x * 11);
		p.setFill(BasicVariables.SPACE_COLOR);
		if (BasicVariables.HISTORY.size() % 2 == 1) {
			p.setFill(BasicVariables.FIRST_COLOR);
		} else {
			p.setFill(BasicVariables.SECOND_COLOR);
		}

		if (main.checkWin()) {
			setDisable(true);
		} else {
			setDisable(false);
			showTime();
		}
	}

	private Thread first = new Thread(new Task<Void>() {
		@Override
		protected Void call() throws Exception {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				firstTime.setText(ShowTime.setTime(firstTime.getText()));
			}
		}
	});

	private Thread second = new Thread(new Task<Void>() {
		protected Void call() throws Exception {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				secondTime.setText(ShowTime.setTime(secondTime.getText()));
			}
		}
	});

	@SuppressWarnings("deprecation")
	private void showTime() {
		if (!first.isAlive() && !second.isAlive()) {
			first.start();
			second.start();
			first.suspend();
		} else if (BasicVariables.HISTORY.size() % 2 == 1) {
			first.suspend();
			second.resume();
		} else {
			first.resume();
			second.suspend();
		}
	}

	private void clean() {
		for (int i = 0; i < 11 * 11; i++) {
			Polygon p = (Polygon) mainPane.getChildren().get(i);
			p.setFill(BasicVariables.SPACE_COLOR);
		}
		this.setDisable(false);
	}

	public void showResult(boolean isFirst) {
		String[] re = result.getText().split(":");
		if (isFirst) {
			result.setText((Integer.parseInt(re[0]) + 1) + ":" + re[1]);
		} else {
			result.setText(re[0] + ":" + (Integer.parseInt(re[1]) + 1));
		}
	}

	@SuppressWarnings("deprecation")
	@FXML
	private void restart() {
		if (BasicVariables.HISTORY.size() != 0) {
			this.first.suspend();
			this.second.suspend();
			this.firstTime.setText(ShowTime.initTime());
			this.secondTime.setText(ShowTime.initTime());
		}
		clean();
		main.reStart(firstTime);
	}

	public void undo() {
		Point point = main.undo();
		if (point == null) {
			return;
		}
		if (BasicVariables.UndoCheckWin != 0) {
			String[] re = result.getText().split(":");
			if (BasicVariables.UndoCheckWin == 1) {
				result.setText((Integer.parseInt(re[0]) - 1) + ":" + re[1]);
			} else {
				result.setText(re[0] + ":" + (Integer.parseInt(re[1]) - 1));
			}
			BasicVariables.UndoCheckWin = 0;
		}
		BasicVariables.TABLE[point.x][point.y] = 0;
		Polygon p = (Polygon) mainPane.getChildren().get(point.x * 11 + point.y);
		p.setFill(BasicVariables.SPACE_COLOR);
		this.setDisable(false);
		this.showTime();
	}

	public String getTime(boolean isFirst) {
		if (isFirst) {
			return firstTime.getText();
		} else {
			return secondTime.getText();
		}
	}

	@SuppressWarnings("deprecation")
	public void stopAllTime() {
		first.suspend();
		second.suspend();
	}

	@FXML
	private void randomMove() {
		this.robotGo();
	}

	@FXML
	private void restore() {
		this.restart();
		File f = new File("Hex.txt");
		try {
			Scanner input = new Scanner(f);
			String s = input.nextLine();
			input.close();
			String[] t = s.split("  ");
			for (int i = 0; i <= t.length / 2; i++) {
				int m, n;
				String[] tmp = t[i * 2].split("坐标");
				tmp = tmp[1].split(" ");
				m = Integer.parseInt(tmp[0]);
				n = Integer.parseInt(tmp[1]);
				Polygon p = (Polygon) mainPane.getChildren().get(m * 11 + n);
				if (p.getFill().equals(BasicVariables.SPACE_COLOR)) {
					if (BasicVariables.HISTORY.size() % 2 == 0) {
						p.setFill(BasicVariables.FIRST_COLOR);
					} else {
						p.setFill(BasicVariables.SECOND_COLOR);
					}
					main.move(m + " " + n);
				}
			}
			if (main.checkWin()) {
				this.setDisable(true);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Pane getTable() {
		return mainPane;
	}

	private void setDisable(boolean flag) {
		if (flag) {
			for (int i = 0; i < 121; i++) {
				mainPane.getChildren().get(i).setDisable(true);
			}
		} else {
			for (int i = 0; i < 121; i++) {
				mainPane.getChildren().get(i).setDisable(false);
			}
		}
	}

	@FXML
	private void robotGoFirst() {
		this.restart();
		this.setRobot();
		this.robotGo();
	}

	@FXML
	private void explanation() {
		main.showInfo(BasicVariables.EXPRESION);
	}

	@FXML
	private void author() {
		main.showInfo(BasicVariables.ADITOR);
	}

	@FXML
	private void writeToFile() {
		main.writeToFile();
	}

	@FXML
	private void newGame() {
		this.restart();
		this.result.setText("0:0");
	}

	public void exit() {
		System.exit(0);
	}
}
